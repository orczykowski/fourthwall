package com.fourthwall.moviedb.domain.movie.rating;

import com.fourthwall.moviedb.domain.movie.Movie
import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.MovieRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.concurrent.TimeUnit

@Service
data class RatingService(
    private val movieRepository: MovieRepository,
    private val ratingsRepository: RatingRepository,
    private val externalRatingProvider: ExternalRatingProvider,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val clock: Clock,
) : MovieEvaluator, MovieRatingDetailsProvider {
    private val logger: Logger = LoggerFactory.getLogger(RatingService::class.java)
    override fun rateMovie(command: RateMovieCommand) {
        validate(command)
        ratingsRepository.store(
            RatingDetails(
                movieId = command.movieId,
                rate = command.rate,
                userName = command.userName,
                createdAt = clock.instant()
            )
        )
        val newRatingValue = calculateAverageRating(command.movieId)

        applicationEventPublisher.publishEvent(
            MovieUserRatingUpdated(
                command.movieId,
                newRatingValue,
                clock.instant()
            )
        )
    }

    @Scheduled(
        timeUnit = TimeUnit.MINUTES,
        fixedRateString = "\${domain.update-movie-info.fixed-rate}",
    )
    fun updateImdb() {
        logger.info("Updating movies ratings")
        movieRepository.findAll().forEach { movie ->
            val externalRating = externalRatingProvider.findBy(movie.id)
            logger.info("External rating for movie ${movie.id} is $externalRating")
            if (shouldUpdateRating(externalRating, movie)) {
                applicationEventPublisher.publishEvent(
                    MovieImdbRatingUpdated(movie.id, externalRating!!, clock.instant())
                )
            }
        }
    }

    override fun findMovieRatings(query: RatingQuery): Ratings? {
        val movie = movieRepository.findById(query.movieId) ?: return null
        ratingsRepository.findAllByMovieId(query.movieId)
            .map { SimpleRating(it.userName, it.rate.value) }
            .let {
                return Ratings(
                    imdb = movie.rating.imdb,
                    userRating = movie.rating.userRating,
                    userRatings = it
                )
            }
    }

    private fun shouldUpdateRating(externalRating: ExternalRating?, movie: Movie) =
        externalRating != null && movie.rating.imdb != externalRating.value

    private fun calculateAverageRating(movieId: MovieId) = ratingsRepository.findAllByMovieId(movieId)
        .asSequence()
        .map { it.rate.value }
        .average()


    private fun validate(command: RateMovieCommand) {
        movieRepository.findById(command.movieId) ?: throw InvalidRateOperationException(command.movieId)
    }
}


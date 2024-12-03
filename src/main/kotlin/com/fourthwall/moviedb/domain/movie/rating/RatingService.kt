package com.fourthwall.moviedb.domain.movie.rating;

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.MovieRepository
import org.springframework.stereotype.Service
import java.time.Clock

@Service
data class RatingService(
    private val movieRepository: MovieRepository,
    private val ratingsRepository: RatingRepository,
    private val clock: Clock,
) : MovieEvaluator, MovieRatingDetailsProvider {

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
        //publish event
        TODO("Not yet implemented")
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

    private fun calculateAverageRating(movieId: MovieId) = ratingsRepository.findAllByMovieId(movieId)
        .asSequence()
        .map { it.rate.value }
        .average()


    private fun validate(command: RateMovieCommand) {
        movieRepository.findById(command.movieId) ?: throw InvalidRateOperationException(command.movieId)
    }
}


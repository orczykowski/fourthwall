package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.rating.MovieImdbRatingUpdated
import com.fourthwall.moviedb.domain.movie.rating.MovieRatingUpdated
import com.fourthwall.moviedb.domain.movie.rating.MovieUserRatingUpdated
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MovieService(private val movieRepository: MovieRepository) : MovieProvider, MovieUpdater {
    override fun findMovies(criteria: MoviesQuery): List<MovieSnapshot> {
        if (criteria.isEmpty()) {
            return movieRepository.findAll().map { it.toSnapshot() }
        }
        return movieRepository.findBy(criteria.category!!).map { it.toSnapshot() }
    }

    override fun getMovieById(id: MovieId): MovieSnapshot =
        movieRepository.findById(id)?.toSnapshot() ?: throw MovieNotFoundException(id)

    @Async
    @EventListener
    override fun onUpdateUserMovieRating(businessEvent: MovieRatingUpdated) {
        val movie = movieRepository.findById(businessEvent.movieId)
            ?: throw MovieNotFoundException(businessEvent.movieId)
        when (businessEvent) {
            is MovieImdbRatingUpdated -> movieRepository.save(movie.updateImdb(businessEvent.rate))
            is MovieUserRatingUpdated -> movieRepository.save(movie.updateUserRating(businessEvent.rate))
        }
    }
}
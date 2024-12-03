package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId

interface MovieEvaluator {
    fun rateMovie(command: RateMovieCommand)
}

data class RateMovieCommand(val movieId: MovieId, val rate: UserRateValue, val userName: String) {
    init {
        if (userName.isBlank()) {
            throw InvalidUserException()
        }
    }
}
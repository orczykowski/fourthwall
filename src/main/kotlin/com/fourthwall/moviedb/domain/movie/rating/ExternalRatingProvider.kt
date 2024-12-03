package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId

interface ExternalRatingProvider {
    fun findBy(movieId: MovieId): ExternalRating?
}

data class ExternalRating(val value: String)
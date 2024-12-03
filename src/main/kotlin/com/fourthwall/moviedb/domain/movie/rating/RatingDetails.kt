package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.Instant

data class RatingDetails(
    val movieId: MovieId,
    val rate: UserRateValue,
    val userName: String,
    val createdAt: Instant,
)
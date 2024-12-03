package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.Instant

data class UserRatingUpdated(val movieId: MovieId, val userRating: Double, val instant: Instant)

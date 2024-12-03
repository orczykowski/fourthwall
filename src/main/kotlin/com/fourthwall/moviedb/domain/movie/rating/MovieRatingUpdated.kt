package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.Instant

sealed class MovieRatingUpdated(
    val movieId: MovieId,
    val instant: Instant,
)

class MovieUserRatingUpdated(
    movieId: MovieId,
    val rate: Double,
    instant: Instant,
) : MovieRatingUpdated(movieId, instant)

class MovieImdbRatingUpdated(
    movieId: MovieId,
    val rate: ExternalRating,
    instant: Instant,
) : MovieRatingUpdated(movieId, instant)

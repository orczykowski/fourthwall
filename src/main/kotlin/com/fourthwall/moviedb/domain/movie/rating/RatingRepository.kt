package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId

interface RatingRepository {
    fun store(rating: RatingDetails)
    fun findAllByMovieId(movieId: MovieId): List<RatingDetails>
}

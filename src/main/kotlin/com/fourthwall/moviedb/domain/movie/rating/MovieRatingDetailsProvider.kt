package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId

interface MovieRatingDetailsProvider {
    fun findMovieRatings(query: RatingQuery): Ratings?
}

data class RatingQuery(val movieId: MovieId)
data class SimpleRating(val user: String, val rating: Int)
data class Ratings(val imdb: String, val userRating: Double, val userRatings: List<SimpleRating>)
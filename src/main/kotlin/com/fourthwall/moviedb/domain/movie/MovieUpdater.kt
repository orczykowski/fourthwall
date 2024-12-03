package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.rating.MovieRatingUpdated

interface MovieUpdater {
    fun onUpdateUserMovieRating(businessEvent: MovieRatingUpdated)
}

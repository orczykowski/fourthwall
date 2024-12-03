package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.MovieId

interface ExternalMovieIdProvider {
    fun provideFor(movieId: MovieId): String?
}
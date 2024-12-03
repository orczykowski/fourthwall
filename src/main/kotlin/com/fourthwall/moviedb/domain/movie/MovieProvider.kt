package com.fourthwall.moviedb.domain.movie

interface MovieProvider {
    fun findMovies(criteria: MoviesQuery): List<MovieSnapshot>
    fun getMovieById(id: MovieId): MovieSnapshot
}

data class MoviesQuery(val category: Category? = null) {
    fun isEmpty(): Boolean {
        return category == null
    }
}


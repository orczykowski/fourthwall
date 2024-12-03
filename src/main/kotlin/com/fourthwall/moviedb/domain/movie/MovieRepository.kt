package com.fourthwall.moviedb.domain.movie

interface MovieRepository {
    fun findById(id: MovieId): Movie?
    fun findBy(category: Category): List<Movie>
    fun findAll(): List<Movie>
    fun save(movie: Movie)
}

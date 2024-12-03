package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.Movie
import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.MoviesQuery
import com.fourthwall.moviedb.domain.movie.SimpleMovie

interface MovieRepository {
    fun findById(id: MovieId): Movie?
    fun findBy(category: Category): List<SimpleMovie>
    fun findAll(): List<SimpleMovie>
    fun save(movie: Movie)
}

package com.fourthwall.moviedb.domain.movie

import org.springframework.stereotype.Service

@Service
class MovieService(private val movieRepository: MovieRepository) : MovieProvider {

    override fun findMovies(criteria: MoviesQuery): List<SimpleMovie> {
        if (criteria.isEmpty()) {
            return movieRepository.findAll()
        }
        return movieRepository.findBy(criteria.category!!)
    }

    override fun getMovieById(id: MovieId): MovieSnapshot =
        movieRepository.findById(id)?.toSnapshot() ?: throw MovieNotFoundException(id)

}
package com.fourthwall.moviedb.adapter.properties

import com.fourthwall.moviedb.domain.movie.MovieId
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "movie-catalog")
data class MovieCatalog(
    val movies: List<BaseMovieInfo>,
) {
    fun getImoDbMovieId(movieId: MovieId) = movies.find { it.movieId == movieId.value }?.imdbId

}

data class BaseMovieInfo(
    val movieId: String,
    val imdbId: String,
)

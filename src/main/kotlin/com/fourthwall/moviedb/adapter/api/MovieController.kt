package com.fourthwall.moviedb.adapter.api

import com.fourthwall.moviedb.domain.movie.*
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
@RequestMapping(
    path = ["/api/public/movies"],
    headers = [
        "X-Api-Version=${ApiVersion.V1}",
        "Content-Type=application/json"]
)
class MovieController(private val movieProvider: MovieProvider) {

    @GetMapping
    fun findMovies(@RequestParam(required = false) category: String?): MoviesDto {
        val movies = movieProvider
            .findMovies(MoviesQuery(category?.toCategory()))
            .map { SimpleMovieDto(it.id, it.title) }
        return MoviesDto(movies)
    }

    @GetMapping("/{id}")
    fun getMovie(@PathVariable("id") id: String) = MovieDto.from(movieProvider.getMovieById(MovieId(id)))

    data class MoviesDto(
        val movies: List<SimpleMovieDto>,
    )

    data class SimpleMovieDto(
        val id: String,
        val title: String,
    )

    data class MovieDto(
        val id: String,
        val title: String,
        val description: String,
        val duration: Duration,
        val ratingImDb: String,
        val userRating: Double,
        val category: String,
    ) {
        companion object {
            fun from(movie: MovieSnapshot): MovieDto {
                return MovieDto(
                    id = movie.id,
                    title = movie.title,
                    description = movie.description,
                    duration = movie.duration,
                    ratingImDb = movie.ratingImDb,
                    userRating = movie.userRating,
                    category = movie.category
                )
            }
        }
    }

    fun String?.toCategory() = this?.let { Category(it) }
}
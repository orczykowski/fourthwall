package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MongoMovieRepositoryFacade(
    private val movieMongoRepository: MovieMongoRepository,
) : MovieRepository {
    override fun findById(id: MovieId) = movieMongoRepository.findById(id.value).map {
        Movie(
            id = MovieId(it.id),
            details = Details(
                title = Title(it.title),
                description = Description(it.description),
                duration = it.duration,
                category = Category(it.category),
            ),
            rating = MovieRatings(it.ratingImDb, it.userRating),
        )
    }.orElse(null)

    override fun findBy(category: Category) =
        movieMongoRepository.findByCategory(category.value).map {
            SimpleMovie(
                movieId = MovieId(it.id),
                title = Title(it.title),
            )
        }


    override fun findAll() = movieMongoRepository.findAll().map {
        SimpleMovie(
            movieId = MovieId(it.id),
            title = Title(it.title),
        )
    }

    override fun save(movie: Movie) {
        movieMongoRepository.save(
            MovieDocument(
                id = movie.id.value,
                title = movie.details.title.value,
                description = movie.details.description.value,
                duration = movie.details.duration,
                ratingImDb = movie.rating.imdb,
                userRating = movie.rating.userRating,
                category = movie.details.category.value,
            )
        )
    }
}

interface MovieMongoRepository : MongoRepository<MovieDocument, String> {
    fun findByCategory(value: String): List<MovieDocument>

}

@Document(collation = "movies")
data class MovieDocument(
    @Id
    val id: String,
    val title: String,
    val description: String,
    val duration: Duration,
    val ratingImDb: String,
    val userRating: Double,
    @Indexed
    val category: String,
)

package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MongoMovieRepositoryFacade(
    private val movieMongoRepository: MovieMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : MovieRepository, ExternalMovieIdProvider {
    override fun findById(id: MovieId) = movieMongoRepository.findById(id.value)
        .map { it.toDomain() }
        .orElse(null)

    override fun findBy(category: Category) =
        movieMongoRepository.findByCategory(category.value).map { it.toDomain() }

    override fun findAll() = movieMongoRepository.findAll().map { it.toDomain() }

    override fun save(movie: Movie) {
        val query = Query()
        query.addCriteria(Criteria.where("_id").`is`(movie.id.value))
        val update = Update()
        update.set("title", movie.details.title.value)
        update.set("description", movie.details.description.value)
        update.set("durationInSeconds", movie.details.duration.toSeconds())
        update.set("ratingImDb", movie.rating.imdb)
        update.set("userRating", movie.rating.userRating)
        update.set("category", movie.details.category.value)
        mongoTemplate.updateFirst(query, update, MovieDocument::class.java)
    }

    override fun provideFor(movieId: MovieId) = movieMongoRepository
        .findById(movieId.value)
        .map { it.imdbId }
        .orElse(null)
}

interface MovieMongoRepository : MongoRepository<MovieDocument, String> {
    fun findByCategory(value: String): List<MovieDocument>
}

@Document("movies")
data class MovieDocument(
    @Id
    val id: String,
    @Indexed
    val imdbId: String,
    val title: String,
    val description: String,
    val durationInSeconds: Long,
    val ratingImDb: String,
    val userRating: Double,
    @Indexed val category: String,
) {
    fun toDomain() = Movie(
        id = MovieId(id),
        details = Details(
            title = Title(title),
            description = Description(description),
            duration = Duration.ofSeconds(durationInSeconds),
            category = Category(category),
        ),
        rating = MovieRatings(ratingImDb, userRating),
    )
}

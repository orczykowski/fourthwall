package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.rating.RatingDetails
import com.fourthwall.moviedb.domain.movie.rating.RatingRepository
import com.fourthwall.moviedb.domain.movie.rating.UserRateValue
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class MongoRatingRepositoryFacade(private val repository: MongoRatingRepository) : RatingRepository {
    override fun store(rating: RatingDetails) {
        repository.save(
            RatingDocument(
                id = null,
                movieId = rating.movieId.value,
                rating = rating.rate.value,
                user = rating.userName,
                createdAt = rating.createdAt
            )
        )
    }

    override fun findAllByMovieId(movieId: MovieId) =
        repository.findByMovieId(movieId.value)
            .map {
                RatingDetails(
                    movieId = MovieId(it.movieId),
                    rate = UserRateValue(it.rating),
                    userName = it.user,
                    createdAt = it.createdAt
                )
            }
}

interface MongoRatingRepository : MongoRepository<RatingDocument, UUID> {
    fun findByMovieId(movieId: String): List<RatingDocument>
}

@Document("ratings")
data class RatingDocument(
    @Id
    val id: UUID?,
    @Indexed
    val movieId: String,
    val rating: Int,
    val user: String,
    val createdAt: Instant,
)
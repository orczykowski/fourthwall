package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.schedule.*
import com.fourthwall.moviedb.domain.schedule.Currency
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.Instant
import java.util.*

@Repository
class MongoScheduleRepositoryFacade(private val repository: MongoScheduleRepository) : ScheduleRepository {
    override fun findAll() = repository.findAll().map { it.asDomain() }

    override fun findByMovieId(movieId: MovieId) = repository.findById(movieId.value)
        .map { it.asDomain() }
        .orElse(null)

    override fun save(movie: MovieScheduleSnapshot) {
        repository.save(movie.toDocument())
    }

    private fun MovieScheduleSnapshot.toDocument() = ScheduleMongoDocument.from(this)
}

@Repository
interface MongoScheduleRepository : MongoRepository<ScheduleMongoDocument, String> {
    override fun findById(id: String): Optional<ScheduleMongoDocument>
}

@Document("schedules")
data class ScheduleMongoDocument(
    @Id val id: String,
    val schedules: Map<DayOfWeek, List<MovieProjectionDocument>>,
) {
    companion object {
        fun from(snapshot: MovieScheduleSnapshot) = ScheduleMongoDocument(
            id = snapshot.movieId,
            schedules = snapshot.schedules.mapValues { (_, value) ->
                value.map { MovieProjectionDocument(it.startAt, it.endAt, it.price.amount, it.price.currency) }
            })
    }

    fun asDomain() = MovieSchedule(movieId = MovieId(id), schedules = schedules.mapValues { (_, value) ->
        value.map { MovieProjection(it.startAt, it.endAt, Money(it.price, it.currency)) }
    })
}

data class MovieProjectionDocument(
    val startAt: Instant,
    val endAt: Instant,
    val price: BigDecimal,
    val currency: Currency,
)
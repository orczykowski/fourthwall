package com.fourthwall.moviedb.adapter.persistance

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.schedule.MovieSchedule
import com.fourthwall.moviedb.domain.schedule.MovieScheduleSnapshot
import com.fourthwall.moviedb.domain.schedule.ScheduleRepository
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.DayOfWeek

@Repository
class MongoScheduleRepositoryFacade(private val repository: MongoScheduleRepository) : ScheduleRepository {
    override fun findAll(): List<MovieSchedule> {
        repository.findAll()
        TODO()
    }

    override fun findByMovieId(movieId: MovieId): MovieSchedule? {
        TODO("Not yet implemented")
    }

    override fun findByDayOfWeek(dayOfWeek: DayOfWeek): List<MovieSchedule> {
        TODO("Not yet implemented")
    }

    override fun save(copy: MovieScheduleSnapshot) {
        TODO("Not yet implemented")
    }

}

@Repository
interface MongoScheduleRepository : MongoRepository<String, ScheduleMongoDocument> {

}

@Document(collection = "schedules")
data class ScheduleMongoDocument(
    @Id val id: String,
)
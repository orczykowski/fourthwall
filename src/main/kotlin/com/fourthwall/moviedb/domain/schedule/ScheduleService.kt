package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.Movie
import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.MovieNotFoundException
import com.fourthwall.moviedb.domain.movie.MovieRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val movieRepository: MovieRepository,
): ScheduleManager {

    override fun addProjection(command: AddProjectionCommand) = scheduleRepository.findByMovieId(command.movieId)
        ?.addSingleMovieProjection(command.dayOfWeek, command.startAt, getMovie(command.movieId), command.price)
        ?.let { scheduleRepository.save(it.toSnapshot()) }
        ?: throw MovieScheduleNotFoundException(command.movieId)


    override fun upsertSchedule(command: UpsertScheduleCommand) {
        val movie = getMovie(command.movieId)
        val schedule = scheduleRepository.findByMovieId(movie.id)
            ?.setScheduleForDay(command.dayOfWeek, command.starts.asMovieTimes(movie))
            ?: MovieSchedule(movie.id, mapOf(command.dayOfWeek to command.starts.asMovieTimes(movie)))
        scheduleRepository.save(schedule.toSnapshot())
    }

    override fun removeSchedule(command: RemoveScheduleCommand) {
        val schedule = scheduleRepository.findByMovieId(command.movieId)
            ?.let {
                if (command.dayOfWeek != null) {
                    it.removeSchedule(command.dayOfWeek)
                } else {
                    it.removeAllSchedules()
                }
            } ?: throw MovieScheduleNotFoundException(command.movieId)
        scheduleRepository.save(schedule.toSnapshot())
    }

    private fun getMovie(movieId: MovieId): Movie {
        return movieRepository.findById(movieId) ?: throw MovieNotFoundException(movieId)
    }

    private fun Map<Instant, Money>.asMovieTimes(movie: Movie) =
        this.map { (startAt, price) -> MovieProjection(startAt, startAt.plusSeconds(movie.durationInSeconds()), price) }

}



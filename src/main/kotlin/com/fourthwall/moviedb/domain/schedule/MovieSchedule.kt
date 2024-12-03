package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.Movie
import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.DayOfWeek
import java.time.Instant

data class MovieSchedule(val movieId: MovieId, val schedules: Map<DayOfWeek, List<MovieProjection>>) {
    companion object {
        fun empty(movieId: MovieId) = MovieSchedule(movieId, emptyMap())
    }

    fun removeSchedule(dayOfWeek: DayOfWeek) = MovieSchedule(movieId, schedules - dayOfWeek)

    fun removeAllSchedules() = MovieSchedule(movieId, mapOf())

    fun toSnapshot() = MovieScheduleSnapshot(movieId.value, schedules)

    fun setScheduleForDay(dayOfWeek: DayOfWeek, movieProjections: List<MovieProjection>): MovieSchedule {
        validate(movieProjections)
        return MovieSchedule(movieId, schedules + (dayOfWeek to movieProjections))
    }

    fun addSingleMovieProjection(dayOfWeek: DayOfWeek, startAt: Instant, movie: Movie, price: Money): MovieSchedule {
        val movieProjection = MovieProjection(startAt, startAt.plusSeconds(movie.durationInSeconds()), price)
        val dailySchedule =
            schedules[dayOfWeek] ?: return MovieSchedule(movieId, schedules + (dayOfWeek to listOf(movieProjection)))
        isValidMovieTimeInSchedule(dailySchedule, movieProjection)
        return MovieSchedule(movieId, schedules + (dayOfWeek to (dailySchedule + movieProjection)))
    }

    private fun validate(movieProjections: List<MovieProjection>) {
        movieProjections.forEach { isValidMovieTimeInSchedule(movieProjections, it) }
    }

    private fun isValidMovieTimeInSchedule(dailySchedule: List<MovieProjection>, movieProjection: MovieProjection) {
        if (dailySchedule.any { it.overlaps(movieProjection) }) {
            throw MovieTimeOverlapsException()
        }
    }
}

data class MovieScheduleSnapshot(val movieId: String, val schedules: Map<DayOfWeek, List<MovieProjection>>)

data class MovieProjection(val startAt: Instant, val endAt: Instant, val price: Money) {
    init {
        if (startAt == endAt || endAt.isBefore(startAt)) {
            throw InvalidMovieTimeException("Invalid time range")
        }
    }

    fun overlaps(time: MovieProjection): Boolean {
        return time.startAt.isBefore(endAt) && time.endAt.isAfter(startAt)
    }
}
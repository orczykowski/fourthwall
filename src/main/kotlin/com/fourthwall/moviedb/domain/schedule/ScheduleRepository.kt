package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.DayOfWeek

interface ScheduleRepository {
    fun findAll(): List<MovieSchedule>
    fun findByMovieId(movieId: MovieId): MovieSchedule?
    fun findByDayOfWeek(dayOfWeek: DayOfWeek): List<MovieSchedule>
    fun save(copy: MovieScheduleSnapshot)

}
package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.DayOfWeek

interface ScheduleProvider {
    fun findSchedules(query: FindScheduleQuery): MovieSchedulesView
}

data class FindScheduleQuery(val movieId: MovieId?, val dayOfWeek: DayOfWeek? = null)

data class MovieSchedulesView(val moviesSchedules: List<MovieScheduleSnapshot>)
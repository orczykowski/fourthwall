package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.MovieId

interface ScheduleProvider {
    fun findSchedules(query: FindScheduleQuery): MovieSchedulesView
}

data class FindScheduleQuery(val movieId: MovieId?)

data class MovieSchedulesView(val moviesSchedules: List<MovieScheduleSnapshot>)
package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.MovieId
import java.time.DayOfWeek
import java.time.Instant

interface ScheduleManager {
    fun upsertSchedule(command: UpsertScheduleCommand)
    fun removeSchedule(command: RemoveScheduleCommand)
    fun addProjection(command: AddProjectionCommand)
    fun updateProjection(command: UpdatePriceCommand)
}

data class RemoveScheduleCommand(val movieId: MovieId, val dayOfWeek: DayOfWeek?)

data class AddProjectionCommand(val movieId: MovieId, val dayOfWeek: DayOfWeek, val startAt: Instant, val price: Money)
data class UpdatePriceCommand(val movieId: MovieId, val dayOfWeek: DayOfWeek, val startAt: Instant, val price: Money)

data class UpsertScheduleCommand(val movieId: MovieId, val dayOfWeek: DayOfWeek, val starts: Map<Instant, Money>)

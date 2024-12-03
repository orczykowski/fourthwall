package com.fourthwall.moviedb.domain.schedule

import org.springframework.stereotype.Service

@Service
class ScheduleSearchService(private val scheduleRepository: ScheduleRepository) : ScheduleProvider {
    override fun findSchedules(query: FindScheduleQuery): MovieSchedulesView {
        when {
            query.movieId != null -> {
                return scheduleRepository.findByMovieId(query.movieId)
                    ?.let { MovieSchedulesView(listOf(it.toSnapshot())) }
                    ?: throw MovieScheduleNotFoundException(query.movieId)
            }

            else -> {
                val schedules = scheduleRepository.findAll()
                return MovieSchedulesView(schedules.map { it.toSnapshot() })
            }
        }
    }

}
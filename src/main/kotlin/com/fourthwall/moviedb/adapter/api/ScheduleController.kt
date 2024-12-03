package com.fourthwall.moviedb.adapter.api

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.schedule.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.Instant

@RestController
@RequestMapping(
    headers = ["X-Api-Version=${ApiVersion.V1}", "Content-Type=application/json"]
)
class ScheduleController(
    private val scheduleInfoProvider: ScheduleProvider,
    private val scheduleManager: ScheduleManager,
) {
    @GetMapping("/api/public/movies/{movieId}/schedules")
    fun findSchedules(@PathVariable("movieId") movieId: String): DailyMoviesSchedulesResponse {
        scheduleInfoProvider.findSchedules(FindScheduleQuery(MovieId(movieId)))
            .let { return DailyMoviesSchedulesResponse.from(it) }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/internal/movies/{movieId}/schedules/{dayOfWeek}")
    fun updateSchedule(
        @PathVariable("movieId") movieId: String,
        @PathVariable("dayOfWeek") dayOfWeek: DayOfWeek,
        @RequestBody scheduleJsonDto: AddProjectionRequest,
    ) {
        val command = UpsertScheduleCommand(
            MovieId(movieId),
            dayOfWeek,
            mapOf(scheduleJsonDto.startAt to Money(scheduleJsonDto.price, scheduleJsonDto.currency))
        )
        scheduleManager.upsertSchedule(command)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/internal/movies/{movieId}/schedules/{dayOfWeek}")
    fun addMovieProjectionToSchedule(
        @PathVariable("movieId") movieId: String,
        @PathVariable("dayOfWeek") dayOfWeek: DayOfWeek,
        @RequestBody scheduleJsonDto: AddProjectionRequest,
    ) {
        val command = AddProjectionCommand(
            MovieId(movieId),
            dayOfWeek,
            scheduleJsonDto.startAt,
            Money(scheduleJsonDto.price, scheduleJsonDto.currency)
        )
        scheduleManager.addProjection(command)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/api/internal/movies/{movieId}/schedules/{dayOfWeek}")
    fun updateMovieProjection(
        @PathVariable("movieId") movieId: String,
        @PathVariable("dayOfWeek") dayOfWeek: DayOfWeek,
        @RequestBody scheduleJsonDto: AddProjectionRequest,
    ) {
        val command = UpdatePriceCommand(
            MovieId(movieId),
            dayOfWeek,
            scheduleJsonDto.startAt,
            Money(scheduleJsonDto.price, scheduleJsonDto.currency)
        )
        scheduleManager.updateProjection(command)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/internal/movies/{movieId}/schedules/{dayOfWeek}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeAllSchedule(
        @PathVariable("movieId") movieId: String,
        @PathVariable("dayOfWeek") dayOfWeek: DayOfWeek,
    ) = scheduleManager.removeSchedule(RemoveScheduleCommand(MovieId(movieId), dayOfWeek))


    data class DailyMoviesSchedulesResponse(
        val moviesSchedules: List<MovieScheduleDto>,
    ) {
        companion object {
            fun from(view: MovieSchedulesView) = DailyMoviesSchedulesResponse(view.moviesSchedules.map {
                MovieScheduleDto(it.movieId, it.schedules.mapValues { (_, schedules) ->
                    schedules.map { schedule ->
                        MovieProjectionDto(
                            schedule.startAt,
                            schedule.endAt,
                            schedule.price.amount,
                            schedule.price.currency
                        )
                    }
                })
            })
        }
    }

    data class MovieScheduleDto(
        val movieId: String,
        val schedules: Map<DayOfWeek, List<MovieProjectionDto>>,
    )

    data class MovieProjectionDto(
        val startAt: Instant,
        val endAt: Instant,
        val price: BigDecimal,
        val currency: Currency,
    )

    data class AddProjectionRequest(
        val startAt: Instant,
        val price: BigDecimal,
        val currency: Currency,
    )

    data class DailyScheduleRequest(
        val starts: Map<Instant, BigDecimal>,
        val currency: Currency,
    )
}
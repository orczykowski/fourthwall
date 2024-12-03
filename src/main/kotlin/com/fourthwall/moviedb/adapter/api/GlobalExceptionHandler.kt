package com.fourthwall.moviedb.adapter.api

import com.fourthwall.moviedb.domain.movie.MovieNotFoundException
import com.fourthwall.moviedb.domain.movie.rating.InvalidRateOperationException
import com.fourthwall.moviedb.domain.movie.rating.InvalidRatingException
import com.fourthwall.moviedb.domain.movie.rating.InvalidUserException
import com.fourthwall.moviedb.domain.schedule.InvalidMovieTimeException
import com.fourthwall.moviedb.domain.schedule.MovieTimeOverlapsException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleMovieNotFoundException(ex: Exception) = ErrorResponse(ex.message ?: "Movie not found")

    @ExceptionHandler(
        value = [InvalidRateOperationException::class,
            InvalidRatingException::class,
            InvalidUserException::class,
            InvalidMovieTimeException::class]
    )
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleInvalidData(ex: Exception) = ErrorResponse(ex.message ?: "Unprocessable entity")

    @ExceptionHandler(
        value = [MovieTimeOverlapsException::class]
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(ex: Exception) = ErrorResponse(ex.message ?: "Conflict")
}

data class ErrorResponse(val message: String)
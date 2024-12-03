package com.fourthwall.moviedb.domain.schedule

import com.fourthwall.moviedb.domain.movie.MovieId

class MovieScheduleNotFoundException(movieId: MovieId) : RuntimeException("Schedule for movie $movieId not found")

class InvalidMovieTimeException(val str: String) : RuntimeException(str)
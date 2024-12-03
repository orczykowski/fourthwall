package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.MovieId

class MovieARatingNotFoundException(id: MovieId) : RuntimeException("Movie $id has no rating")
class InvalidRateOperationException(id: MovieId) : RuntimeException("Cannot rate non existing movie $id")
class InvalidRatingException : RuntimeException("Rate value must be between 1 and 5")
class InvalidUserException : RuntimeException("Reviewer name cannot be blank.")
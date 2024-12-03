package com.fourthwall.moviedb.domain.movie

class MovieNotFoundException(id: MovieId) : RuntimeException("Movie with id $id not found")
package com.fourthwall.moviedb.domain.movie

import java.time.Duration
import kotlin.time.toKotlinDuration


data class Movie(
    val id: MovieId,
    val details: Details,
    val rating: MovieRatings,
) {
    fun toSnapshot() = MovieSnapshot(
        id = id.value,
        title = details.title.value,
        description = details.description.value,
        duration = details.duration,
        ratingImDb = rating.imdb,
        userRating = rating.userRating,
        category = details.category.value,
    )

    fun updateRating(userRating: Double) = Movie(
        id = id,
        details = details,
        rating = rating.copy(userRating = userRating)
    )

    fun durationInSeconds() = details.duration.toKotlinDuration().inWholeSeconds
}

data class MovieSnapshot(
    val id: String,
    val title: String,
    val description: String,
    val duration: Duration,
    val ratingImDb: String,
    val userRating: Double,
    val category: String,
)

data class Details(
    val title: Title,
    val description: Description,
    val duration: Duration,
    val category: Category,
)

data class Title(val value: String)
data class Description(val value: String)
data class Category(val value: String)
data class MovieRatings(val imdb: String, val userRating: Double)

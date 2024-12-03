package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.rating.ExternalRating
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

    fun updateUserRating(userRating: Double) = Movie(
        id = id,
        details = details,
        rating = rating.copy(userRating = userRating)
    )

    fun durationInSeconds() = details.duration.toKotlinDuration().inWholeSeconds
    fun updateImdb(externalRating: ExternalRating) = Movie(
        id = id,
        details = details,
        rating = rating.copy(imdb = externalRating.value)
    )

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

data class Title(val value: String) {
    init {
        require(value.isNotBlank()) { "Title cannot be blank" }
    }
}

data class Description(val value: String) {
    init {
        require(value.isNotBlank()) { "Description cannot be blank" }
    }
}

data class Category(val value: String) {
    init {
        require(value.isNotBlank()) { "Category cannot be blank" }
    }
}

data class MovieRatings(val imdb: String, val userRating: Double) {
    init {
        require(imdb.isNotBlank()) { "Imdb rating cannot be blank" }
        require(userRating in 0.0..5.0) { "User rating must be between 0 and 5" }
    }
}

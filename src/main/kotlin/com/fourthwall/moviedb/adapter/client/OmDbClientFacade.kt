package com.fourthwall.moviedb.adapter.client

import com.fourthwall.moviedb.adapter.persistance.ExternalMovieIdProvider
import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.rating.ExternalRating
import com.fourthwall.moviedb.domain.movie.rating.ExternalRatingProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OmDbClientFacade(
    private val omClient: OmDbFeignClient,
    @Value("\${adapter.client.omdb.api-key}") private val apikey: String,
    private val externalMovieIdProvider: ExternalMovieIdProvider,
) : ExternalRatingProvider {

    override fun findBy(movieId: MovieId): ExternalRating? {
        val externalMovieId = externalMovieIdProvider.provideFor(movieId) ?: return null
        return omClient.findBy(apikey, externalMovieId)
            .let { response ->
                response?.imdbRating.toExternalRating()
            }
    }

    private fun String?.toExternalRating() = this?.let { ExternalRating(it) } ?: ExternalRating("N/A")
}
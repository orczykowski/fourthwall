package com.fourthwall.moviedb.adapter.client

import com.fourthwall.moviedb.adapter.properties.MovieCatalog
import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.rating.ExternalRating
import com.fourthwall.moviedb.domain.movie.rating.ExternalRatingProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OmDbClientFacade(
    private val omClient: OmDbFeignClient,
    private val movieCatalog: MovieCatalog,
    @Value("\${adapter.client.omdb.api-key}") private val apikey: String,
) : ExternalRatingProvider {

    override fun findBy(movieId: MovieId): ExternalRating? {
        val externalMovieId = movieCatalog.getImoDbMovieId(movieId) ?: return null
        return omClient.findBy(apikey, externalMovieId)
            .let { response ->
                response?.imdbID.toExternalRating()
            }
    }

    private fun String?.toExternalRating() = this?.let { ExternalRating(it) } ?: ExternalRating("N/A")
}
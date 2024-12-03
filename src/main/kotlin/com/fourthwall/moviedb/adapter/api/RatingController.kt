package com.fourthwall.moviedb.adapter.api

import com.fourthwall.moviedb.domain.movie.MovieId
import com.fourthwall.moviedb.domain.movie.rating.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    path = ["/api/public/movies/{id}/ratings"], headers = ["X-Api-Version=${ApiVersion.V1}", "Accept=application/json"]
)
class RatingController(
    private val evaluator: MovieEvaluator,
    private val ratingsProvider: MovieRatingDetailsProvider,
) {

    @GetMapping
    fun fetchMovieRatings(@PathVariable("id") id: String) =
        ratingsProvider.findMovieRatings(RatingQuery(MovieId(id)))?.let { ratings ->
            RatingsDto(imdb = ratings.imdb,
                userRating = ratings.userRating,
                userRatings = ratings.userRatings.map { UserRatingsDto(it.user, it.rating) })
        } ?: throw MovieARatingNotFoundException(MovieId(id))


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun rateMovie(@PathVariable("id") id: String, @RequestBody body: RateMovieRequest) =
        evaluator.rateMovie(RateMovieCommand(MovieId(id), UserRateValue(body.rate), body.userName))

    data class RatingsDto(
        val imdb: String,
        val userRating: Double,
        val userRatings: List<UserRatingsDto>,
    )

    data class UserRatingsDto(val userName: String, val rate: Int)

    data class RateMovieRequest(val userName: String, val rate: Int)

}
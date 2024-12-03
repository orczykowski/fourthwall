package com.fourthwall.moviedb.adapter.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "omdbClient", url = "\${adapter.client.omdb.url}") //confguraiton + fallback na null
interface OmDbFeignClient {

    @GetMapping
    fun findBy(@RequestParam("apikey") apiKey: String, @RequestParam("i") id: String): OmDbResponse?

}

data class OmDbResponse(
    @JsonProperty("Title") val title: String,
    @JsonProperty("Runtime") val runtime: String,
    @JsonProperty("Genre") val genre: String,
    @JsonProperty("imdbRating") val imdbRating: String,
    @JsonProperty("imdbID") val imdbID: String,
    @JsonProperty("Plot") val plot: String,
)

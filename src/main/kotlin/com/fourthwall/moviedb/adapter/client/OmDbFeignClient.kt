package com.fourthwall.moviedb.adapter.client

import com.fasterxml.jackson.annotation.JsonProperty
import feign.Retryer
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "omdbClient", url = "\${adapter.client.omdb.url}", configuration = [FeignClientConfig::class]
)
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

@Configuration
class FeignClientConfig {
    //todo it should be more sophisticated like circuit breaker, fallback etc
    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(1000, 2000, 3)
    }
}

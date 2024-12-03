package com.fourthwall.moviedb.adapter.api

import com.fourthwall.moviedb.domain.movie.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Duration

@WebMvcTest(MovieController::class)
class MovieControllerTest {

    private val movie1 = Movie(
        MovieId("1"),
        Details(
            Title("Inception"),
            Description("A mind-bending thriller"),
            Duration.ofHours(2),
            Category("Horror")
        ),
        MovieRatings("4.5", 1.2)
    ).toSnapshot()

    private val movie2 = Movie(
        MovieId("2"),
        Details(
            Title("Sth"),
            Description("Lorem ipsum"),
            Duration.ofHours(7),
            Category("Comedy")
        ),
        MovieRatings("4.5", 1.2)
    ).toSnapshot()

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var movieProvider: MovieProvider

    @MockkBean
    lateinit var mongoTemplate: MongoTemplate

    //@Test
    fun `should fetch list of movies`() {
        // given
        every { movieProvider.findMovies(any()) } returns (listOf(movie1, movie2))

        // when & then
        mockMvc.perform(get("/movies"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].details.title.value").value("Inception"))
            .andExpect(jsonPath("$[0].details.category.value").value("Horror"))
            .andExpect(jsonPath("$[1].details.title.value").value("Sth"))
            .andExpect(jsonPath("$[1].details.category.value").value("Comedy"))
    }


}
package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.rating.ExternalRating
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals

class MovieTest {

    val id = MovieId("123Id")
    val title = Title("Test movie")
    val description = Description("Test description")
    val duration = Duration.ofMinutes(120)
    val category = Category("Test category")
    val imdb = "8/10"
    val userRating = 4.5

    lateinit var subject: Movie

    @BeforeEach
    fun init() {
        subject = Movie(
            id, Details(title, description, duration, category), MovieRatings(imdb, userRating)
        )
    }

    @Test
    fun `should create snapshot`() {
        //when:
        val snapshot = subject.toSnapshot()

        //then
        assertEquals(id.value, snapshot.id)
        assertEquals(title.value, snapshot.title)
        assertEquals(description.value, snapshot.description)
        assertEquals(duration, snapshot.duration)
        assertEquals(imdb, snapshot.ratingImDb)
        assertEquals(userRating, snapshot.userRating)
        assertEquals(category.value, snapshot.category)
    }

    @Test
    fun `should update user rating`() {
        //given
        val newUserRating = 5.0
        //when
        val movie = subject.updateUserRating(newUserRating)

        //then
        assertEquals(newUserRating, movie.rating.userRating)
    }

    @Test
    fun `should update imdb rating`() {
        //given
        val newImdbRating = ExternalRating("213123")
        //when
        val movie = subject.updateImdb(newImdbRating)

        //then
        assertEquals(newImdbRating.value, movie.rating.imdb)
    }


}
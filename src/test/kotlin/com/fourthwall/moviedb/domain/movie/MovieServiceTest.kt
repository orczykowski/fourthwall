package com.fourthwall.moviedb.domain.movie

import com.fourthwall.moviedb.domain.movie.rating.ExternalRating
import com.fourthwall.moviedb.domain.movie.rating.MovieImdbRatingUpdated
import com.fourthwall.moviedb.domain.movie.rating.MovieUserRatingUpdated
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals

class MovieServiceTest {
    val movieId = MovieId("1")
    val imdb = "213/33333"
    val userRating = 1.0

    lateinit var subject: MovieService

    lateinit var movieRepository: MovieRepository
    lateinit var movie: Movie

    @BeforeEach
    fun init() {
        movie = Movie(
            movieId,
            Details(
                Title("title"),
                Description("das"), Duration.ofDays(1), Category("abc")
            ),
            MovieRatings(imdb, userRating)
        )
        movieRepository = Mockito.mock(MovieRepository::class.java)
        subject = MovieService(movieRepository)
    }

    @Test
    fun `should find all movies`() {
        val criteria = MoviesQuery()
        Mockito.`when`(movieRepository.findAll()).thenReturn(listOf(movie))

        // When
        val result = subject.findMovies(criteria)

        // Then
        assert(result.size == 1)
        assert(result[0].id == movie.id.value)
    }

    @Test
    fun `should throw exception when try get non existing movie`() {
        //given
        val id = MovieId("2")
        Mockito.`when`(movieRepository.findById(id)).thenReturn(null)

        // expect
        assertThrows<MovieNotFoundException> {
            subject.getMovieById(id)
        }
    }

    @Test
    fun `should update only imdb rating`() {
        //given
        val rate = ExternalRating("666")
        val event = MovieImdbRatingUpdated(movieId, rate, Instant.now())
        Mockito.`when`(movieRepository.findById(movieId)).thenReturn(movie)

        // When
        subject.onUpdateUserMovieRating(event)

        //then
        argumentCaptor<Movie>().apply {
            Mockito.verify(movieRepository).save(capture())
            assertEquals(firstValue.rating.imdb, rate.value)
        }
    }

    @Test
    fun `should update only user rating`() {
        //given
        val event = MovieUserRatingUpdated(movieId, 1.00, Instant.now())
        Mockito.`when`(movieRepository.findById(movieId)).thenReturn(movie)

        // When
        subject.onUpdateUserMovieRating(event)

        //then
        argumentCaptor<Movie>().apply {
            Mockito.verify(movieRepository).save(capture())
            assertEquals(firstValue.rating.userRating, 1.00)
        }
    }

    @Test
    fun `should not update anything when movie does not exists`() {
        //given
        val event = MovieUserRatingUpdated(movieId, 12.00, Instant.now())
        Mockito.`when`(movieRepository.findById(movieId)).thenReturn(null)

        // When
        assertThrows<MovieNotFoundException> {
            subject.onUpdateUserMovieRating(event)
        }
    }
}
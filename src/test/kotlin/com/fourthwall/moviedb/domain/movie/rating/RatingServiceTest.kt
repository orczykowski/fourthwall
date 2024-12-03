package com.fourthwall.moviedb.domain.movie.rating

import com.fourthwall.moviedb.domain.movie.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.springframework.context.ApplicationEventPublisher
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import kotlin.test.assertEquals

class RatingServiceTest {

    lateinit var movieRepository: MovieRepository
    lateinit var ratingsRepository: RatingRepository
    lateinit var externalRatingProvider: ExternalRatingProvider
    lateinit var applicationEventPublisher: ApplicationEventPublisher
    var clock: Clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())


    lateinit var subject: RatingService

    @BeforeEach
    fun init() {
        movieRepository = Mockito.mock(MovieRepository::class.java)
        ratingsRepository = Mockito.mock(RatingRepository::class.java)
        externalRatingProvider = Mockito.mock(ExternalRatingProvider::class.java)
        applicationEventPublisher = Mockito.mock(ApplicationEventPublisher::class.java)

        subject = RatingService(
            movieRepository,
            ratingsRepository,
            externalRatingProvider,
            applicationEventPublisher,
            clock
        )
    }

    @Test
    fun `should fetch ratings from external sources for all movies`() {
        // given
        val movie1 = MovieId("1")
        val movie2 = MovieId("2")
        val movies = listOf(movie1, movie2)

        Mockito.`when`(movieRepository.findAll()).thenReturn(movies.map { it.toMovie() })

        Mockito.`when`(externalRatingProvider.findBy(movie1)).thenReturn(ExternalRating("1/2"))
        Mockito.`when`(externalRatingProvider.findBy(movie2)).thenReturn(ExternalRating("5/4"))

        // when
        subject.updateImdb()

        // then
        val eventCaptor = argumentCaptor<MovieRatingUpdated>()

        Mockito.verify(applicationEventPublisher, Mockito.times(2))
            .publishEvent(eventCaptor.capture())

        val events = eventCaptor.allValues
        assertEquals(2, events.size)

        assertEquals(movie1, events[0].movieId)
        assertEquals(ExternalRating("1/2"), (events[0] as MovieImdbRatingUpdated).rate)


        assertEquals(movie2, events[1].movieId)
        assertEquals(ExternalRating("5/4"), (events[1] as MovieImdbRatingUpdated).rate)

    }

    @Test
    fun `should update only changed rating`() {
        val movie1 = MovieId("1")
        Mockito.`when`(movieRepository.findAll()).thenReturn(listOf(movie1.toMovie()))
        Mockito.`when`(externalRatingProvider.findBy(movie1)).thenReturn(ExternalRating("1/7"))
        // when
        subject.updateImdb()

        //then
        Mockito.verify(applicationEventPublisher, Mockito.times(0))
            .publishEvent(any())
    }

    @Test
    fun `should calculate new user rating`() {
        // given
        val movieId = MovieId("1")
        Mockito.`when`(movieRepository.findById(movieId)).thenReturn(movieId.toMovie())
        val command = RateMovieCommand(movieId, UserRateValue(2), "user1")

        Mockito.`when`(ratingsRepository.findAllByMovieId(movieId)).thenReturn(
            listOf(
                RatingDetails(movieId, UserRateValue.of(2), "user1", Instant.now()),
                RatingDetails(movieId, UserRateValue.of(3), "user2", Instant.now())
            )
        )

        // when
        subject.rateMovie(command)

        // then
        val eventCaptor = argumentCaptor<MovieRatingUpdated>()

        Mockito.verify(applicationEventPublisher)
            .publishEvent(eventCaptor.capture())

        val event = eventCaptor.firstValue as MovieUserRatingUpdated
        assertEquals(movieId, event.movieId)
        assertEquals(2.5, event.rate)
    }


    @Test
    fun `should throw exception when try to rat non existing movie`() {
        // given
        val movieId = MovieId("1")
        Mockito.`when`(movieRepository.findById(movieId)).thenReturn(null)
        val command = RateMovieCommand(movieId, UserRateValue(2), "user1")

        // then
        assertThrows<InvalidRateOperationException> { subject.rateMovie(command) }
    }

    private fun MovieId.toMovie() = Movie(
        this,
        Details(
            Title("123"),
            Description("des"), Duration.ofDays(1), Category("abc")
        ), MovieRatings("1/7", 2.0)
    )
}


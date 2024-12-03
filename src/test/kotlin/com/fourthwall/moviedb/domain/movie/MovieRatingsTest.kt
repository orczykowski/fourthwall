package com.fourthwall.moviedb.domain.movie

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class MovieRatingsTest {
    @ParameterizedTest
    @ValueSource(strings = ["", " ", "  "])
    fun `should not create from invalid imdb str value`(str: String) {
        assertThrows<IllegalArgumentException> { MovieRatings(str, 0.0) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-0.1, -1.0, 5.01])
    fun `should not create from invalid user value`(rating: Double) {
        assertThrows<IllegalArgumentException> { MovieRatings("12/0", rating) }
    }
}
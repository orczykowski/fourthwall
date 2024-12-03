package com.fourthwall.moviedb.domain.movie.rating

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserRateValueTest {
    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5])
    fun `should create user rate value`(rate: Int) {
        val userRateValue = UserRateValue.of(rate)
        assertEquals(rate, userRateValue.value)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 6])
    fun `should not allow to create user rate with value`(rate: Int) {
        assertThrows<InvalidRatingException> {
            UserRateValue.of(rate)
        }
    }
}
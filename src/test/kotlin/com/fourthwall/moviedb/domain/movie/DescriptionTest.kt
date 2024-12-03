package com.fourthwall.moviedb.domain.movie

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DescriptionTest {
    @ParameterizedTest
    @ValueSource(strings = ["", " ", "  "])
    fun `should not create from invalid str value`(str: String) {
        assertThrows<IllegalArgumentException> { Description(str) }
    }
}
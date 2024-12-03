package com.fourthwall.moviedb.domain.schedule

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun `should create money`() {
        val money = Money(BigDecimal("123.12"), Currency.PLN)
        assertEquals("123.12", money.amount.toString())
        assertEquals(Currency.PLN, money.currency)
    }

    @ParameterizedTest
    @ValueSource(strings = ["USD", "EUR", "PLN"])
    fun `should not allow to create money with negative amount`(currency: Currency) {
        val exception = assertThrows<IllegalArgumentException> {
            Money(BigDecimal("-123.12"), currency)
        }
        assertEquals("Amount cannot be negative", exception.message)
    }
}
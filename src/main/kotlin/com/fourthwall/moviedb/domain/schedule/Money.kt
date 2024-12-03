package com.fourthwall.moviedb.domain.schedule


import java.math.BigDecimal


data class Money(val amount: BigDecimal, val currency: Currency = Currency.PLN) {
    init {
        if (amount < BigDecimal.ZERO) {
            throw IllegalArgumentException("Amount cannot be negative")
        }
    }
}


enum class Currency {
    USD,
    EUR,
    PLN
}
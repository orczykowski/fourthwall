package com.fourthwall.moviedb.domain.movie.rating


data class UserRateValue(val value: Int) {
    companion object {
        fun of(rate: Int): UserRateValue {
            if (rate < 1 || rate > 5) {
                throw InvalidRatingException()
            }
            return UserRateValue(rate)
        }
    }
}

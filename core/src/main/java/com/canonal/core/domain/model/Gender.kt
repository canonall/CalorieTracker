package com.canonal.core.domain.model

sealed class Gender(val value: String) {
    object Male: Gender("male")
    object Female: Gender("female")

    companion object {
        fun fromString(value: String): Gender {
            return when(value) {
                "male" -> Male
                "female" -> Female
                else -> Female
            }
        }
    }
}

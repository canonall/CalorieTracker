package com.canonal.core.domain.model

sealed class GoalType(val value: String) {
    object LoseWeight: GoalType("lose_weight")
    object KeepWeight: GoalType("keep_weight")
    object GainWeight: GoalType("gain_weight")

    companion object {
        fun fromString(value: String): GoalType {
            return when(value) {
                "lose_weight" -> LoseWeight
                "keep_weight" -> KeepWeight
                "gain_weight" -> GainWeight
                else -> KeepWeight
            }
        }
    }
}

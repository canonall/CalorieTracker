package com.canonal.onboarding_presentation.weight.scale

sealed class LineType {
    object Normal: LineType()
    object FiveStep: LineType()
    object TenStep: LineType()
}

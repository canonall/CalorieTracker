package com.canonal.onboarding_presentation.weight.scale

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canonal.core_ui.BrightGreen

data class ScaleStyle(
    val scaleWidth: Dp = 150.dp,
    val radius: Dp = 550.dp,
    val normalLineColor: Color = Color.LightGray,
    val fiveStepLineColor: Color = BrightGreen,
    val tenStepLineColor: Color = Color.Black,
    val normalLineLength: Dp = 15.dp,
    val fiveStepLineLength: Dp = 25.dp,
    val tenStepLineLength: Dp = 35.dp,
    val scaleIndicatorColor: Color = BrightGreen,
    val scaleIndicatorLength: Dp = 60.dp,
    val textSize: TextUnit = 18.sp
)

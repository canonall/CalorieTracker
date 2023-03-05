package com.canonal.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.canonal.core_ui.CarbColor
import com.canonal.core_ui.FatColor
import com.canonal.core_ui.ProteinColor

@Composable
fun NutrientsBar(
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    calorieGoal: Int,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colors.background
    val caloriesExceedColor = MaterialTheme.colors.error
    val barCornerRadius = CornerRadius(100f)

    val carbsWidthRatio = remember { Animatable(initialValue = 0f) }
    val proteinWidthRatio = remember { Animatable(initialValue = 0f) }
    val fatWidthRatio = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = carbs) {
        carbsWidthRatio.animateTo(targetValue = ((carbs * 4f) / calorieGoal))
    }
    LaunchedEffect(key1 = protein) {
        proteinWidthRatio.animateTo(targetValue = ((carbs * 4f) / calorieGoal))
    }
    LaunchedEffect(key1 = fat) {
        fatWidthRatio.animateTo(targetValue = ((carbs * 9f) / calorieGoal))
    }

    Canvas(modifier = modifier) {
        if (calories <= calorieGoal) {

            var carbsWidth = carbsWidthRatio.value * size.width
            var proteinWidth = proteinWidthRatio.value * size.width
            var fatWidth = fatWidthRatio.value * size.width
            val widthSum = carbsWidth + proteinWidth + fatWidth

            if (widthSum > size.width) {
                // scale bar widths
                val resizeFactor = (size.width / widthSum)
                carbsWidth *= resizeFactor
                proteinWidth *= resizeFactor
                fatWidth *= resizeFactor
            }

            // empty bar
            drawRoundRect(
                color = background,
                size = size,
                cornerRadius = barCornerRadius
            )
            // fat bar
            drawRoundRect(
                color = FatColor,
                size = Size(width = carbsWidth + proteinWidth + fatWidth, height = size.height),
                cornerRadius = barCornerRadius
            )
            // protein bar
            drawRoundRect(
                color = ProteinColor,
                size = Size(width = carbsWidth + proteinWidth, height = size.height),
                cornerRadius = barCornerRadius
            )
            // carbs bar
            drawRoundRect(
                color = CarbColor,
                size = Size(width = carbsWidth, height = size.height),
                cornerRadius = barCornerRadius
            )
        } else {
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = barCornerRadius
            )
        }
    }
}

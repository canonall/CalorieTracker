package com.canonal.onboarding_presentation.weight.scale

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import kotlin.math.*

const val MIN_WEIGHT = 20
const val MAX_WEIGHT = 250

@Composable
fun Scale(
    initialWeight: Int,
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    minWeight: Int = MIN_WEIGHT,
    maxWeight: Int = MAX_WEIGHT,
    onWeightChange: (Int) -> Unit
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth

    // center of canvas
    var center by remember { mutableStateOf(Offset.Zero) }

    // center of big circle that we see only the upper part
    var circleCenter by remember { mutableStateOf(Offset.Zero) }

    // angle we currently rotated the scale
    // stores how many degrees we dragged
    var angle by remember { mutableStateOf(0f) }

    var dragStartedAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }

    Canvas(
        modifier = modifier
            .semantics {
                contentDescription = "scale"
            }
            .pointerInput(key1 = true) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        dragStartedAngle = -atan2(
                            y = circleCenter.x - startOffset.x,
                            x = circleCenter.y - startOffset.y
                        ) * (180f / PI.toFloat())
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->
                    val touchAngle = -atan2(
                        y = circleCenter.x - change.position.x,
                        x = circleCenter.y - change.position.y
                    ) * (180f / PI.toFloat())
                    val newAngle = oldAngle + (touchAngle - dragStartedAngle)
                    angle = newAngle.coerceIn(
                        minimumValue = (initialWeight - maxWeight).toFloat(),
                        maximumValue = (initialWeight - minWeight).toFloat()
                    )
                    onWeightChange((initialWeight - angle).roundToInt())
                }
            }
    ) {
        center = this.center
        circleCenter = Offset(
            x = center.x,
            y = (scaleWidth.toPx() / 2) + radius.toPx()
        )
        val outerRadius = radius.toPx() + (scaleWidth.toPx() / 2f)
        val innerRadius = radius.toPx() - (scaleWidth.toPx() / 2f)
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }
        // draw lines
        for (currentWeight in minWeight..maxWeight) {
            val angleInRad = (currentWeight - initialWeight + angle - 90) * (PI / 180f).toFloat()
            val lineType = when {
                currentWeight % 10 == 0 -> LineType.TenStep
                currentWeight % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }
            val lineLength = when (lineType) {
                LineType.FiveStep -> style.fiveStepLineLength.toPx()
                LineType.Normal -> style.normalLineLength.toPx()
                LineType.TenStep -> style.tenStepLineLength.toPx()
            }
            val lineColor = when (lineType) {
                LineType.FiveStep -> style.fiveStepLineColor
                LineType.Normal -> style.normalLineColor
                LineType.TenStep -> style.tenStepLineColor
            }
            // bottom position of each line with Polar Coordinate
            val lineStart = Offset(
                x = ((outerRadius - lineLength) * cos(angleInRad)) + circleCenter.x,
                y = ((outerRadius - lineLength) * sin(angleInRad)) + circleCenter.y
            )
            // top position of each line with Polar Coordinate
            val lineEnd = Offset(
                x = (outerRadius * cos(angleInRad)) + circleCenter.x,
                y = (outerRadius * sin(angleInRad)) + circleCenter.y
            )

            drawContext.canvas.nativeCanvas.apply {
                // draw numbers only for 20, 30, 40 ...
                if (lineType is LineType.TenStep) {
                    // where to draw text with Polar Coordinate
                    val textRadius = outerRadius - lineLength - 5.dp.toPx() - style.textSize.toPx()
                    val x = (textRadius * cos(angleInRad)) + circleCenter.x
                    val y = (textRadius * sin(angleInRad)) + circleCenter.y
                    withRotation(
                        degrees = (angleInRad * (180f / PI.toFloat())) + 90f,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            abs(currentWeight).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            // draw scale indicator
            val middleTop = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
            )
            val bottomLeft = Offset(
                x = circleCenter.x - 5f,
                y = circleCenter.y - innerRadius
            )
            val bottomRight = Offset(
                x = circleCenter.x + 5f,
                y = circleCenter.y - innerRadius
            )
            val indicator = Path().apply {
                moveTo(middleTop.x, middleTop.y)
                lineTo(bottomLeft.x, bottomLeft.y)
                lineTo(bottomRight.x, bottomRight.y)
                lineTo(middleTop.x, middleTop.y)
            }
            drawPath(
                path = indicator,
                color = style.scaleIndicatorColor
            )

        }
    }
}

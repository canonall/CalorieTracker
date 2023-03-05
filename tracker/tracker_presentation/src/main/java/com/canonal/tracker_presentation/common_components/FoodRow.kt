package com.canonal.tracker_presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.canonal.core_ui.spacing

fun Modifier.foodRow(): Modifier =
    composed {
        val cornerSize = 5.dp
        clip(RoundedCornerShape(cornerSize))
            .padding(MaterialTheme.spacing.spaceExtraSmall)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(cornerSize)
            )
            .background(MaterialTheme.colors.surface)
    }

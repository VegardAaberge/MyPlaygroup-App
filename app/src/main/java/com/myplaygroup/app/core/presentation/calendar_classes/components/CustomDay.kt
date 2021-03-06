package com.myplaygroup.app.core.presentation.calendar_classes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.LocalDateTime

@Composable
fun CustomDay(
    state: DayState<DynamicSelectionState>,
    dailyClasses: List<DailyClass>,
    modifier: Modifier = Modifier,
) {
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)
    val isCompleted = state.date.dayOfYear < LocalDateTime.now().dayOfYear
    val isCancelled = dailyClasses.any() && dailyClasses.all { it.cancelled }

    var classColor = when {
        isSelected -> {
            if(!isCancelled){
                MaterialTheme.colors.primaryVariant
            }else{
                val argbColor = ColorUtils.blendARGB(MaterialTheme.colors.primaryVariant.toArgb(), MaterialTheme.colors.error.toArgb(), 0.7f)
                Color(argbColor)
            }
        }
        dailyClasses.any() && isCompleted -> {
            if(!isCancelled){
                Color.LightGray
            }else{
                val argbColor = ColorUtils.blendARGB(Color.LightGray.toArgb(), MaterialTheme.colors.error.toArgb(), 0.5f)
                Color(argbColor)
            }
        }
        dailyClasses.any() && !isCompleted -> {
            if(!isCancelled){
                MaterialTheme.colors.primary
            }else{
                MaterialTheme.colors.error
            }
        }
        else -> {
            Color.Unspecified
        }
    }


    val textColor =  if(dailyClasses.any() || isSelected){
        Color.White
    }
    else if(state.isCurrentDay){
        MaterialTheme.colors.primary
    }else if(state.date.dayOfYear < LocalDateTime.now().dayOfYear){
        Color.LightGray
    }
    else Color.Unspecified

    val textWeight = if(isSelected){
        FontWeight.Bold
    } else FontWeight.Normal

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(7.dp)
            .background(classColor, CircleShape)
            .clip(CircleShape)
            .clickable {
                selectionState.onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                color = textColor,
                fontWeight = textWeight,
                text = date.dayOfMonth.toString()
            )
        }
    }
}
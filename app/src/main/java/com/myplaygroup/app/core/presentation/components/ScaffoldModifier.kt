package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun scaffoldColumnModifier(onClick: () -> Unit) : Modifier {
    return Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(Color(0xFFFFFDFA))
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = onClick
        )
}
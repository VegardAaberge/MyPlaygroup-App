package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun DefaultTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 12.dp
    )
}
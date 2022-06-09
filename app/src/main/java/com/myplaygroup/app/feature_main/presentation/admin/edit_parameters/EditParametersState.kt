package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import com.myplaygroup.app.feature_main.domain.model.EditItem

data class EditParametersState(
    val parameterItems: List<EditItem> = listOf()
)

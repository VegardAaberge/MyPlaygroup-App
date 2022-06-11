package com.myplaygroup.app.feature_main.presentation.admin.create_plans.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.myplaygroup.app.core.presentation.components.ReadonlyOutlinedTextField
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun OutlinedDateField(
    label: String,
    selected: LocalDate,
    timeChanged: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val itemDate = selected

    val time = itemDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            timeChanged(LocalDate.of(year, month + 1, dayOfMonth))
        }, itemDate.year, itemDate.monthValue - 1, itemDate.dayOfMonth
    )

    ReadonlyOutlinedTextField(
        label = label,
        fieldValue = time,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        datePickerDialog.show()
    }
}
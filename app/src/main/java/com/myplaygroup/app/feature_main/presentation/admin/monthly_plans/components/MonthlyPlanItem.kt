package com.plcoding.stockmarketapp.presentation.company_listings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.core.util.display
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MonthlyPlanItem(
    monthlyPlan: MonthlyPlan,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = monthlyPlan.planName.display(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = monthlyPlan.daysOfWeek
                        .map { x -> x.name.take(1) }
                        .joinToString(", "),
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = monthlyPlan.kidName,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(1f)
                )

                if(monthlyPlan.modified){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = null
                    )
                }
                else if(monthlyPlan.cancelled){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_block_24),
                        tint = MaterialTheme.colors.error,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyPlanItemPreview() {
    MyPlaygroupTheme {
        MonthlyPlanItem(
            monthlyPlan = MonthlyPlan(
                id = -1,
                username = "meng",
                kidName = "emma",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusMonths(1).minusDays(1),
                daysOfWeek = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
                ),
                planName = "EVENING_2",
                cancelled = false,
                planPrice = 790,
                availableClasses = 1
            ),
        )
    }
}
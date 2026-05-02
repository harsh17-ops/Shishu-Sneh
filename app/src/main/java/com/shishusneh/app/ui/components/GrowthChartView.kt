package com.shishusneh.app.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.shishusneh.app.data.entity.WeightEntryEntity
import com.shishusneh.app.utils.DateUtils

@Composable
fun GrowthChartView(
    dobMillis: Long,
    entries: List<WeightEntryEntity>,
    referenceLine: List<Pair<Float, Float>>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                legend.isEnabled = true
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
            }
        },
        update = { chart ->
            val babyPoints = entries.map {
                Entry(DateUtils.monthsBetween(dobMillis, it.recordedAt).toFloat(), it.weightKg.toFloat())
            }
            val whoPoints = referenceLine.map { Entry(it.first, it.second) }

            val babyDataSet = LineDataSet(babyPoints, "Baby weight").apply {
                color = Color.parseColor("#00695C")
                setCircleColor(Color.parseColor("#00695C"))
                lineWidth = 3f
                circleRadius = 4f
                valueTextColor = Color.parseColor("#00695C")
            }

            val referenceDataSet = LineDataSet(whoPoints, "WHO reference").apply {
                color = Color.parseColor("#F57C00")
                lineWidth = 2f
                enableDashedLine(18f, 8f, 0f)
                setDrawCircles(false)
                valueTextColor = Color.parseColor("#F57C00")
            }

            chart.data = LineData(referenceDataSet, babyDataSet)
            chart.invalidate()
        }
    )
}

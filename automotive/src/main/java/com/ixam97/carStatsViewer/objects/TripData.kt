package com.ixam97.carStatsViewer.objects

import com.ixam97.carStatsViewer.plot.PlotLineItem
import com.ixam97.carStatsViewer.plot.PlotMarker
import com.ixam97.carStatsViewer.plot.PlotMarkers
import java.util.*

data class TripData(
    var appVersion: String,
    var saveDate: Date,
    var traveledDistance: Float,
    var usedEnergy: Float,
    var averageConsumption: Float,
    var travelTimeMillis: Long,
    var consumptionPlotLine: List<PlotLineItem>,
    var speedPlotLine: List<PlotLineItem>,
    var chargeCurves: List<ChargeCurve>,
    var markers: List<PlotMarker>
) {

}
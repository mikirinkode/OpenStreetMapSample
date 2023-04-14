package com.mikirinkode.openstreetmapsample

import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController

data class LocationItem(

    val name: String,
    val latitude: Double,
    val longitude: Double,

) {
    companion object {
        fun getDummyLocations(): List<LocationItem> = listOf(
            LocationItem("Yogyakarta", -7.795472672400174, 110.36963783598146),
            LocationItem("Jakarta", -6.209391770495319, 106.84590364284973),
            LocationItem("Lampung", -5.397676692956404, 105.26744430506268),
            LocationItem("Aceh", 5.549561258346542, 95.32361783502326),
            LocationItem("Banjarmasin", -3.3199803641109566, 114.59167423572727),
            LocationItem("Makassar", -5.035103241875181, 119.4051583846377),
            LocationItem("Merauke", -8.498153466096419, 140.40531141326272),
        )
    }
}
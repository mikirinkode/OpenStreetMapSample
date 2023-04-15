package com.mikirinkode.openstreetmapsample.feature.main

import android.content.Context
import android.location.Address
import android.location.Geocoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class MainPresenter(private val view: MainView) {
    fun getAddress(context: Context, lat: Double, lng: Double){
        CoroutineScope(Dispatchers.IO).launch {

            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
                val obj = addresses?.get(0)
                if (obj != null) {
                    withContext(Dispatchers.Main){
                        view.updateAddress(obj.getAddressLine(0))
                    }
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
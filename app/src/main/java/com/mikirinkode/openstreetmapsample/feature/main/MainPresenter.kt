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
//                        return obj.getAddressLine(0)
//                    binding.edtStartLocation.text = obj.getAddressLine(0)
                }
//                binding.edtStartLocation.text = "error"
//                    return ""

            } catch (e: IOException) {
//                binding.edtStartLocation.text = "error"
                e.printStackTrace()
//                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//                    return "${e.message}"
            }

        }
    }
}
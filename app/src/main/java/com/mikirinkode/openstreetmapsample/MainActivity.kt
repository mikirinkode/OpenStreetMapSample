package com.mikirinkode.openstreetmapsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.app.ActivityCompat
import com.mikirinkode.openstreetmapsample.databinding.ActivityMainBinding
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val map: MapView by lazy {
        binding.mapView
    }

    private val mapController: IMapController by lazy {
        map.controller
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(binding.root)
        setupMap()
        actionClick()
    }

    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        // setup starting view
        val startPoint = GeoPoint(-3.150333537966098, 115.70600362763918)
        mapController.setCenter(startPoint)
        mapController.setZoom(4.5)

//        map.addMapListener(object: MapListener {
//            override fun onScroll(event: ScrollEvent?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onZoom(event: ZoomEvent?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//        })

        val mapEventsReceiver = object : MapEventsReceiver{
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { createMarker(it) }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return true
            }

        }
        val mapEventsOverlay = MapEventsOverlay(this,  mapEventsReceiver)
        map.overlays.add(mapEventsOverlay)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initCityMarker(locationList: List<LocationItem>) {
        for (location in locationList) {
            val markerPoint = GeoPoint(location.latitude, location.longitude) // jakarta
            val marker = Marker(map)
            marker.position = markerPoint
            marker.title = location.name
            marker.icon = resources.getDrawable(R.drawable.ic_location)

            map.overlays.add(marker)
        }
    }

    private fun createMarker(geoPoint: GeoPoint){
        val markerPoint = geoPoint // jakarta
        val marker = Marker(map)
        marker.position = markerPoint
        marker.title = "Your Selected Location"
        marker.icon = resources.getDrawable(R.drawable.ic_location)

        map.overlays.add(marker)
    }

    private fun observeUserLocation() {
        showLoading()
        val myLocation = MyLocationNewOverlay(GpsMyLocationProvider(this@MainActivity), map)
        myLocation.enableMyLocation()
        map.overlays.add(myLocation)
        myLocation.runOnFirstFix(Runnable {
            runOnUiThread {
                mapController.animateTo(myLocation.myLocation, 18.0, 10L)
                hideLoading()
            }
        })
    }

    private fun actionClick() {
        binding.apply {
            fabMyLocation.setOnClickListener {
                observeUserLocation()
            }
            switchShowCity.setOnCheckedChangeListener { compoundButton, value ->
                showLoading()
                if (value) {
                    initCityMarker(LocationItem.getDummyLocations())
                } else {
                    map.overlays.clear()
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
    }
}
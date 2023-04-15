package com.mikirinkode.openstreetmapsample

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mikirinkode.openstreetmapsample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainView {

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

    private val presenter: MainPresenter by lazy {
        MainPresenter(this)
    }

    private var startLocation: String? = null
    private var startLocationLatLng: GeoPoint? = null
    private var destinationLocation: String? = null
    private var destinationLocationLatLng: GeoPoint? = null

    private var tempLatLng: GeoPoint? = null
    private var tempIndex: Int? = null

    companion object {
        private const val BTN_TEXT_SELECT_AS_START_LOCATION = "Pilih sebagai lokasi awal"
        private const val BTN_TEXT_SELECT_AS_DESTINATION_LOCATION = "Pilih sebagai lokasi tujuan"
        private const val BTN_TEXT_CONFIRM = "Konfirmasi & Lanjutkan"

        const val SEARCH_PICKUP_CODE = 1
        const val SEARCH_DESTINATION_CODE = 2
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(binding.root)
        setupMap()
        actionClick()
        observeOnUpdateData()
    }

    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.setBuiltInZoomControls(false)

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

        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    Log.e("Main", "${map.overlays}")
                    Log.e("Main", "${map.overlays.size}")
                    for (overlay in map.overlays) {
                        Log.e("Main", "${overlay}")
                        Log.e("Main", "${overlay}")
                    }
                if (destinationLocationLatLng == null || startLocationLatLng == null){
                    p?.let { createMarker(it) }
                    if (p != null) {
                        presenter.getAddress(this@MainActivity, p.latitude, p.longitude)
                    }
                    observeOnUpdateData()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return true
            }

        }
        val mapEventsOverlay = MapEventsOverlay(this, mapEventsReceiver)
        map.overlays.add(mapEventsOverlay)

//        val point1 = GeoPoint(-7.795472672400174, 110.36963783598146)
//        val point2 = GeoPoint(-6.209391770495319, 106.84590364284973)
//        val line = Polyline(map)
//        line.addPoint(point1)
//        line.addPoint(point2)
//
//        map.overlays.add(line)
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

    private fun createMarker(geoPoint: GeoPoint) {
        tempLatLng = geoPoint

        if (tempIndex != null) {
            map.overlays.removeAt(tempIndex!!)
            tempIndex = null
        }

        val markerPoint = geoPoint // jakarta
        val marker = Marker(map)
        marker.position = markerPoint
        marker.title = "Your Selected Location"
        marker.icon = resources.getDrawable(R.drawable.ic_location)

        if (startLocation == null) {
            marker.icon.setTint(resources.getColor(R.color.purple_500))
        } else {
            marker.icon.setTint(resources.getColor(R.color.teal_200))
        }

        map.overlays.add(marker)
        tempIndex = map.overlays.indexOf(marker)
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

    private fun observeOnUpdateData() {
        val emptyStart = startLocation == null && startLocationLatLng == null
        val emptyDestination = destinationLocation == null && destinationLocationLatLng == null

        if (binding.edtStartLocation.text == "Lokasi Awal" && tempLatLng == null){
            binding.btnSelectLocation.isEnabled = false
        } else if(startLocationLatLng != null && binding.edtDestinationLocation.text == "Lokasi Tujuan" && tempLatLng == null){
            binding.btnSelectLocation.isEnabled = false
        } else {
            binding.btnSelectLocation.isEnabled = true
        }

        if (!emptyStart && !emptyDestination){
            createDestinationMarker()

            val line = Polyline(map)
            line.addPoint(startLocationLatLng)
            line.addPoint(destinationLocationLatLng)

            map.overlays.add(line)

            binding.btnSelectLocation.text = BTN_TEXT_CONFIRM
        }
        else if (emptyStart) {
            binding.layoutDestinationLocation.visibility = View.GONE
            binding.btnSelectLocation.text = BTN_TEXT_SELECT_AS_START_LOCATION
        } else if (emptyDestination) {
            binding.layoutDestinationLocation.visibility = View.VISIBLE
            binding.btnSelectLocation.text = BTN_TEXT_SELECT_AS_DESTINATION_LOCATION
        }
    }

    private fun createStartMarker() {
        val markerPoint = startLocationLatLng // jakarta
        val marker = Marker(map)
        marker.position = markerPoint
        marker.title = "Lokasi awal atau lokasi penjemputan"
        marker.icon = resources.getDrawable(R.drawable.ic_location)

        marker.icon.setTint(resources.getColor(R.color.purple_500))

        map.overlays.add(marker)
    }

    private fun createDestinationMarker() {
//        if (tempIndex != null) {
//            map.overlays.removeAt(tempIndex!!)
//            tempIndex = null
//        }

        val markerPoint = startLocationLatLng // jakarta
        val marker = Marker(map)
        marker.position = markerPoint
        marker.title = "Lokasi yang dituju"
        marker.icon = resources.getDrawable(R.drawable.ic_location)

        marker.icon.setTint(resources.getColor(R.color.teal_200))

        map.overlays.add(marker)
    }

    override fun updateAddress(address: String) {
        if (startLocation == null || startLocationLatLng == null){
            binding.edtStartLocation.text = address
        } else {
            binding.edtDestinationLocation.text = address
        }
    }


    private fun actionClick() {
        binding.apply {
            fabMyLocation.setOnClickListener {
                observeUserLocation()
            }
//            switchShowCity.setOnCheckedChangeListener { compoundButton, value ->
//                if (value) {
//                    initCityMarker(LocationItem.getDummyLocations())
//                } else {
//                    map.overlays.clear()
//                }
//            }

            layoutStartLocation.setOnClickListener {
                startActivityForResult(Intent(this@MainActivity, SearchActivity::class.java), SEARCH_PICKUP_CODE)
            }
            layoutDestinationLocation.setOnClickListener {
                startActivityForResult(Intent(this@MainActivity, SearchActivity::class.java), SEARCH_DESTINATION_CODE)
            }

            btnSelectLocation.setOnClickListener {
                when (btnSelectLocation.text) {
                    BTN_TEXT_SELECT_AS_START_LOCATION -> {
                        startLocation = edtStartLocation.text.toString().trim()
                        startLocationLatLng = tempLatLng
                        tempLatLng = null
                        createStartMarker()
                        observeOnUpdateData()
                    }
                    BTN_TEXT_SELECT_AS_DESTINATION_LOCATION -> {
                        destinationLocation = edtDestinationLocation.text.toString().trim()
                        destinationLocationLatLng = tempLatLng
                        observeOnUpdateData()
                    }
                    BTN_TEXT_CONFIRM -> {

                    }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEARCH_PICKUP_CODE -> {
                val address = data?.getStringExtra(EXTRA_ADDRESS)
                val latitude = data?.getStringExtra(EXTRA_LATITUDE)?.toDouble() ?: 0.0
                val longitude = data?.getStringExtra(EXTRA_LONGITUDE)?.toDouble() ?: 0.0
                val point = GeoPoint(latitude, longitude)

                binding.edtStartLocation.text = address.toString()
                createMarker(point)

                observeOnUpdateData()
            }
            SEARCH_DESTINATION_CODE -> {
                val address = data?.getStringExtra(EXTRA_ADDRESS)
                val latitude = data?.getStringExtra(EXTRA_LATITUDE)?.toDouble() ?: 0.0
                val longitude = data?.getStringExtra(EXTRA_LONGITUDE)?.toDouble() ?: 0.0
                val point = GeoPoint(latitude, longitude)

                binding.edtDestinationLocation.text = address.toString()
                createMarker(point)

                observeOnUpdateData()
            }
        }
    }
}
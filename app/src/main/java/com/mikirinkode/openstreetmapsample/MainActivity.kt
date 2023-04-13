package com.mikirinkode.openstreetmapsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat
import com.mikirinkode.openstreetmapsample.databinding.ActivityMainBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val map: MapView by lazy {
        binding.mapView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(binding.root)

        map.setTileSource(TileSourceFactory.MAPNIK)
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
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
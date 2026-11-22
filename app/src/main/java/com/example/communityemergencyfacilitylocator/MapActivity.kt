package com.example.communityemergencyfacilitylocator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import com.google.firebase.database.*

import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.MarkerOptions

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapLibreMap: MapLibreMap

    private var userLatitude = 0.0
    private var userLongitude = 0.0

    // PASTE YOUR FIREBASE URL BETWEEN THE QUOTES
    private val DATABASE_URL = "https://emergency-facility-locator-default-rtdb.asia-southeast1.firebasedatabase.app"

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this)

        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->

            map.setStyle("https://tiles.openfreemap.org/styles/liberty") {

                mapLibreMap = map

                map.setOnMarkerClickListener { marker ->

                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(marker.title)
                        .setMessage(marker.snippet)
                        .setPositiveButton("OK", null)
                        .show()

                    true
                }

                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(14.0722, 120.6318))
                    .zoom(14.0)
                    .build()

                loadFacilities()
                checkLocationPermission()
            }
        }

    }

    private fun checkLocationPermission() {

        val fineLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocation == PackageManager.PERMISSION_GRANTED ||
            coarseLocation == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @Suppress("MissingPermission")
    private fun getCurrentLocation() {

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location ->

            if (location != null) {

                val userLocation = LatLng(location.latitude, location.longitude)

                userLatitude = location.latitude
                userLongitude = location.longitude

                mapLibreMap.addMarker(
                    MarkerOptions()
                        .position(userLocation)
                        .title("Your Current Location")
                )

                mapLibreMap.cameraPosition = CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(14.0)
                    .build()

            } else {
                Toast.makeText(
                    this,
                    "Unable to detect location. Using default map location.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // LOAD FACILITIES FROM FIREBASE
    private fun loadFacilities() {

        val database = FirebaseDatabase
            .getInstance(DATABASE_URL)
            .getReference("facilities")

        database.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val selectedType =
                        intent.getStringExtra("TYPE")

                    var pins = 0

                    for (facility in snapshot.children) {

                        val name = facility.child("name")
                            .value?.toString() ?: continue

                        val type = facility.child("type")
                            .value?.toString() ?: "Facility"

                        if (selectedType != null &&
                            type != selectedType) {
                            continue
                        }

                        val contacts = facility.child("contacts")
                            .value?.toString() ?: "No contact"

                        val latitude = facility.child("latitude")
                            .value?.toString()?.toDoubleOrNull() ?: continue

                        val longitude = facility.child("longitude")
                            .value?.toString()?.toDoubleOrNull() ?: continue

                        val results = FloatArray(1)

                        android.location.Location.distanceBetween(
                            userLatitude,
                            userLongitude,
                            latitude,
                            longitude,
                            results
                        )

                        val distanceKm = results[0] / 1000

                        when (type) {

                            "Hospital" -> {
                                mapLibreMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title("🏥 $name")
                                        .snippet("Contact: $contacts\nDistance: %.2f km".format(distanceKm))
                                )
                            }

                            "Police" -> {
                                mapLibreMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title("👮 $name")
                                        .snippet("Contact: $contacts\nDistance: %.2f km".format(distanceKm))
                                )
                            }

                            "Fire" -> {
                                mapLibreMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title("🚒 $name")
                                        .snippet("Contact: $contacts\nDistance: %.2f km".format(distanceKm))
                                )
                            }

                            else -> {
                                mapLibreMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title(name)
                                        .snippet("Contact: $contacts\nDistance: %.2f km".format(distanceKm))
                                )
                            }
                        }

                        pins++
                    }

                    Toast.makeText(
                        this@MapActivity,
                        "Found ${snapshot.childrenCount} items, $pins pins added",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MapActivity,
                        "Firebase Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                checkLocationPermission()
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { mapView.onPause(); super.onPause() }
    override fun onStop() { mapView.onStop(); super.onStop() }
    override fun onLowMemory() { super.onLowMemory(); mapView.onLowMemory() }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
    override fun onDestroy() { mapView.onDestroy(); super.onDestroy() }
}
package pl.cdv.fitWalk
import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
class LocationHelper(private val fusedLocationClient: FusedLocationProviderClient, private val googleMap: GoogleMap) {
    private lateinit var userLocationMarker: Marker
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                updateUserLocationMarker(currentLocation)
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun updateUserLocationMarker(currentLocation: LatLng) {
        userLocationMarker?.remove()

        userLocationMarker = googleMap.addMarker(
            MarkerOptions()
                .position(currentLocation)
                .title("Your Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )!!

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationAndSetMarkers() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentPosition = LatLng(it.latitude, it.longitude)
                    userLocationMarker = googleMap.addMarker(MarkerOptions().position(currentPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Your Position"))!!
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15f))

                    generateRandomMarkers(it.latitude - 0.01, it.latitude + 0.01, it.longitude - 0.01, it.longitude + 0.01)
                } ?: run {
                    // Lokalizacja jest null, co może się zdarzyć w przypadku, gdy nie ma ostatniej lokalizacji.
                    // Tutaj możesz obsłużyć to zdarzenie.
                    // Log.e(TAG, "Ostatnia lokalizacja jest null")
                }
            }
            .addOnFailureListener { e ->
                // Błąd podczas pobierania lokalizacji.
                // Log.e(TAG, "Błąd podczas pobierania lokalizacji: ${e.message}")
            }
    }

    private fun generateRandomMarkers(maxLat: Double, minLat: Double, maxLng: Double, minLng: Double) {
        val randomPoints = mutableListOf<LatLng>()

        for (i in 0 until 10) {
            val randomLat = (Math.random() * (maxLat - minLat)) + minLat
            val randomLng = (Math.random() * (maxLng - minLng)) + minLng
            val randomPoint = LatLng(randomLat, randomLng)
            randomPoints.add(randomPoint)

            googleMap.addMarker(
                MarkerOptions()
                    .position(randomPoint)
                    .title("Random Point $i")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
    }
}
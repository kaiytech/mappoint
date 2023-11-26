package pl.cdv.fitWalk
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.widget.TextView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
class LocationHelper(private val fusedLocationClient: FusedLocationProviderClient, private val googleMap: GoogleMap,context:Context,nameTextView: TextView) {
    private lateinit var userLocationMarker: Marker
    private lateinit var gameHelper: GameHelper



    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                updateUserLocationMarker(currentLocation,location.bearing)
                gameHelper.checkPointIsCollected(location, context, nameTextView)
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(100)

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun updateUserLocationMarker(currentLocation: LatLng,bearing: Float) {
        userLocationMarker?.remove()

        userLocationMarker = googleMap.addMarker(
            MarkerOptions()
                .position(currentLocation)
                .title("Your Position")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                .rotation(bearing)
        )!!

val cameraPosition= CameraPosition.Builder().target(currentLocation).zoom(15f).bearing(bearing).build()
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationAndSetMarkers() {
        gameHelper = GameHelper(googleMap)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentPosition = LatLng(it.latitude, it.longitude)
                    userLocationMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(currentPosition)
                            .title("Your Position")
                            .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                            .rotation(it.bearing)
                    )!!
                    gameHelper.generateRandomMarkers(it.latitude - 0.01, it.latitude + 0.01, it.longitude - 0.01, it.longitude + 0.01)
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
    @SuppressLint("MissingPermission")

    fun getCurrentLocationAndSetCamera() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentPosition = LatLng(it.latitude, it.longitude)
                    updateUserLocationMarker(currentPosition,it.bearing)

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
}
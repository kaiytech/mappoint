package pl.cdv.fitWalk

import android.content.Context
import android.location.Location
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GameHelper(private val googleMap: GoogleMap ) {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val randomPoints = mutableListOf<LatLng>()
    fun generateRandomMarkers(maxLat: Double, minLat: Double, maxLng: Double, minLng: Double) {
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

    fun checkPointIsCollected(currentLocation: Location, context: Context,nameTextView: TextView) {
        val iterator = randomPoints.iterator()

        while (iterator.hasNext()) {
            val point = iterator.next()
            val pointLocation = Location("PointLocation")
            pointLocation.latitude = point.latitude
            pointLocation.longitude = point.longitude

            val distance = currentLocation.distanceTo(pointLocation)

            if (distance <= 100) {
                // Użytkownik jest w odległości 30 metrów od punktu, możesz teraz obsłużyć to zdarzenie
                iterator.remove()
                handleCollectedPoint(context,nameTextView)
            }
        }
    }

    private fun handleCollectedPoint(context: Context,nameTextView: TextView) {
        // Usuń zebrany punkt z mapy
        sharedPreferencesHelper = SharedPreferencesHelper(context)

        googleMap.clear()

        // Tutaj możesz dodać dowolną logikę związaną z zebranymi punktami
        // Na przykład, zwiększenie licznika punktów, wyświetlenie komunikatu, itp.
        // Przykładowy kod:
        var collectedPointsCount = sharedPreferencesHelper.getKeyFromSharedPreferences("collectedPointsCount")?.toInt()
        collectedPointsCount = collectedPointsCount!! + 1
        sharedPreferencesHelper.saveKeyToSharedPreferences("collectedPointsCount",collectedPointsCount.toString())
        val yourName = sharedPreferencesHelper.getKeyFromSharedPreferences("your_name")

        nameTextView.text = "Hello, $yourName! Your Points: $collectedPointsCount"
        // Ponownie narysuj na mapie pozostałe punkty (niezebrane)
        for (point in randomPoints) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(point)
                    .title("Random Point")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
    }
}
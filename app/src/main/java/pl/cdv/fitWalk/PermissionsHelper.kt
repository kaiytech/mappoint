import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionsHelper(private val fragment: Fragment) {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    fun checkLocationPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                fragment.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            false
        }
    }

    fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                fragment.requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Tutaj możesz wyjaśnić użytkownikowi, dlaczego potrzebujesz uprawnień do lokalizacji.
            // Na przykład poprzez pokazanie dialogu z wyjaśnieniem.
        } else {
            // Poproś użytkownika o uprawnienia.
            fragment.requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

}

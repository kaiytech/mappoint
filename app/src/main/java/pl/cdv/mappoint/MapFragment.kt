package pl.cdv.mappoint
import PermissionsHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import pl.cdv.mappoint.databinding.MapPageBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: MapPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var permissionsHelper: PermissionsHelper
    private lateinit var locationHelper: LocationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        permissionsHelper = PermissionsHelper(this)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        val yourName = sharedPreferencesHelper.getKeyFromSharedPreferences("your_name")
        val nameTextView = view.findViewById<TextView>(R.id.textview_name)
        nameTextView.text = "Hello, $yourName!"
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        locationHelper = LocationHelper(fusedLocationClient, googleMap)
        if(permissionsHelper.checkLocationPermission()){
            locationHelper.getCurrentLocationAndSetMarkers()
            locationHelper.startLocationUpdates()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
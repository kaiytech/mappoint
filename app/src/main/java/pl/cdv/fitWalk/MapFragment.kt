package pl.cdv.fitWalk
import PermissionsHelper
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import pl.cdv.fitWalk.databinding.MapPageBinding
import java.lang.Exception
import kotlin.system.exitProcess

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: MapPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var permissionsHelper: PermissionsHelper
    private lateinit var locationHelper: LocationHelper
    private lateinit var pointsButton: Button
    private lateinit var nearestButton: Button

    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pointsButton = requireView().findViewById<Button>(R.id.points_button)
        nearestButton = requireView().findViewById<Button>(R.id.nearest_button)

        gestureDetector = GestureDetector(requireContext(), SwipeGestureListener())
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        permissionsHelper = PermissionsHelper(this)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        var collectedPointsCount = sharedPreferencesHelper.getKeyFromSharedPreferences("collectedPointsCount")

        if(collectedPointsCount==""){
            collectedPointsCount = "0"
            sharedPreferencesHelper.saveKeyToSharedPreferences("collectedPointsCount",collectedPointsCount)

        }

        collectedPointsCount = sharedPreferencesHelper.getKeyFromSharedPreferences("collectedPointsCount")
        pointsButton.text = "Your Points: $collectedPointsCount"

        pointsButton.setOnClickListener {
            GoToList()
        }

        nearestButton.setOnClickListener {
            GoToList()
        }


        binding.fab.setOnClickListener {
            locationHelper.getCurrentLocationAndSetCamera()
        }
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        locationHelper = LocationHelper(fusedLocationClient, googleMap,requireContext(),pointsButton, nearestButton)
        if(permissionsHelper.checkLocationPermission()){
            locationHelper.getCurrentLocationAndSetMarkers()
            locationHelper.startLocationUpdates()
        }

        val yourName = sharedPreferencesHelper.getKeyFromSharedPreferences("your_name")
        activity?.setTitle("Hello, $yourName!")
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

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 30
        private val SWIPE_VELOCITY_THRESHOLD = 30

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return super.onDoubleTap(e)
        }

        override fun onFling(
            e1: android.view.MotionEvent,
            e2: android.view.MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                GoToList()
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

    private fun GoToList() {
        try {
            val arrayList: ArrayList<String?> = ArrayList(locationHelper.getAllPoints())
            val bundle = Bundle().apply {
                putStringArrayList("vals", arrayList)
            }
            findNavController().navigate(R.id.action_MapFragment_to_ListFragment2, bundle)
        } catch (e: Exception) {
            var toast = Toast.makeText(requireContext(), "Please wait...", Toast.LENGTH_SHORT)

            toast.setGravity(Gravity.BOTTOM, 0,16)

            toast.show()
        }
    }

}
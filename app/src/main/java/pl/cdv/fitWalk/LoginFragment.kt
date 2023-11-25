package pl.cdv.fitWalk

import PermissionsHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import pl.cdv.fitWalk.databinding.LoginPageBinding
class LoginFragment : Fragment() {

    private var _binding: LoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var permissionsHelper: PermissionsHelper

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = LoginPageBinding.inflate(inflater, container, false);
        return binding.root;

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val loginButton = view.findViewById<Button>(R.id.button_login)
        loginButton.setOnClickListener {
            onLoginButtonClick(it)
        }
        val yourName = sharedPreferencesHelper.getKeyFromSharedPreferences("your_name")
        if (yourName != null) {
            if (yourName.isNotEmpty()) {
                val navController = findNavController()
                navController.navigate(R.id.action_LoginFragment_to_MapFragment)
            }
        }
        permissionsHelper = PermissionsHelper(this)

        if(!permissionsHelper.checkLocationPermission()){
            permissionsHelper.requestLocationPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onLoginButtonClick(view: View) {
        val enteredName = binding.inputName.text.toString()

        if (enteredName.isNotEmpty()) {
            sharedPreferencesHelper.saveKeyToSharedPreferences("your_name",enteredName)

            val navController = findNavController()
            navController.navigate(R.id.action_LoginFragment_to_MapFragment)
        } else {
            Toast.makeText(requireContext(), "Wprowadź swoje imię", Toast.LENGTH_SHORT).show()
        }
    }


}
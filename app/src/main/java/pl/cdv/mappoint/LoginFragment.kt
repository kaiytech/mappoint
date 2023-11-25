package pl.cdv.mappoint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import pl.cdv.mappoint.databinding.LoginPageBinding
class LoginFragment : Fragment() {

    private var _binding: LoginPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = LoginPageBinding.inflate(inflater, container, false);
        return binding.root;

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_SecondFragment)
        }
        val loginButton = view.findViewById<Button>(R.id.button_login)
        loginButton.setOnClickListener {
            onLoginButtonClick(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onLoginButtonClick(view: View) {
        val enteredName = binding.inputName.text.toString()

        // Sprawdź, czy imię nie jest puste
        if (enteredName.isNotEmpty()) {
            // Przygotuj dane do przekazania do kolejnego fragmentu
            val bundle = Bundle()
            bundle.putString("your_name", enteredName)

            // Uzyskaj NavController
            val navController = findNavController()

            // Przejdź do kolejnego fragmentu z przekazanymi danymi
            navController.navigate(R.id.action_LoginFragment_to_SecondFragment, bundle)
        } else {
            // Imię jest puste, możesz obsłużyć to odpowiednią wiadomością lub działaniem
            Toast.makeText(requireContext(), "Wprowadź swoje imię", Toast.LENGTH_SHORT).show()
        }
    }

}
package mx.tecnm.cdhidalgo.lostobjectsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentLoginBinding
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackLogin.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_PresentationFragment)
        }
        binding.btnForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RecoveryPassFragment)
        }
        binding.btnButtonLogin.setOnClickListener {
            /*val intent = Intent(activity, NuevaActividad::class.java)
            activity?.startActivity(intent)
            activity?.finish()*/
        }
        binding.btnGoogleLogin.setOnClickListener {

        }
        binding.btnWishRegister.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
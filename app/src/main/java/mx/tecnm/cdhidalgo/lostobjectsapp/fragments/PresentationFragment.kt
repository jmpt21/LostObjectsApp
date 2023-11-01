package mx.tecnm.cdhidalgo.lostobjectsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentPresentationBinding

class PresentationFragment : Fragment() {

    private var _binding: FragmentPresentationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPresentationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignInPresentation.setOnClickListener {
            findNavController().navigate(R.id.action_PresentationFragment_to_LoginFragment)
        }

        binding.btnSignUpPresentation.setOnClickListener {
            findNavController().navigate(R.id.action_PresentationFragment_to_RegisterFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
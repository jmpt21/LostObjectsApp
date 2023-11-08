package mx.tecnm.cdhidalgo.lostobjectsapp.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentRecoveryPassBinding

class RecoveryPassFragment : Fragment() {
    private var _binding : FragmentRecoveryPassBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        // Inflate the layout for this fragment
        _binding = FragmentRecoveryPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackRecoveryPass.setOnClickListener {
            findNavController().navigate(R.id.action_RecoveryPassFragment_to_LoginFragment)
        }
        binding.btnRecoveryPass.setOnClickListener {
            val email = binding.inputEmailRecovery.editText?.text?.toString()?.trim()
            if (email.isNullOrEmpty() || email.isBlank()){
                Snackbar.make(view, "Ingresa algún valor", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Snackbar.make(view, "Ingresa un correo electrónico válido", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                Snackbar.make(view, "Correo de recuperación enviado", BaseTransientBottomBar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
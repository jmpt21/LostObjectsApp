package mx.tecnm.cdhidalgo.lostobjectsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.HomeActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentRegisterBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.UserDataClass

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        firestore = Firebase.firestore
        auth = Firebase.auth
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBackRegister.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }
        binding.btnRegister.setOnClickListener {
            val nameInput = binding.inputNameRegister.editText?.text?.trim().toString()
            val lastName1 = binding.inputLastname1Register.editText?.text?.trim().toString()
            val lastName2 = binding.inputLastname2Register.editText?.text?.trim().toString()
            val numberPhone = binding.inputNumberPhoneRegister.editText?.text?.trim().toString()
            val email = binding.inputEmailRegister.editText?.text?.trim().toString()
            val password = binding.inputPasswordRegister.editText?.text.toString()
            if (nameInput.isEmpty() || lastName1.isEmpty() || numberPhone.isEmpty() || email.isEmpty() || password.isEmpty()){
                Snackbar.make(view, "¡Ingresa todos los campos necesarios!", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(view, "Formato de correo inválido.", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Snackbar.make(view, "La contraseña debe contener al menos 6 caracteres.", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userData = UserDataClass(nameInput, lastName1, lastName2, numberPhone, email)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val profileUpdates = UserProfileChangeRequest.Builder().apply {
                        displayName = "$nameInput $lastName1 $lastName2".trim()
                    }
                    it.user?.updateProfile(profileUpdates.build())
                    firestore.collection("users").document(email).set(userData)
                        .addOnSuccessListener {
                            goToHome()
                            Toast.makeText(context, "Registro exitoso. Bienvenido :D", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Snackbar.make(view, "Error de registro.", BaseTransientBottomBar.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Snackbar.make(view, setErrorMessage(it.message!!), BaseTransientBottomBar.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun setErrorMessage(ex : String) : String {
        return if (ex.contains("already in use")){
            "Este email ya está registrado, pruebe con otro."
        }else if (ex.contains("network error")) {
            "Error de conexión. Revise la calidad de su red."
        } else if (ex.contains("badly formatted")) {
            "Formato de email inválido."
        } else {
            ex
        }
    }
}
package mx.tecnm.cdhidalgo.lostobjectsapp.fragments

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.HomeActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentLoginBinding
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    //Variables para el uso de OneTap de Google
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    //Code para relacionar el intent y su resultado
    private val codeOneTap = 2
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
        auth = Firebase.auth

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
            val email = binding.inputEmail.editText?.text.toString().trim()
            val password = binding.inputPassword.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    view,
                    "Ingrese valores en ambos campos.",
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    goToHome()
                }
                .addOnFailureListener {
                    Snackbar.make(
                        view,
                        "Fallo en la autenticación, revise sus credenciales de acceso o su conexión a internet.",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
        }
        binding.btnGoogleLogin.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, codeOneTap,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("Error UI", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(requireActivity()) { _ ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Snackbar.make(view, "No se encontraron credenciales de acceso", BaseTransientBottomBar.LENGTH_SHORT).show()
                }
        }
        binding.btnWishRegister.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
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

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.fragment.app.Fragment"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        //Código básico para realizar las acciones correspondientes en caso de iniciar sesión
        //exitosamente o al haber un error. (Puede optimizarse).
        when (requestCode) {
            codeOneTap -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate with your backend.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnFailureListener {
                                    Snackbar.make(
                                        requireView(),
                                        "Error en la autenticación, revisa tu conexión a internet.",
                                        BaseTransientBottomBar.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnCompleteListener(requireActivity()) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        // Aquí van las acciones correspondientes, si es que la autenticación salió bien
                                        val user = auth.currentUser
                                        Toast.makeText(
                                            requireContext(),
                                            "Sesión iniciada como: ${user?.email}",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                        goToHome()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "No se obtuvieron las credenciales de acceso",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }
                        } else -> {
                            // Shouldn't happen.
                            Log.d("No account credentials", "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // Otros errores a controlar
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Toast.makeText(
                                requireContext(),
                                "Inicio de sesión con Google cancelado",
                                Toast.LENGTH_SHORT,
                            ).show()
                            // Don't re-prompt the user.
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            // Try again or just ignore.
                        } else -> {

                        }
                    }
                }
            }
        }
    }
}
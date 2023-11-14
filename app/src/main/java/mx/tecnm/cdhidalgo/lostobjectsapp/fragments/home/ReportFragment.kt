package mx.tecnm.cdhidalgo.lostobjectsapp.fragments.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.HomeActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.auth
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentReportBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass
import mx.tecnm.cdhidalgo.lostobjectsapp.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.userProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ReportFragment : Fragment() {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    private lateinit var imageObject : ImageView
    private lateinit var firestore: FirebaseFirestore
    private val lostObjectsPath = "LostObjects/"
    private val foundObjectsPath = "FoundObjects/"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        firestore = Firebase.firestore
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageObject = binding.imageReport
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.usernameReport.text = auth.currentUser?.displayName
        binding.txtDateReport.text = currentDate

        binding.btnAddReport.setOnClickListener {
            val groupTypeReport = binding.radioGroupReport
            val selectedId = groupTypeReport.checkedRadioButtonId
            val radioValue = view.findViewById<RadioButton>(selectedId)?.text
            if (radioValue == null) {
                showAlert("Selecciona un tipo de reporte", view)
                return@setOnClickListener
            }
            val title = binding.txtObjectReport.text.trim().toString()
            val description = binding.textDescriptionReport.text.trim().toString()
            val location = binding.txtLocationReport.text.trim().toString()

            if (title.isBlank() || description.isBlank() || location.isBlank()) {
                showAlert("Llena todos los campos necesarios", view)
                return@setOnClickListener
            }

            val reportType = if (radioValue == "Encontré un objeto") "Found" else "Lost"
            uploadData(reportType, view)
        }

        binding.btnImageReport.setOnClickListener {
            launchGallery()
        }

        binding.btnDeleteImageReport.setOnClickListener {
            imageObject.setImageDrawable(null)
            filePath = null
            showAddImage()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null ) {
            filePath = data.data
            Glide.with(this).load(filePath).into(imageObject)
            hideAddImage()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideFab()
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity).showFab()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showAlert (message : String, view : View) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT).show()
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    private fun uploadData(typeObject : String, view: View) {
        when (typeObject) {
            "Lost" -> {
                if(filePath != null) {
                    val uuidImage = UUID.randomUUID().toString()
                    val ref = storage.reference.child(lostObjectsPath + uuidImage)
                    ref.putFile(filePath!!)
                        .addOnSuccessListener {
                            val data = ObjectDataClass(
                                null,
                                auth.currentUser?.email,
                                auth.currentUser?.displayName,
                                userProfile?.phoneNumber  ?: auth.currentUser?.phoneNumber,
                                binding.txtObjectReport.text.toString(),
                                binding.textDescriptionReport.text.toString(),
                                binding.txtLocationReport.text.toString(),
                                binding.txtDateReport.text.toString(),
                                "Lost",
                                lostObjectsPath + uuidImage
                            )
                            setData(lostObjectsPath, data, view)
                        }
                        .addOnFailureListener {
                            showAlert("Error al publicar el reporte, revise su conexión a internet.", view)
                        }
                } else {
                    val data = ObjectDataClass(
                        null,
                        auth.currentUser?.email,
                        auth.currentUser?.displayName,
                        userProfile?.phoneNumber ?: auth.currentUser?.phoneNumber,
                        binding.txtObjectReport.text.toString(),
                        binding.textDescriptionReport.text.toString(),
                        binding.txtLocationReport.text.toString(),
                        binding.txtDateReport.text.toString(),
                        "Lost",
                        null
                    )
                    setData(lostObjectsPath, data, view)
                }
            }
            "Found" -> {
                if(filePath != null) {
                    val uuidImage = UUID.randomUUID().toString()
                    val ref = storage.reference.child(foundObjectsPath + uuidImage)
                    ref.putFile(filePath!!)
                        .addOnSuccessListener {
                            val data = ObjectDataClass(
                                null,
                                auth.currentUser?.email,
                                auth.currentUser?.displayName,
                                userProfile?.phoneNumber ?: auth.currentUser?.phoneNumber,
                                binding.txtObjectReport.text.toString(),
                                binding.textDescriptionReport.text.toString(),
                                binding.txtLocationReport.text.toString(),
                                binding.txtDateReport.text.toString(),
                                "Found",
                                foundObjectsPath + uuidImage
                            )
                            setData(foundObjectsPath, data, view)
                        }
                        .addOnFailureListener {
                            showAlert("Error al publicar el reporte, revise su conexión a internet.", view)
                        }
                } else {
                    val data = ObjectDataClass(
                        null,
                        auth.currentUser?.email,
                        auth.currentUser?.displayName,
                        userProfile?.phoneNumber ?: auth.currentUser?.phoneNumber,
                        binding.txtObjectReport.text.toString(),
                        binding.textDescriptionReport.text.toString(),
                        binding.txtLocationReport.text.toString(),
                        binding.txtDateReport.text.toString(),
                        "Found",
                        null
                    )
                    setData(foundObjectsPath, data, view)
                }
            }
        }
    }

    private fun setData(path: String, data: ObjectDataClass, view: View) {
        val docRef = firestore.collection(path).document()
        val id = docRef.id
        docRef.set(data.copy(id = id))
            .addOnSuccessListener {
                Toast.makeText(context, "Reporte publicado", Toast.LENGTH_SHORT).show()
                backToHome()
            }
            .addOnFailureListener {
                showAlert("Error al publicar el reporte, revise su conexión a internet.", view)
            }
    }

    private fun hideAddImage() {
        binding.btnImageReport.visibility = View.GONE
    }

    private fun showAddImage() {
        binding.btnImageReport.visibility = View.VISIBLE
    }

    private fun backToHome() {
        findNavController().navigate(R.id.action_ReportFragment_to_HomeFragment)
    }
}
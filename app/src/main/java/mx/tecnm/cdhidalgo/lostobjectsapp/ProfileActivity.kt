package mx.tecnm.cdhidalgo.lostobjectsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.UserDataClass

class ProfileActivity : AppCompatActivity() {
    private lateinit var btnBack : AppCompatImageButton
    private lateinit var txtName : EditText
    private lateinit var txtLastName1 : EditText
    private lateinit var txtLastName2 : EditText
    private lateinit var txtPhone : EditText
    private lateinit var txtEmail : EditText
    private lateinit var btnUpdate : MaterialButton
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        db = Firebase.firestore
        if (userProfile == null) {
            MaterialAlertDialogBuilder(this,
                com.google.android.material.R.style.AlertDialog_AppCompat)
                .setTitle("Información")
                .setMessage("Completa tu perfil para una mejor experiencia")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        txtName = findViewById(R.id.txt_name_profile)
        txtLastName1 = findViewById(R.id.txt_lastname_1_profile)
        txtLastName2 = findViewById(R.id.txt_lastname_2_profile)
        txtPhone = findViewById(R.id.txt_phone_profile)
        txtEmail = findViewById(R.id.txt_email_profile)
        btnUpdate = findViewById(R.id.btn_edit_profile)
        btnBack = findViewById(R.id.btn_back_profile)

        txtName.setText(userProfile?.name ?: "")
        txtLastName1.setText(userProfile?.lastName1 ?: "")
        txtLastName2.setText(userProfile?.lastName2 ?: "")
        txtPhone.setText(userProfile?.phoneNumber ?: "")
        txtEmail.setText(auth.currentUser?.email ?: "")

        btnUpdate.setOnClickListener {
            val name = String.format("%s %s %s", txtName.text.trim(), txtLastName1.text.trim(), txtLastName2.text.trim())
            val phoneNumber = txtPhone.text.trim().toString()
            if (txtName.text.isEmpty() || txtLastName1.text.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Completa tu perfil", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = UserDataClass(
                txtName.text.trim().toString(),
                txtLastName1.text.trim().toString(),
                txtLastName2.text.trim().toString(),
                phoneNumber,
                txtEmail.text.trim().toString()
            )

            MaterialAlertDialogBuilder(this, com.google.android.material.R.style.AlertDialog_AppCompat)
                .setTitle("Confirmación")
                .setMessage("Nombre: ${name.trim()}\nNúmero de teléfono: $phoneNumber")
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("Aceptar") { _, _ ->
                    db.collection("users").document(auth.currentUser?.email!!).set(user)
                        .addOnSuccessListener {
                            userProfile = user
                            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Log.e("Error", it.message.toString())
                        }
                }
                .show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
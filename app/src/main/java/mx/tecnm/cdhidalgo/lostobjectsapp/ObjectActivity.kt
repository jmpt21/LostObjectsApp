package mx.tecnm.cdhidalgo.lostobjectsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass

class ObjectActivity : AppCompatActivity() {
    private lateinit var btnBack: MaterialButton
    private lateinit var usernameObject: MaterialTextView
    private lateinit var dateObject: MaterialTextView
    private lateinit var titleObject: MaterialTextView
    private lateinit var descriptionObject: MaterialTextView
    private lateinit var locationObject: MaterialTextView
    private lateinit var imageObject: ShapeableImageView
    private lateinit var btnWhatsapp: AppCompatImageButton
    private lateinit var btnCall: AppCompatImageButton
    private lateinit var btnMessage: AppCompatImageButton
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)

        storage = Firebase.storage
        val objectData = intent.getParcelableExtra<ObjectDataClass>("object")!!
        btnBack = findViewById(R.id.btn_back_full_report)
        usernameObject = findViewById(R.id.username_object)
        dateObject = findViewById(R.id.date_object)
        titleObject = findViewById(R.id.title_object)
        descriptionObject = findViewById(R.id.description_object)
        locationObject = findViewById(R.id.location_object)
        imageObject = findViewById(R.id.image_object)
        btnWhatsapp = findViewById(R.id.btn_whatsapp)
        btnCall = findViewById(R.id.btn_numberPhone)
        btnMessage = findViewById(R.id.btn_message)

        btnBack.setOnClickListener {
            finish()
        }

        usernameObject.text = objectData.username
        dateObject.text = buildString { append("Fecha: ").append(objectData.date) }
        titleObject.text = buildString { append("Objeto: ").append(objectData.title) }
        descriptionObject.text = buildString { append("Descripción: ").append(objectData.description) }
        locationObject.text = buildString { append("Ubicación: ").append(objectData.location) }
        val imageUrl = objectData.imageUrl
        if (imageUrl != null && imageUrl != "") {
            val storage = storage.reference.child(imageUrl)
            storage.downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).into(imageObject)
            }
        } else {
            //imageObject.setImageResource(R.drawable.default_object)
            imageObject.setImageDrawable(null)
        }

        val phoneNumber = objectData.phoneNumber
        val message = "Hola, vi tu publicación en Lost Objects App sobre tu objeto \"${objectData.title}\" y tengo información al respecto."
        btnWhatsapp.setOnClickListener {
            goToWhatsApp(this, phoneNumber, message)
        }

        btnCall.setOnClickListener {
            goToCall(this, phoneNumber)
        }

        btnMessage.setOnClickListener {
            goToMessage(this, phoneNumber, message)
        }
    }
}
package mx.tecnm.cdhidalgo.lostobjectsapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.UserDataClass
import java.net.URLEncoder

val auth : FirebaseAuth = Firebase.auth
val storage : FirebaseStorage = Firebase.storage
var userProfile: UserDataClass? = null

fun goToWhatsApp(activity : Activity, phoneNumber : String?, message : String = "") {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(
        "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
            URLEncoder.encode(
                message,
                "UTF-8"
            )
        }"
    )
    try {
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(activity, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
    }
}
fun goToCall(activity : Activity, phoneNumber : String?) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    try {
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(activity, "No se puede realizar la llamada", Toast.LENGTH_SHORT).show()
    }
}

fun goToMessage(activity : Activity, phoneNumber : String?, message : String = "") {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("sms:$phoneNumber")
    intent.putExtra("sms_body", message)
    try {
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(activity, "No se puede enviar el mensaje", Toast.LENGTH_SHORT).show()
    }
}
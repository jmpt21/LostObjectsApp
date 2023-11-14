package mx.tecnm.cdhidalgo.lostobjectsapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass
import mx.tecnm.cdhidalgo.lostobjectsapp.goToCall
import mx.tecnm.cdhidalgo.lostobjectsapp.goToMessage
import mx.tecnm.cdhidalgo.lostobjectsapp.goToWhatsApp

class GeneralPostAdapter (
    private val list: List<ObjectDataClass>,
    private val activity: Activity
) : RecyclerView.Adapter<GeneralPostAdapter.GeneralPostViewHolder>() {
    class GeneralPostViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val username: MaterialTextView = item.findViewById(R.id.username_object_all_reports)
        val date: MaterialTextView = item.findViewById(R.id.date_object_all_reports)
        val title: MaterialTextView = item.findViewById(R.id.title_object_all_reports)
        val description: MaterialTextView = item.findViewById(R.id.description_object_all_reports)
        val location: MaterialTextView = item.findViewById(R.id.location_object_all_reports)
        val image: ShapeableImageView = item.findViewById(R.id.image_object_all_reports)
        val btnWhatsapp: AppCompatImageButton = item.findViewById(R.id.btn_whatsapp_all_reports)
        val btnCall: AppCompatImageButton = item.findViewById(R.id.btn_numberPhone_all_reports)
        val btnMessage: AppCompatImageButton = item.findViewById(R.id.btn_message_all_reports)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralPostViewHolder {
        return GeneralPostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.general_card_object, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GeneralPostViewHolder, position: Int) {
        val item = list[position]
        holder.username.text = item.username
        holder.date.text = buildString { append("Fecha: ").append(item.date) }
        holder.title.text = buildString { append("Objeto: ").append(item.title) }
        holder.description.text = buildString { append("Descripción: ").append(item.description) }
        holder.location.text = buildString { append("Ubicación: ").append(item.location) }
        val imageUrl = item.imageUrl
        if (imageUrl != null && imageUrl != "") {
            val storage = Firebase.storage.reference.child(imageUrl)
            storage.downloadUrl.addOnSuccessListener {
                Glide.with(activity).load(it).into(holder.image)
            }
        } else {
            //holder.image.setImageResource(R.drawable.default_object)
            holder.image.setImageDrawable(null)
        }
        val phoneNumber = item.phoneNumber
        val message = "Hola, vi tu publicación en Lost Objects App sobre tu objeto \"${item.title}\" y tengo información al respecto."
        holder.btnWhatsapp.setOnClickListener {
            goToWhatsApp(activity, phoneNumber, message)
        }
        holder.btnCall.setOnClickListener {
            goToCall(activity, phoneNumber)
        }
        holder.btnMessage.setOnClickListener {
            goToMessage(activity, phoneNumber, message)
        }
    }
}
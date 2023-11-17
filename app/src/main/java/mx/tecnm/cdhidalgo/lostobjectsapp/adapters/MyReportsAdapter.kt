package mx.tecnm.cdhidalgo.lostobjectsapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass
import mx.tecnm.cdhidalgo.lostobjectsapp.storage

class MyReportsAdapter(
    private val list: List<ObjectDataClass>,
    private val activity: Activity
) : RecyclerView.Adapter<MyReportsAdapter.MyReportsViewHolder>() {
    class MyReportsViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val btnDelete: MaterialButton = item.findViewById(R.id.btn_delete_my_report)
        val username: MaterialTextView = item.findViewById(R.id.username_my_object)
        val date: MaterialTextView = item.findViewById(R.id.date_my_object)
        val title: MaterialTextView = item.findViewById(R.id.title_my_object)
        val description: MaterialTextView = item.findViewById(R.id.description_my_object)
        val location: MaterialTextView = item.findViewById(R.id.location_my_object)
        val image: ShapeableImageView = item.findViewById(R.id.image_my_object)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportsViewHolder {
        return MyReportsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_reports_card, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyReportsViewHolder, position: Int) {
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

        holder.btnDelete.setOnClickListener {
            val objectId = item.id
            val type = item.reportType
            val firestore = Firebase.firestore

            if (type == "Found") {
                MaterialAlertDialogBuilder(activity, com.google.android.material.R.style.AlertDialog_AppCompat)
                    .setTitle("Confirmación")
                    .setMessage("¿Deseas marcar el objeto como devuelto?\nYa no sera visible para los demás usuarios.")
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Aceptar") { _, _ ->
                        firestore.collection("FoundObjects").document(objectId!!).delete().addOnSuccessListener {
                            if (item.imageUrl != null && item.imageUrl != "") {
                                storage.reference.child(item.imageUrl!!).delete()
                            }
                            activity.recreate()
                        }
                    }
                    .show()
            } else if (type == "Lost") {
                MaterialAlertDialogBuilder(activity, com.google.android.material.R.style.AlertDialog_AppCompat)
                    .setTitle("Confirmación")
                    .setMessage("¿Deseas marcar el objeto como devuelto?\nYa no sera visible para los demás usuarios.")
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Aceptar") { _, _ ->
                        firestore.collection("LostObjects").document(objectId!!).delete().addOnSuccessListener {
                            if (item.imageUrl != null && item.imageUrl != "") {
                                storage.reference.child(item.imageUrl!!).delete()
                            }
                            activity.recreate()
                        }
                    }
                    .show()
            }
        }
    }
}
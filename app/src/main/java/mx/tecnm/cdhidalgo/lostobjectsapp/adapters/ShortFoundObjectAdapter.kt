package mx.tecnm.cdhidalgo.lostobjectsapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass

class ShortFoundObjectAdapter (
    private val list: List<ObjectDataClass>,
    private val activity: Activity,
    private val onClickListener: (ObjectDataClass) -> Unit
) : RecyclerView.Adapter<ShortFoundObjectAdapter.FoundObjectViewHolder>() {
    class FoundObjectViewHolder (item: View) : RecyclerView.ViewHolder(item) {
        val title: MaterialTextView = item.findViewById(R.id.title_found_object)
        val image: ShapeableImageView = item.findViewById(R.id.image_found_object)
        val location: MaterialTextView = item.findViewById(R.id.location_found_object)
        val button: MaterialButton = item.findViewById(R.id.btn_found_object)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundObjectViewHolder {
        return FoundObjectViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.short_card_found_object, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FoundObjectViewHolder, position: Int) {
        val item = list[position]
        val imageUrl = item.imageUrl
        if (imageUrl != null && imageUrl != "") {
            val storage = Firebase.storage.reference.child(imageUrl)
            storage.downloadUrl.addOnSuccessListener {
                Glide.with(activity).load(it).into(holder.image)
            }
        } else {
            holder.image.setImageResource(R.drawable.default_object)
        }
        holder.title.text = item.title
        holder.location.text = StringBuilder().append("Encontrado en: ").append(item.location)
        holder.button.setOnClickListener { onClickListener.invoke(item) }
    }

}
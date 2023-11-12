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

class ShortLostObjectAdapter(
    private val list: List<ObjectDataClass>,
    private val activity: Activity,
    private val onClick: (ObjectDataClass) -> Unit
) : RecyclerView.Adapter<ShortLostObjectAdapter.LostObjectViewHolder>(){
    class LostObjectViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val username: MaterialTextView = item.findViewById(R.id.username_lost_object)
        val title: MaterialTextView = item.findViewById(R.id.title_lost_object)
        val image: ShapeableImageView = item.findViewById(R.id.image_lost_object)
        val description: MaterialTextView = item.findViewById(R.id.description_lost_object)
        val button: MaterialButton = item.findViewById(R.id.btn_lost_object)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostObjectViewHolder {
        return LostObjectViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.short_card_lost_object, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LostObjectViewHolder, position: Int) {
        val item = list[position]
        holder.username.text = item.username
        holder.title.text = buildString { append("Perdí: ").append(item.title) }
        val imageUrl = item.imageUrl
        if (imageUrl != null && imageUrl != "") {
            val storage = Firebase.storage.reference.child(imageUrl)
            storage.downloadUrl.addOnSuccessListener {
                Glide.with(activity).load(it).into(holder.image)
            }
        } else {
            holder.image.setImageResource(R.drawable.default_object)
        }
        holder.description.text = buildString { append("Descripción: ").append(item.description) }
        holder.button.setOnClickListener { onClick.invoke(item) }
    }
}
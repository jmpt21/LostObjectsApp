package mx.tecnm.cdhidalgo.lostobjectsapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.HomeActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.adapters.GeneralPostAdapter
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentLostObjectsBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass

class LostObjectsFragment : Fragment() {
    private var _binding : FragmentLostObjectsBinding? = null
    private var listLostObjects: List<ObjectDataClass> = listOf()
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        firestore = Firebase.firestore
        _binding = FragmentLostObjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            firestore.collection("LostObjects").orderBy("date", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    listLostObjects = it.toObjects(ObjectDataClass::class.java)
                    binding.recyclerViewAllLostObjects.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewAllLostObjects.adapter = GeneralPostAdapter(listLostObjects, requireActivity())
                }
        binding.fabReloadAllLostObject.setOnClickListener {
            firestore.collection("LostObjects").orderBy("date", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    listLostObjects = it.toObjects(ObjectDataClass::class.java)
                    binding.recyclerViewAllLostObjects.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewAllLostObjects.adapter = GeneralPostAdapter(listLostObjects, requireActivity())
                }
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
}
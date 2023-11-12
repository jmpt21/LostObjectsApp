package mx.tecnm.cdhidalgo.lostobjectsapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.adapters.ShortFoundObjectAdapter
import mx.tecnm.cdhidalgo.lostobjectsapp.adapters.ShortLostObjectAdapter
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentHomeBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.ObjectDataClass

class HomeFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewFoundObjects: RecyclerView
    private lateinit var recyclerViewLostObjects: RecyclerView
    private var listFoundObjects: List<ObjectDataClass> = listOf()
    private var listLostObjects: List<ObjectDataClass> = listOf()
    private lateinit var foundObjectsAdapter: ShortFoundObjectAdapter
    private lateinit var lostObjectsAdapter: ShortLostObjectAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        firestore = Firebase.firestore
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFoundObjects = binding.recyclerViewFoundObjects
        recyclerViewLostObjects = binding.recyclerViewLostObjects

        firestore.collection("FoundObjects").orderBy("date", Query.Direction.DESCENDING).limit(6).get()
            .addOnSuccessListener {
                listFoundObjects = it.toObjects(ObjectDataClass::class.java)
                foundObjectsAdapter = ShortFoundObjectAdapter(listFoundObjects, requireActivity()) {

                }
                recyclerViewFoundObjects.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                recyclerViewFoundObjects.adapter = foundObjectsAdapter
            }

        firestore.collection("LostObjects").orderBy("date", Query.Direction.DESCENDING).limit(6).get()
            .addOnSuccessListener {
                listLostObjects = it.toObjects(ObjectDataClass::class.java)
                lostObjectsAdapter = ShortLostObjectAdapter(listLostObjects, requireActivity()) {

                }
                recyclerViewLostObjects.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                recyclerViewLostObjects.adapter = lostObjectsAdapter
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
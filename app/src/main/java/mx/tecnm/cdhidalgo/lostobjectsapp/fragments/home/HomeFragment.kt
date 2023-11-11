package mx.tecnm.cdhidalgo.lostobjectsapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLostObjects.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_LostObjectsFragment)
        }
        binding.btnFoundObjects.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_FoundObjectsFragment)
        }
        binding.btnReportar.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_ReportFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package mx.tecnm.cdhidalgo.lostobjectsapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tecnm.cdhidalgo.lostobjectsapp.HomeActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.R
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentHomeBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.FragmentReportBinding

class ReportFragment : Fragment() {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
package com.example.wattup.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.wattup.R
import com.example.wattup.databinding.FragmentDashboardBinding
import com.example.wattup.utils.PreferenceManager

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val TAG = "onDashboardFragmentTest"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.e(TAG, "onCreateView: ", )
        
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButton2.setOnClickListener {}
        binding.imageButton3.setOnClickListener {}
        binding.imageButton4.setOnClickListener {}
        binding.imageButton.setOnClickListener {
            Toast.makeText(activity, "Turn off lights records", Toast.LENGTH_SHORT).show()

            val navController = findNavController()
            val navOptions = navOptions {
                popUpTo(R.id.navigation_dashboard) { inclusive = true }
            }

            navController.navigate(R.id.navigation_dashboard_turn_off, null, navOptions)
        }

        val username = context?.let { PreferenceManager.getUserName(it) }
        binding.heyName.text = username
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.wattup.ui.calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.wattup.CameraActivity
import com.example.wattup.R
import com.example.wattup.databinding.FragmentCalculatorBinding
import com.example.wattup.utils.PreferenceManager

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null

    private val TAG = "onCalculatorFragmentTest"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)

        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.e(TAG, "onCreateView: calculaotr", )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = context?.let { PreferenceManager.getUserName(it) }
        binding.helloTextName.text = "Hello $username!"

        setListener()
    }

    private fun setListener(){
        binding.imageViewCamera.setOnClickListener{
            navToCalculateResult()
        }
        binding.buttonCamera.setOnClickListener{
            val intent = Intent(
                requireActivity(),
                CameraActivity::class.java
            )

            startActivity(intent)}

    }

    private fun navToCalculateResult(){
        try {
            val navController = findNavController()
            val navOptions = navOptions {
                popUpTo(R.id.navigation_home) { inclusive = true }
            }
            navController.navigate(R.id.navigation_calculator_result, null, navOptions)
        }catch (e :Exception){
            Log.e(TAG, "onViewCreated: $e", )
        }
    }
}
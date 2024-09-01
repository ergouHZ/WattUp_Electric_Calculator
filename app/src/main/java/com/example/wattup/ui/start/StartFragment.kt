package com.example.wattup.ui.start

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.wattup.MainActivity
import com.example.wattup.R
import com.example.wattup.ui.start.start2.StartSecondFragment

class StartFragment : Fragment() {

    private val TAG = "TestStartFragment"

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setListener(view)

    }

    private fun setListener(view:View){
        val checkboxA: CheckBox = view.findViewById(R.id.checkbox_a)
        val checkboxB: CheckBox = view.findViewById(R.id.checkbox_b)
        val checkboxC: CheckBox = view.findViewById(R.id.checkbox_c)
        val checkboxD: CheckBox = view.findViewById(R.id.checkbox_d)
        val checkboxE: CheckBox = view.findViewById(R.id.checkbox_e)
        val btnProceed: Button = view.findViewById(R.id.btn_proceed)

        btnProceed.setOnClickListener {
            val selectedGoals = mutableListOf<String>()
            if (checkboxA.isChecked) selectedGoals.add(checkboxA.text.toString())
            if (checkboxB.isChecked) selectedGoals.add(checkboxB.text.toString())
            if (checkboxC.isChecked) selectedGoals.add(checkboxC.text.toString())
            if (checkboxD.isChecked) selectedGoals.add(checkboxD.text.toString())
            if (checkboxE.isChecked) selectedGoals.add(checkboxE.text.toString())

            if (selectedGoals.isNotEmpty()) {
                Toast.makeText(activity, "Selected: ${selectedGoals.joinToString()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Please select at least one option.", Toast.LENGTH_SHORT).show()
            }
            try {
                val navController = findNavController()
                navController.navigate(R.id.navigation_starter2)

            }catch (e :Exception){
                Log.e(TAG, "onViewCreated: $e", )
            }
        }
    }




}
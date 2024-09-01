package com.example.wattup.ui.start.start2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.wattup.MainActivity
import com.example.wattup.R
import com.example.wattup.databinding.FragmentHomeBinding
import com.example.wattup.databinding.FragmentStartSecondBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartSecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartSecondFragment : Fragment() {
    private val TAG = "StartSecondFragment"

    private var _binding: FragmentStartSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener(view)
    }

    private fun setListener(view: View){
        val checkboxA: CheckBox = view.findViewById(R.id.checkbox_a)
        val checkboxB: CheckBox = view.findViewById(R.id.checkbox_b)
        val checkboxC: CheckBox = view.findViewById(R.id.checkbox_c)
        val checkboxD: CheckBox = view.findViewById(R.id.checkbox_d)
        val checkboxE: CheckBox = view.findViewById(R.id.checkbox_e)

        val checkboxA2: CheckBox = view.findViewById(R.id.checkbox_a2)
        val checkboxB2: CheckBox = view.findViewById(R.id.checkbox_b2)
        val checkboxC2: CheckBox = view.findViewById(R.id.checkbox_c2)
        val checkboxD2: CheckBox = view.findViewById(R.id.checkbox_d2)
        val checkboxE2: CheckBox = view.findViewById(R.id.checkbox_e2)

        val btnProceed: Button = view.findViewById(R.id.btn_proceed)

        btnProceed.setOnClickListener {
            val selectedGoals = mutableListOf<String>()
            if (checkboxA.isChecked) selectedGoals.add(checkboxA.text.toString())
            if (checkboxB.isChecked) selectedGoals.add(checkboxB.text.toString())
            if (checkboxC.isChecked) selectedGoals.add(checkboxC.text.toString())
            if (checkboxD.isChecked) selectedGoals.add(checkboxD.text.toString())
            if (checkboxE.isChecked) selectedGoals.add(checkboxE.text.toString())

            if (checkboxA2.isChecked) selectedGoals.add(checkboxA.text.toString())
            if (checkboxB2.isChecked) selectedGoals.add(checkboxB.text.toString())
            if (checkboxC2.isChecked) selectedGoals.add(checkboxC.text.toString())
            if (checkboxD2.isChecked) selectedGoals.add(checkboxD.text.toString())
            if (checkboxE2.isChecked) selectedGoals.add(checkboxE.text.toString())

            if (selectedGoals.isNotEmpty()) {
                Toast.makeText(activity, "Selected: ${selectedGoals.joinToString()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Please select at least one option.", Toast.LENGTH_SHORT).show()
            }

            try {
                val navController = findNavController()
                navController.navigate(R.id.navigation_register)
            }catch (e :Exception){
                Log.e(TAG, "onViewCreated: $e", )
            }
//            context?.let { it1 -> PreferenceManager.setInitialSetupComplete(it1) }

//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish() // finish activity to prevent user back action to this activity
        }
    }

}
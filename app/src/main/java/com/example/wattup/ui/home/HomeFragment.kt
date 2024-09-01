package com.example.wattup.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.wattup.R
import com.example.wattup.databinding.FragmentHomeBinding
import com.example.wattup.ui.calculator.CalculatorFragment
import com.example.wattup.ui.profile.ProfileFragment
import com.example.wattup.utils.PreferenceManager

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.name.observe(viewLifecycleOwner) { newName ->
            binding.userName.text = newName
        }


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        binding.userName.text = username

        setListener()
    }

    private fun setListener(){
        binding.calculateCard.setOnClickListener{
            try {
                val navController = findNavController()
                val navOptions = navOptions {
                    popUpTo(R.id.navigation_home) { inclusive = true }
                }

                navController.navigate(R.id.navigation_calculator, null, navOptions)
            }catch (e :Exception){
                Log.e(TAG, "on navigate: $e", )
            }
        }
    }

    private fun navigateToSubFragment() {
        val fragment = CalculatorFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        transaction.addToBackStack(null)  // 将副Fragment添加到返回栈
        transaction.commit()
    }

}
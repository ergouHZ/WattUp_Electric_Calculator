package com.example.wattup.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.wattup.R
import com.example.wattup.databinding.FragmentRegisterBinding
import com.example.wattup.utils.PreferenceManager
import com.example.wattup.utils.StringMod

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = RegisterFragment()
        private val TAG = "RegisterFragment::class.java."
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener(view)
    }

    private fun setListener(view: View){
        val registerBtn :Button= binding.btnCreateAccount
        registerBtn.setOnClickListener {
            var username:String = binding.etUsername.text.toString()

            //username can't not be null
            if (username == ""){
//                binding.etUsername.error = "Username cannot be empty."
//                return@setOnClickListener
                username = "youth Pulse"
            }

//            if (!username.contains('@')){
//                binding.etUsername.error = "Invalid email format."
//                return@setOnClickListener
//            }

            try {

                context?.let { it1 -> PreferenceManager.setInitialSetupComplete(it1) }
                context?.let { it1 -> PreferenceManager.setUserName(it1,StringMod.cutAfterEmail(username)) }
            }catch (e :Exception){
                Log.e(TAG, "onPreferencemanagerSet: $e", )
            }

            try {
                val navController = findNavController()
                navController.navigate(R.id.navigation_home)
            }catch (e :Exception){
                Log.e(TAG, "onViewCreated: $e", )
            }
        }
    }

}
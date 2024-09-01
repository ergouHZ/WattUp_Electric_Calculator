package com.example.wattup.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.wattup.R
import com.example.wattup.databinding.FragmentProfileBinding
import com.example.wattup.ui.home.HomeViewModel
import com.example.wattup.ui.register.RegisterFragment
import com.example.wattup.utils.PreferenceManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    companion object {
        private val TAG = "ProfileFragment"
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set username on the page
        val username = context?.let { PreferenceManager.getUserName(it) }
        binding.userName.text = username
        binding.username.text.clear()
        binding.username.hint = username
        binding.signOut.setOnClickListener {
            context?.let { it1 -> PreferenceManager.renewInitialSetup(it1) }
            try {
                val navController = findNavController()
                navController.navigate(R.id.navigation_starter)
            }catch (e :Exception){
                Log.e(TAG, "on navigate: $e", )
            }
        }
    }
}
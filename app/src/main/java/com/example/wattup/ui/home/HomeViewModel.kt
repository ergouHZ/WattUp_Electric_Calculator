package com.example.wattup.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wattup.utils.PreferenceManager

class HomeViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name
    //updating the username
    init {
        // Load the name from SharedPreferences

    }

    private fun loadNameFromPreferences(username: String) {
        _name.value = username
    }


}
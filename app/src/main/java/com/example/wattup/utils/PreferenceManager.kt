package com.example.wattup.utils

import android.content.Context

object PreferenceManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_INITIAL_SETUP_COMPLETE = "initial_setup_complete"
    private const val USER_NAME = "user_name"

    //if the user has finished QA startup session
    fun isInitialSetupComplete(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_INITIAL_SETUP_COMPLETE, false)
    }

    //set the user has finished QA startup session, call this function
    fun setInitialSetupComplete(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_INITIAL_SETUP_COMPLETE, true)
            apply()
        }
    }

    fun renewInitialSetup(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_INITIAL_SETUP_COMPLETE, false)
            apply()
        }
    }

    //get the user name and set
    fun setUserName(context: Context, userName: String){
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString(USER_NAME, userName)
            apply()
        }
    }

    fun getUserName(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_NAME, "")?: "name"
    }
}
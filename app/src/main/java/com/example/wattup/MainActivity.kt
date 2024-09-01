package com.example.wattup


import android.R.attr
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wattup.databinding.ActivityMainBinding
import com.example.wattup.utils.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity test"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_tips,
                R.id.navigation_calculator,
                R.id.navigation_profile,
                R.id.navigation_starter,
                R.id.navigation_starter2,
                R.id.navigation_register
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //if this is the first time, then hide the action bar, and nav to the initial session
        if (!PreferenceManager.isInitialSetupComplete(this)) {
            try {
                navController.navigate(R.id.navigation_starter)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: $e")
            }
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_starter,
                R.id.navigation_starter2,
                R.id.navigation_register -> {
                    binding.navView.visibility = View.INVISIBLE  // 隐藏底部导航栏
                }

                else -> {
                    binding.navView.visibility = View.VISIBLE // 显示底部导航栏
                }
            }
        }
    }

    private fun <T : Fragment> isFragmentInContainer(fragmentClass: Class<T>): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        return fragment?.javaClass == fragmentClass
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: $resultCode")
        // 检查请求代码及结果代码
        if (resultCode == RESULT_OK) {
            // 从 Intent 中获取结果
            val result: String? = data?.getStringExtra("result")


            Log.d(TAG, "onActivityResult: $result")
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        // 获取从 UploadActivity 传递过来的数据
        val result = intent.getStringExtra("result")


        if (result != null) {
            Toast.makeText(this, "$result ", Toast.LENGTH_LONG).show()

            try {
                val dialog = Dialog(this).apply {
                    setContentView(R.layout.layout_dialog) // 设置自定义布局
                    setCancelable(true)
                    show() // 显示 Dialog
                }

                val textView = dialog.findViewById<TextView>(R.id.dialog_message)
                textView.text = result.toString()

                val buttonClose = dialog.findViewById<Button>(R.id.dialog_button)
                buttonClose.setOnClickListener {
                    dialog.dismiss()
                }
                // 延迟时间后自动关闭 Dialog
//                Handler(Looper.getMainLooper()).postDelayed({
//                    dialog.dismiss() // 关闭 Dialog
//                }, 10000) // 设置显示时间，例如 10 秒
            }catch(e: Exception){
                Log.e(TAG, "on Dialog: $e", )
            }
        } else {
            Log.d(TAG, "No result received")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

}
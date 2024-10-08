package com.example.wattup_yp.ui.dashboard.dashboardlight

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wattup.R

class DashboardLightFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardLightFragment()
    }

    private lateinit var viewModel: DashboardLightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_light, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardLightViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
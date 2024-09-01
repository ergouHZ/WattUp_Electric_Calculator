package com.example.wattup.ui.dashboard.dashboardcalender

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wattup.R

class DashboardCalenderFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardCalenderFragment()
    }

    private lateinit var viewModel: DashboardCalenderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_calender, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardCalenderViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
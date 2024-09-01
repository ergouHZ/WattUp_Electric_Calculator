package com.example.wattup.ui.calculator.calculatorresult

import ApplianceAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wattup.CameraActivity
import com.example.wattup.R
import com.example.wattup.utils.ApplianceSelectionItem
import com.example.wattup_yp.ui.calculator.calculatorresult.CalculatorResultViewModel


class CalculatorResultFragment : Fragment() {
    private val TAG = "Test CalculatorResultFragment::class.java"

    private lateinit var recyclerView: RecyclerView
    private lateinit var applianceAdapter: ApplianceAdapter
    private var selectionItems = mutableListOf<ApplianceSelectionItem>()

    companion object {
        fun newInstance() = CalculatorResultFragment()
    }

    private lateinit var viewModel: CalculatorResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculatorResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            recyclerView =
                view.findViewById(R.id.recycler_view) // Make sure you have a recycler view in your layout
            recyclerView.layoutManager = LinearLayoutManager(this.activity)

            // Initialize adapter and set to RecyclerView
            applianceAdapter =
                ApplianceAdapter(
                    requireContext(),
                    selectionItems
                ) { position -> removeSelectionItem(position) }

            recyclerView.adapter = applianceAdapter

            recyclerView.findViewById<Button>(R.id.imageView_camera).setOnClickListener{
                val intent = Intent(
                    requireActivity(),
                    CameraActivity::class.java
                )

                startActivity(intent)
            }

        } catch (e: Exception) {
            Log.e(TAG, "on Recycler Created: $e")
        }

        // Set up “add” button
        view.findViewById<Button>(R.id.button_add_appliances).setOnClickListener {
            addSelectionItem()
        }

        val configureButton: Button = view.findViewById<Button>(R.id.configureCostsBtn)
        try {
            configureButton.setOnClickListener {
                // TODO: Implement cost configuration
                val intent = Intent(
                    requireActivity(),
                    CameraActivity::class.java
                )

                startActivity(intent)

            }

        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated: $e")
        }

    }

    private fun addSelectionItem() {
        try {
            selectionItems.add(ApplianceSelectionItem("Category", "Appliance"))
            applianceAdapter.notifyItemInserted(selectionItems.size - 1)
            recyclerView.scrollToPosition(selectionItems.size - 1) // Scroll to the new item
        } catch (e: Exception) {
            Log.e(TAG, "addSelectionItem: ")
        }
    }

    private fun removeSelectionItem(position: Int) {
        selectionItems.removeAt(position)
        applianceAdapter.notifyItemRemoved(position)
    }
}
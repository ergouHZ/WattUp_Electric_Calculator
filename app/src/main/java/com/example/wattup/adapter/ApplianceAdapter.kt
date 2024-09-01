import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wattup.MainActivity
import com.example.wattup.R
import com.example.wattup.utils.Appliance
import com.example.wattup.utils.ApplianceSelectionItem
import com.example.wattup.utils.ElectricityUsageEstimator


class ApplianceAdapter(
    private val context: Context,
    private var items: MutableList<ApplianceSelectionItem>,
    private val onRemoveClick: (Int) -> Unit // lambda, to handle item removal
) : RecyclerView.Adapter<ApplianceAdapter.ViewHolder>() {
    private val TAG = "RecyclerViewAdapter test"

    private val applianceCategories = mapOf(
        "Kitchen" to listOf(
            ElectricityUsageEstimator.KitchenAppliance.REFRIGERATOR.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.MICROWAVE_OVEN.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.RICE_COOKER.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.INDUCTION_COOKER.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.KETTLE.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.SOYBEAN_MILK_MAKER.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.JUICER.appliance.name,
            ElectricityUsageEstimator.KitchenAppliance.BREAD_MAKER.appliance.name
        ),
        "Living Room" to listOf(
            ElectricityUsageEstimator.LivingRoomAppliance.TV.appliance.name,
            ElectricityUsageEstimator.LivingRoomAppliance.AIR_CONDITIONER.appliance.name,
            ElectricityUsageEstimator.LivingRoomAppliance.FAN.appliance.name,
            ElectricityUsageEstimator.LivingRoomAppliance.SPEAKER.appliance.name,
            ElectricityUsageEstimator.LivingRoomAppliance.SET_TOP_BOX.appliance.name
        ),
        "Bedroom" to listOf(
            ElectricityUsageEstimator.BedroomAppliance.TABLE_LAMP.appliance.name,
            ElectricityUsageEstimator.BedroomAppliance.HEATER.appliance.name,
            ElectricityUsageEstimator.BedroomAppliance.HUMIDIFIER.appliance.name,
            ElectricityUsageEstimator.BedroomAppliance.ELECTRIC_BLANKET.appliance.name
        ),
        "Bathroom" to listOf(
            ElectricityUsageEstimator.BathroomAppliance.WATER_HEATER.appliance.name,
            ElectricityUsageEstimator.BathroomAppliance.HAIR_DRYER.appliance.name,
            ElectricityUsageEstimator.BathroomAppliance.ELECTRIC_TOOTHBRUSH.appliance.name,
            ElectricityUsageEstimator.BathroomAppliance.WASHING_MACHINE.appliance.name // Note, also used in cleaning section.
        ),
        "Cleaning" to listOf(
            ElectricityUsageEstimator.CleaningAppliance.VACUUM_CLEANER.appliance.name,
            ElectricityUsageEstimator.CleaningAppliance.ROBOT_VACUUM.appliance.name,
            ElectricityUsageEstimator.CleaningAppliance.DEHUMIDIFIER.appliance.name,
            ElectricityUsageEstimator.CleaningAppliance.AIR_PURIFIER.appliance.name
        ),
        "Other" to listOf(
            ElectricityUsageEstimator.OtherAppliance.PHONE.appliance.name,
            ElectricityUsageEstimator.OtherAppliance.COMPUTER.appliance.name,
            ElectricityUsageEstimator.OtherAppliance.PHONE_CHARGER.appliance.name,
            ElectricityUsageEstimator.OtherAppliance.IRON.appliance.name,
            ElectricityUsageEstimator.OtherAppliance.ELECTRIC_MOSQUITO_REPELLENT.appliance.name
        ),
        "Category" to listOf(
            "Appliance"
        )
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val spinnerCategory: Spinner = itemView.findViewById(R.id.spinner_category)
        val spinnerAppliance: Spinner = itemView.findViewById(R.id.spinner_appliance)
        val buttonRemove: ImageView = itemView.findViewById(R.id.imageView_remove_application)
        val photo: ImageView = itemView.findViewById(R.id.imageView_take_photo)
        val orderText: TextView = itemView.findViewById(R.id.order)
        val modifyBtn : Button = itemView.findViewById(R.id.modify_calc)

        val load: EditText = itemView.findViewById(R.id.app_load)
        val hoursDaily: EditText = itemView.findViewById(R.id.app_daily_hours)
        val quantity: EditText = itemView.findViewById(R.id.app_number)
        val days: EditText = itemView.findViewById(R.id.app_days)
        val monthlyUsage: TextView = itemView.findViewById(R.id.app_monthly_usage)
        val monthlyCost: TextView = itemView.findViewById(R.id.app_monthly_fee)
        val weeklyUsage: TextView = itemView.findViewById(R.id.app_weekly_usage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item: View =
            layoutInflater.inflate(R.layout.appliance_adapter_layout_cus, parent, false)
        return ViewHolder(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderText.text = (position + 1).toString()
        try {
            val categoryAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                applianceCategories.keys.toList()
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            holder.spinnerCategory.adapter = categoryAdapter

            val currentItem = items[position]

            // 确保 category 不为 null
            val categoryPosition = categoryAdapter.getPosition(currentItem.category ?: "")
            holder.spinnerCategory.setSelection(categoryPosition)

            //update the sub spinner
            updateApplianceSpinner(holder, holder.spinnerAppliance, currentItem.category ?: "")

            holder.spinnerCategory.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val selectedCategory = parent.getItemAtPosition(position) as? String
                        if (selectedCategory != null) {
                            updateApplianceSpinner(
                                holder,
                                holder.spinnerAppliance,
                                selectedCategory
                            )

                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

            holder.buttonRemove.setOnClickListener {
                onRemoveClick(position)

                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "item removed ")
            }

            holder.photo.setOnClickListener {


                Toast.makeText(context, "Take photo", Toast.LENGTH_SHORT).show()

            }

            holder.modifyBtn.setOnClickListener {
                modifyCalculations(holder)
            }

        } catch (e: Exception) {
            Log.e("RecyclerViewAdapter", "Error in onBindViewHolder: ${e.message}", e)
        }


    }

    //according to the selected category and appliance to do the calculation
    // calculate everytime when slecet the spinner
    private fun calculateAppliances(holder: ViewHolder, appliance: Appliance) {
        try {
            holder.hoursDaily.hint = appliance.hours.toString()
            holder.load.hint = appliance.power.toString()
            holder.quantity.hint = appliance.quantity.toString()
            holder.days.hint = appliance.days.toString()

            val estimator: ElectricityUsageEstimator = ElectricityUsageEstimator()
            estimator.addAppliance(appliance)
            val result = estimator.estimateUsage( "summer")

            holder.monthlyUsage.text = result["monthly usage"].toString()
            holder.weeklyUsage.text = result["weekly usage"].toString()
            holder.monthlyCost.text = result["monthly cost"].toString()
        } catch (e: Exception) {
            Log.e(TAG, "calculateAppliances: ")
        }
//
//        "monthly usage" to monthlyUsage.round(2),
//        "weekly usage" to weeklyUsage.round(2),
//        "monthly cost" to monthlyCost.round(2)
    }

    private fun modifyCalculations(holder: ViewHolder){
        val load = holder.hoursDaily.text?.toString()?.toFloatOrNull()?:holder.hoursDaily.hint.toString().toFloatOrNull()
        val days = holder.days.text?.toString()?.toFloatOrNull()?:holder.days.hint.toString().toFloatOrNull()
        val quantity = holder.quantity.text?.toString()?.toFloatOrNull()?:holder.quantity.hint.toString().toFloatOrNull()
        val hoursDaily = holder.hoursDaily.text?.toString()?.toFloatOrNull()?: holder.hoursDaily.hint.toString().toFloatOrNull()

        Log.d(TAG, "load: ${load}, days: ${days}, quantity: ${quantity}, hours:$hoursDaily")
        if(load != null && days != null && quantity != null&& hoursDaily != null) {
            val monthlyUsage = load * hoursDaily * quantity * days / 1000
            Toast.makeText(context, "result $monthlyUsage", Toast.LENGTH_SHORT).show()
        }else{
            Log.w(TAG, "modifyCalculations: load, days, quantity or hoursDaily is null")
            Toast.makeText(context, "Please input right number", Toast.LENGTH_SHORT).show()
        }
    }

    //update the sub spinner according to the category
    private fun updateApplianceSpinner(holder: ViewHolder, spinner: Spinner, category: String) {
//        val appliances = applianceCategories[category] ?: emptyList()
//        val applianceNames = appliances.map { it.name } // get the names


        val applianceNames = applianceCategories[category] ?: emptyList()
        val applianceAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, applianceNames)
        applianceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = applianceAdapter
        try {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (view != null) {
                        val selectedAppliance = parent.getItemAtPosition(position)
                        if (selectedAppliance != null) {
                            val estimator: ElectricityUsageEstimator = ElectricityUsageEstimator()
                            val appliance =
                                estimator.findAppliance(category, selectedAppliance.toString())

                            if (appliance != null) {
                                calculateAppliances(holder, appliance)
                            }
                        }
                    } else {
                        Log.w(TAG, "onItemSelected: view is null")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateApplianceSpinner: ")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

//    return all the cost that added
    fun getTotalCost():Double?{
        var totalCost = 0f
        val estimator: ElectricityUsageEstimator = ElectricityUsageEstimator()
        for(item in items){

            val appliance = estimator.findAppliance(item.category, item.appliance)
            if(appliance!= null){
                estimator.addAppliance(appliance)
            }
        }
        return estimator.estimateUsage("summer")["monthly cost"]
    }
}
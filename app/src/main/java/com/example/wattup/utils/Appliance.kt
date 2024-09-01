package com.example.wattup.utils

import org.jetbrains.annotations.TestOnly
import java.util.Locale

data class Appliance(
    val name: String,
    val power: Double, // Power, unit: watts
    val hours: Double, // Daily usage hours, unit: hours
    val quantity: Int = 1, // Number of devices, default is 1
    val days: Int = 30 // Estimated usage days in a month
)

class ElectricityUsageEstimator {
    private val appliances = mutableListOf<Appliance>()

    // Preset some common appliances
    init {
//        appliances.addAll(listOf(
//            Appliance("Refrigerator", 150.0, 24.0, 1, 30),
//            Appliance("TV", 100.0, 4.0, 1, 30),
//            Appliance("Washing Machine", 500.0, 1.0, 2, 8), // Estimated 8 days of use in a month
//            Appliance("Air Conditioner", 1500.0, 6.0, 1, 25), // Estimated 25 days of use in a month
//            Appliance("Computer", 200.0, 5.0, 2, 20), // Estimated 20 days of use in a month
//            Appliance("Lighting", 60.0, 5.0, 5, 30) // Likely on every day
//        ))
    }

    // Enum class: Kitchen Appliances
    enum class KitchenAppliance(val appliance: Appliance) {
        REFRIGERATOR(Appliance("Refrigerator", 150.0, 24.0, 1, 30)),
        MICROWAVE_OVEN(Appliance("Microwave Oven", 1200.0, 0.5, 1, 15)), // Example usage for 15 days
        RICE_COOKER(Appliance("Rice Cooker", 400.0, 1.0, 1, 10)), // Example usage for 10 days
        INDUCTION_COOKER(Appliance("Induction Cooker", 1800.0, 1.5, 1, 10)),
        KETTLE(Appliance("Electric Kettle", 1500.0, 0.3, 1, 5)), // Example usage for 5 days
        SOYBEAN_MILK_MAKER(Appliance("Soybean Milk Maker", 800.0, 0.5, 1, 10)),
        JUICER(Appliance("Juicer", 400.0, 0.3, 1, 8)),
        BREAD_MAKER(Appliance("Bread Maker", 600.0, 2.0, 1, 6))
    }

    // Enum class: Living Room Appliances
    enum class LivingRoomAppliance(val appliance: Appliance) {
        TV(Appliance("TV", 100.0, 4.0, 1, 30)),
        AIR_CONDITIONER(Appliance("Air Conditioner", 1200.0, 6.0, 1, 25)),
        FAN(Appliance("Fan", 50.0, 8.0, 1, 20)),
        SPEAKER(Appliance("Speaker", 100.0, 2.0, 1, 15)),
        SET_TOP_BOX(Appliance("Set-Top Box", 20.0, 8.0, 1, 20))
    }

    // Enum class: Bedroom Appliances
    enum class BedroomAppliance(val appliance: Appliance) {
        TABLE_LAMP(Appliance("Table Lamp", 40.0, 4.0, 1, 30)),
        HEATER(Appliance("Heater", 1500.0, 4.0, 1, 15)),
        HUMIDIFIER(Appliance("Humidifier", 25.0, 8.0, 1, 20)),
        ELECTRIC_BLANKET(Appliance("Electric Blanket", 60.0, 8.0, 1, 20))
    }

    // Enum class: Bathroom Appliances
    enum class BathroomAppliance(val appliance: Appliance) {
        WATER_HEATER(Appliance("Water Heater", 3000.0, 1.0, 1, 30)),
        HAIR_DRYER(Appliance("Hair Dryer", 1000.0, 0.3, 1, 8)),
        ELECTRIC_TOOTHBRUSH(Appliance("Electric Toothbrush", 5.0, 0.2, 1, 30)),
        WASHING_MACHINE(Appliance("Washing Machine", 500.0, 1.0, 2, 8))
    }

    // Enum class: Cleaning Appliances
    enum class CleaningAppliance(val appliance: Appliance) {
        VACUUM_CLEANER(Appliance("Vacuum Cleaner", 1000.0, 1.0, 1, 4)),
        ROBOT_VACUUM(Appliance("Robot Vacuum", 30.0, 2.0, 1, 15)),
        DEHUMIDIFIER(Appliance("Dehumidifier", 300.0, 8.0, 1, 20)),
        AIR_PURIFIER(Appliance("Air Purifier", 50.0, 24.0, 1, 30))
    }

    // Enum class: Other Common Appliances
    enum class OtherAppliance(val appliance: Appliance) {
        PHONE(Appliance("Phone", 20.0, 4.0, 1, 30)),
        COMPUTER(Appliance("Computer/Laptop", 200.0, 5.0, 2, 20)),
        PHONE_CHARGER(Appliance("Phone Charger", 10.0, 4.0, 4, 30)),
        IRON(Appliance("Iron", 1200.0, 0.5, 1, 5)),
        ELECTRIC_MOSQUITO_REPELLENT(Appliance("Electric Mosquito Repellent", 5.0, 8.0, 2, 30))
    }

    //find the certain class
    fun findAppliance(category: String, applianceName: String): Appliance? {
        return when (category.toLowerCase(Locale.ROOT)) {
            "kitchen" -> KitchenAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            "living room" -> LivingRoomAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            "bedroom" -> BedroomAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            "bathroom" -> BathroomAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            "cleaning" -> CleaningAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            "other" -> OtherAppliance.entries.find { it.appliance.name.equals(applianceName, ignoreCase = true) }?.appliance
            else -> null
        }
    }


    fun addAppliance(appliance: Appliance) {
        appliances.add(appliance)
    }

    fun estimateUsageExtra(people: Int, area: Double, season: String): Map<String, Double> {
        val baseUsage = people * 50.0 // kWh
        val spaceUsage = area * 1.0 // kWh

        val seasonFactor = when (season.toLowerCase()) {
            "summer" -> 0.9
            "winter" -> 1.1
            else -> 1.0
        }

        val applianceUsage = appliances.sumByDouble {
            it.power * it.hours * it.quantity * it.days / 1000
        }

        val monthlyUsage = (baseUsage + spaceUsage + applianceUsage) * seasonFactor
        val weeklyUsage = monthlyUsage / 4.2
        val monthlyCost = monthlyUsage * 0.38

        return mapOf(
            "monthly usage" to monthlyUsage.round(2),
            "weekly usage" to weeklyUsage.round(2),
            "monthly cost" to monthlyCost.round(2)
        )
    }

    fun estimateUsage(season: String): Map<String, Double> {

        val seasonFactor = when (season.toLowerCase()) {
            "summer" -> 0.9
            "winter" -> 1.1
            else -> 1.0
        }

        val applianceUsage = appliances.sumByDouble {
            it.power * it.hours * it.quantity * it.days / 1000
        }

        val monthlyUsage = applianceUsage * seasonFactor
        val weeklyUsage = monthlyUsage / 4.2
        val monthlyCost = monthlyUsage * 0.38

        return mapOf(
            "monthly usage" to monthlyUsage.round(2),
            "weekly usage" to weeklyUsage.round(2),
            "monthly cost" to monthlyCost.round(2)
        )
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}

@TestOnly
fun main() {
    val estimator = ElectricityUsageEstimator()

    // User input
    print("Enter number of people in the household: ")
    val people = readLine()?.toIntOrNull() ?: 3
    print("Enter the area of the house (square meters): ")
    val area = readLine()?.toDoubleOrNull() ?: 100.0
    print("Enter the season (summer/winter/spring_autumn): ")
    val season = readLine() ?: "spring_autumn"

    // Optional: Add custom appliance
    estimator.addAppliance(Appliance("Water Heater", 2000.0, 2.0))

    val result = estimator.estimateUsageExtra(people, area, season)

    println("Estimated monthly electricity usage: ${result["monthly"]} kWh")
    println("Estimated weekly electricity usage: ${result["weekly"]} kWh")
    println("Estimated daily electricity usage: ${result["daily"]} kWh")
}

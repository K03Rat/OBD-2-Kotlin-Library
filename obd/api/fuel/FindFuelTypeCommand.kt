package com.obd.api.fuel

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
import com.obd.enums.FuelType
/**
 * Finds the fuel type used by the vehicle.
 *
 * PID: 01 51
 * Response Example: 41 51 04 → 04 = Diesel
 */
class FindFuelTypeCommand : ObdCommand {

    private var fuelType: Int = 0

    constructor() : super("01 51")

    constructor(other: FindFuelTypeCommand) : super(other)

    override fun performCalculations() {
        fuelType = buffer[2].toInt()
    }

    override fun getFormattedResult(): String {
        return try {
            FuelType.fromValue(fuelType).toString()
        } catch (e: Exception) {
            "-"
        }
    }

    override fun getCalculatedResult(): String = fuelType.toString()

    override fun getName(): String = AvailableCommandNames.FUEL_TYPE.value
}
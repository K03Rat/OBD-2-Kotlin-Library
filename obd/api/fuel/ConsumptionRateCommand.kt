package com.obd.api.fuel

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Fuel Consumption Rate Command
 * PID: 01 5E
 *
 * Formula: Fuel Rate = ((A * 256) + B) * 0.05
 * Unit: Liters per hour (L/h)
 */
class ConsumptionRateCommand : ObdCommand {

    private var fuelRate: Float = -1.0f

    constructor() : super("01 5E")

    constructor(other: ConsumptionRateCommand) : super(other)

    override fun performCalculations() {
        fuelRate = ((buffer[2] * 256) + buffer[3]) * 0.05f
    }

    override fun getFormattedResult(): String = "%.1f%s".format(fuelRate, getResultUnit())

    override fun getCalculatedResult(): String = fuelRate.toString()

    override fun getResultUnit(): String = "L/h"

    fun getLitersPerHour(): Float = fuelRate

    override fun getName(): String = AvailableCommandNames.FUEL_CONSUMPTION_RATE.value
}
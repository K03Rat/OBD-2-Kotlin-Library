package com.obd.api.fuel

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the current fuel level percentage from the vehicle.
 *
 * PID: 01 2F
 * Formula: (A * 100) / 255
 */
class FuelLevelCommand : PercentageObdCommand {

    constructor() : super("01 2F")

    constructor(other: FuelLevelCommand) : super(other)

    override fun performCalculations() {
        val A = buffer[2].toFloat()
        percentage = (A * 100f) / 255f
    }

    override fun getName(): String = AvailableCommandNames.FUEL_LEVEL.value

    fun getFuelLevel(): Float = percentage
}
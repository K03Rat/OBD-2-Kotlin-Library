package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.commands.SystemOfUnits
import com.obd.enums.AvailableCommandNames
/**
 * Reads the current vehicle speed.
 *
 * OBD-II Mode 01 PID 0D
 */
class VehicleSpeedCommand : ObdCommand, SystemOfUnits {

    private var speedKmh: Int = 0

    constructor() : super("01 0D")
    constructor(other: VehicleSpeedCommand) : super(other)

    override fun performCalculations() {
        speedKmh = buffer[2].toInt() and 0xFF
    }

    override fun getFormattedResult(): String {
        return if (useImperialUnits) {
            String.format("%.1f %s", getImperialUnit(), getResultUnit())
        } else {
            "$speedKmh ${getResultUnit()}"
        }
    }

    override fun getCalculatedResult(): String = speedKmh.toString()

    override fun getResultUnit(): String = if (useImperialUnits) "mph" else "km/h"

    override fun getImperialUnit(): Float = speedKmh * 0.621371f

    fun getKmH(): Int = speedKmh

    override fun getName(): String = AvailableCommandNames.VEHICLE_SPEED.value
}
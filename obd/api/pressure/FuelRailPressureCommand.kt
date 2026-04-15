package com.obd.api.pressure

import com.obd.commands.PressureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the fuel rail pressure from the vehicle.
 *
 * PID: 01 23
 * Formula: Pressure (kPa) = ((A * 256) + B) * 10
 */
class FuelRailPressureCommand : PressureCommand {

    constructor() : super("01 23")

    constructor(other: FuelRailPressureCommand) : super(other)

    override fun preparePressureValue(): Int {
        val a = buffer[2].toInt()
        val b = buffer[3].toInt()
        return ((a * 256) + b) * 10
    }

    override fun getName(): String = AvailableCommandNames.FUEL_RAIL_PRESSURE.value
}
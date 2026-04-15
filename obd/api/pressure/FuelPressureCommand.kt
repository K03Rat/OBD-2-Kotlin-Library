package com.obd.api.pressure

import com.obd.commands.PressureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the fuel pressure from the vehicle.
 *
 * PID: 01 0A
 * Formula: Pressure (kPa) = A × 3
 */
class FuelPressureCommand : PressureCommand {

    constructor() : super("01 0A")

    constructor(other: FuelPressureCommand) : super(other)

    override fun preparePressureValue(): Int = buffer[2] * 3

    override fun getName(): String = AvailableCommandNames.FUEL_PRESSURE.value
}
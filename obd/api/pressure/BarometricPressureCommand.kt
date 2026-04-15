package com.obd.api.pressure

import com.obd.commands.PressureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the barometric pressure from the vehicle.
 *
 * PID: 01 33
 * Unit: kPa
 */
class BarometricPressureCommand : PressureCommand {

    constructor() : super("01 33")

    constructor(other: BarometricPressureCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.BAROMETRIC_PRESSURE.value
}
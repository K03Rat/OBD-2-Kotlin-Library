package com.obd.api.engine

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the driver's demand engine torque as a percentage.
 *
 * PID: 01 61
 * Unit: %
 */
class DriversDemandTorqueCommand : PercentageObdCommand {

    constructor() : super("01 61")

    constructor(other: DriversDemandTorqueCommand) : super(other)

    override fun performCalculations() {
        percentage = (buffer[2] - 125).toFloat()
    }

    override fun getName(): String = AvailableCommandNames.DRIVERS_DEMAND_TORQUE.value
}
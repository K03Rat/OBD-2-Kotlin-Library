package com.obd.api.engine

import com.obd.commands.TemperatureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the intake air temperature.
 *
 * PID: 01 0F
 * Unit: °C
 */
class IntakeAirTemperatureCommand : TemperatureCommand {

    constructor() : super("01 0F")

    constructor(other: IntakeAirTemperatureCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.INTAKE_AIR_TEMP.value
}
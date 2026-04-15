package com.obd.api.temperature

import com.obd.commands.TemperatureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the ambient (outside) air temperature.
 *
 * PID: 01 46
 * Formula: Temperature (°C) = A - 40
 */
class AmbientAirTemperatureCommand : TemperatureCommand {

    constructor() : super("01 46")

    constructor(other: AmbientAirTemperatureCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.AMBIENT_AIR_TEMP.value
}
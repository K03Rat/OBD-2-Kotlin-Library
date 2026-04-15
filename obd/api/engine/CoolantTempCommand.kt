package com.obd.api.engine

import com.obd.commands.TemperatureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Measures the engine coolant temperature.
 *
 * PID: 01 05
 * Unit: Celsius (°C)
 */
class CoolantTempCommand : TemperatureCommand {

    constructor() : super("01 05")

    constructor(other: CoolantTempCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.COOLANT_TEMP.value
}

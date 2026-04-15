package com.obd.api.engine

import com.obd.commands.TemperatureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the engine oil temperature.
 *
 * PID: 01 5C
 * Unit: °C
 */
class OilTempCommand : TemperatureCommand {

    constructor() : super("01 5C")

    constructor(other: OilTempCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.OIL_TEMP.value
}
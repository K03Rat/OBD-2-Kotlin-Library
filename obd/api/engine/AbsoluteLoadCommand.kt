package com.obd.api.engine

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Determines the absolute load of the engine.
 *
 * PID: 01 43
 * Formula: ((A * 256) + B) * 100 / 255
 * Unit: Percentage (%)
 */
class AbsoluteLoadCommand : PercentageObdCommand {

    constructor() : super("01 43")

    constructor(other: AbsoluteLoadCommand) : super(other)

    override fun performCalculations() {
        val a = buffer[2].toInt()
        val b = buffer[3].toInt()
        percentage = ((a * 256 + b) * 100f) / 255f
    }

    fun getRatio(): Double = percentage.toDouble()

    override fun getName(): String = AvailableCommandNames.ABS_LOAD.value
}
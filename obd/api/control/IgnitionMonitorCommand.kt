package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Monitors the ignition status of the vehicle.
 *
 * Returns "ON" or "OFF" depending on whether the ignition is active.
 */
class IgnitionMonitorCommand : ObdCommand {

    private var ignitionOn: Boolean = false

    constructor() : super("AT IGN")

    constructor(other: IgnitionMonitorCommand) : this()

    override fun performCalculations() {
        val res = getResult().trim()
        ignitionOn = res.equals("ON", ignoreCase = true)
    }

    override fun getFormattedResult(): String = getResult()

    override fun getCalculatedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.IGNITION_MONITOR.value

    fun isIgnitionOn(): Boolean = ignitionOn
}
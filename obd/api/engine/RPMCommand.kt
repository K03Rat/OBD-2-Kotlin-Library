package com.obd.api.engine

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the engine revolutions per minute (RPM).
 *
 * PID: 01 0C
 * Unit: RPM
 */
class RPMCommand : ObdCommand {

    private var rpm = -1

    constructor() : super("01 0C")

    constructor(other: RPMCommand) : super(other)

    override fun performCalculations() {
        if (buffer.size < 4) return

        val a = buffer[2].toInt()
        val b = buffer[3].toInt()
        rpm = (a * 256 + b) / 4
    }

    override fun getFormattedResult(): String = "$rpm RPM"

    override fun getCalculatedResult(): String = rpm.toString()

    fun getRPM(): Int = rpm

    override fun getName(): String = AvailableCommandNames.RPM.value
}
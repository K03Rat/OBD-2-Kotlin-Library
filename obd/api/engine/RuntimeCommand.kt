package com.obd.api.engine

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the engine runtime since start.
 *
 * PID: 01 1F
 * Unit: seconds (formatted as HH:MM:SS)
 */
class RuntimeCommand : ObdCommand {

    private var value: Int = -1

    constructor() : super("01 1F")

    constructor(other: RuntimeCommand) : super(other)

    override fun performCalculations() {
        value = buffer[2].toInt() * 256 + buffer[3].toInt()
    }

    override fun getFormattedResult(): String {
        val hours = value / 3600
        val minutes = (value % 3600) / 60
        val seconds = value % 60
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    override fun getCalculatedResult(): String = value.toString()

    override fun getName(): String = AvailableCommandNames.RUNTIME.value
}
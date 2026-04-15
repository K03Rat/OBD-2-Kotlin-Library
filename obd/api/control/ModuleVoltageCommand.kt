package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the control module voltage.
 *
 * OBD-II Mode 01 PID 42
 * Formula: ((A * 256) + B) / 1000 → volts
 */
class ModuleVoltageCommand : ObdCommand {

    private var voltage: Double = 0.0

    constructor() : super("01 42")

    constructor(other: ModuleVoltageCommand) : super(other)

    override fun performCalculations() {
        val a = buffer[2]
        val b = buffer[3]
        voltage = ((a * 256) + b) / 1000.0
    }

    override fun getFormattedResult(): String = String.format("%.1f%s", voltage, getResultUnit())

    override fun getResultUnit(): String = "V"

    override fun getCalculatedResult(): String = voltage.toString()

    fun getVoltage(): Double = voltage

    override fun getName(): String = AvailableCommandNames.CONTROL_MODULE_VOLTAGE.value
}
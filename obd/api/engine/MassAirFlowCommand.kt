package com.obd.api.engine

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the mass air flow (MAF) rate in grams per second.
 *
 * PID: 01 10
 * Unit: g/s
 */
class MassAirFlowCommand : ObdCommand {

    private var maf: Double = 0.0

    constructor() : super("01 10")

    constructor(other: MassAirFlowCommand) : super(other)

    override fun performCalculations() {
        val a = buffer[2].toInt()
        val b = buffer[3].toInt()
        maf = (a * 256 + b) / 100.0
    }

    override fun getFormattedResult(): String = "%.2f g/s".format(maf)

    override fun getCalculatedResult(): String = maf.toString()

    fun getMAF(): Double = maf

    override fun getName(): String = AvailableCommandNames.MAF.value
}
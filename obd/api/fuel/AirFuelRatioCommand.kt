package com.obd.api.fuel

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Air–Fuel Ratio (AFR) Command
 * PID: 01 44
 *
 * Formula: AFR = (((A * 256) + B) / 32768) * 14.7
 * Unit: Ratio (no unit, e.g. "14.7:1")
 */
class AirFuelRatioCommand : ObdCommand {

    private var afr: Float = 0f

    constructor() : super("01 44")

    constructor(other: AirFuelRatioCommand) : super(other)

    override fun performCalculations() {
        val A = buffer[2].toFloat()
        val B = buffer[3].toFloat()
        afr = (((A * 256f) + B) / 32768f) * 14.7f
    }

    override fun getFormattedResult(): String = "%.2f:1 AFR".format(afr)

    override fun getCalculatedResult(): String = afr.toString()

    fun getAirFuelRatio(): Float = afr

    override fun getName(): String = AvailableCommandNames.AIR_FUEL_RATIO.value
}
package com.obd.api.fuel

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the wideband air-fuel ratio (AFR) from OBD-II PID 01 34.
 *
 * Formula: AFR = (((A * 256) + B) / 32768) * 14.7
 */
class WidebandAirFuelRatioCommand : ObdCommand {

    private var widebandAfr: Float = 0f

    constructor() : super("01 34")
    constructor(other: WidebandAirFuelRatioCommand) : this()

    override fun performCalculations() {
        val A = buffer[2].toFloat()
        val B = buffer[3].toFloat()
        widebandAfr = (((A * 256f) + B) / 32768f) * 14.7f
    }

    override fun getFormattedResult(): String =
        "%.2f:1 AFR".format(widebandAfr)

    override fun getCalculatedResult(): String = widebandAfr.toString()

    fun getWidebandAirFuelRatio(): Float = widebandAfr

    override fun getName(): String = AvailableCommandNames.WIDEBAND_AIR_FUEL_RATIO.value
}
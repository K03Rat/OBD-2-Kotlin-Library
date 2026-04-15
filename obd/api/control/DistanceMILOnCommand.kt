package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.commands.SystemOfUnits
import com.obd.enums.AvailableCommandNames
/**
 * Distance traveled since MIL (Malfunction Indicator Lamp) was activated.
 *
 * PID: 01 21
 * Formula: Distance (km) = (A * 256) + B
 */
class DistanceMILOnCommand : ObdCommand, SystemOfUnits {

    private var km: Int = 0

    constructor() : super("01 21")

    constructor(other: DistanceMILOnCommand) : super(other)

    override fun performCalculations() {
        km = (buffer[2] * 256) + buffer[3]
    }

    override fun getFormattedResult(): String =
        if (useImperialUnits) "%.2f%s".format(getImperialUnit(), getResultUnit())
        else "%d%s".format(km, getResultUnit())

    override fun getCalculatedResult(): String =
        if (useImperialUnits) getImperialUnit().toString() else km.toString()

    override fun getResultUnit(): String = if (useImperialUnits) "m" else "km"

    override fun getImperialUnit(): Float = km * 0.621371192f

    fun getKm(): Int = km

    override fun getName(): String = AvailableCommandNames.DISTANCE_TRAVELED_MIL_ON.value
}
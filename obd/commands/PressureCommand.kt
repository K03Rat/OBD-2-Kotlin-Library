package com.obd.commands

/**
 * Abstract class for OBD-II pressure-based commands.
 *
 * Handles both metric (kPa) and imperial (psi) units.
 */
abstract class PressureCommand(command: String) : ObdCommand(command), SystemOfUnits {

    protected var tempValue: Int = 0
    protected var pressure: Int = 0

    constructor(other: PressureCommand) : this(other.command)

    /**
     * Some PressureCommand subclasses will need to override this method
     * to determine the final kPa value.
     * Must use [tempValue].
     *
     * @return pressure in kPa
     */
    protected open fun preparePressureValue(): Int {
        return buffer[2]
    }

    override fun performCalculations() {
        pressure = preparePressureValue()
    }

    override fun getFormattedResult(): String {
        return if (useImperialUnits) {
            "%.1f%s".format(getImperialUnit(), getResultUnit())
        } else {
            "%d%s".format(pressure, getResultUnit())
        }
    }

    fun getMetricUnit(): Int = pressure

    override fun getImperialUnit(): Float = pressure * 0.145037738f

    override fun getCalculatedResult(): String {
        return if (useImperialUnits) getImperialUnit().toString() else pressure.toString()
    }

    override fun getResultUnit(): String = if (useImperialUnits) "psi" else "kPa"
}

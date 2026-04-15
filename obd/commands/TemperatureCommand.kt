package com.obd.commands

abstract class TemperatureCommand(
    command: String
) : ObdCommand(command), SystemOfUnits {

    protected var temperature: Float = 0.0f
        private set

    constructor(other: TemperatureCommand) : this(other.command)

    override fun performCalculations() {
        temperature = buffer[2] - 40.0f
    }

    override fun getFormattedResult(): String {
        return if (useImperialUnits) {
            "%.1f%s".format(getImperialUnit(), getResultUnit())
        } else {
            "%.0f%s".format(temperature, getResultUnit())
        }
    }

    override fun getCalculatedResult(): String {
        return if (useImperialUnits) getImperialUnit().toString() else temperature.toString()
    }

    override fun getResultUnit(): String = if (useImperialUnits) "F" else "C"

    /**
     * Returns the temperature in Celsius
     */

    /**
     * Returns the temperature in Fahrenheit
     */
    override fun getImperialUnit(): Float = temperature * 1.8f + 32

    fun getKelvin(): Float = temperature + 273.15f

    abstract override fun getName(): String
}

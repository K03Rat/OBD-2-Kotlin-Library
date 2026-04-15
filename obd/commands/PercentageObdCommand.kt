package com.obd.commands
abstract class PercentageObdCommand(command: String) : ObdCommand(command) {

    var percentage: Float = 0f
        protected set

    constructor(other: PercentageObdCommand) : this(other.command) {
        this.percentage = other.percentage
    }

    override fun performCalculations() {
        val bufferValue = buffer[2]
        percentage = (bufferValue * 100f) / 255f
    }

    override fun getFormattedResult(): String = "%.1f%s".format(percentage, getResultUnit())

    override fun getCalculatedResult(): String = percentage.toString()

    override fun getResultUnit(): String = "%"
}

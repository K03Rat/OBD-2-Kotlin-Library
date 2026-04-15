package com.obd.commands

abstract class AvailablePidsCommand : PersistentObdCommand {

    constructor(command: String) : super(command)

    constructor(other: AvailablePidsCommand) : super(other.command)

    override fun performCalculations() {}

    override fun getFormattedResult(): String = getCalculatedResult()

    override fun getCalculatedResult(): String = rawData.substring(4)
}

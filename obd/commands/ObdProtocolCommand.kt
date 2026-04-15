package com.obd.commands

abstract class ObdProtocolCommand : ObdCommand {

    constructor(command: String) : super(command)

    constructor(other: ObdProtocolCommand) : this(other.command)

    override fun performCalculations() {}

    override fun fillBuffer() {}

    override fun getCalculatedResult(): String = getResult()
}
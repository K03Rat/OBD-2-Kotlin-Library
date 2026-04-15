package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

class EchoOffCommand : ObdProtocolCommand {

    constructor() : super("AT E0")

    constructor(other: EchoOffCommand) : this()

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.ECHO_OFF.value
}
package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Warm-start the OBD connection.
 *
 * Sends "AT WS" to the ELM327 interface.
 * Use this command when you want to restart the interface
 * without fully resetting it (keeps some learned settings).
 */
class ObdWarmstartCommand : ObdProtocolCommand {

    constructor() : super("AT WS")

    constructor(other: ObdWarmstartCommand) : this()

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.WARMSTART_OBD.value
}
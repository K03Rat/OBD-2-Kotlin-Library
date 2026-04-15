package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Reset the OBD connection.
 *
 * Sends "AT Z" to the ELM327 interface to reset it.
 * Use this command when you need to clear the interface state
 * or after a connection is lost.
 */
class ObdResetCommand : ObdProtocolCommand {

    constructor() : super("AT Z")

    constructor(other: ObdResetCommand) : this()

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.RESET_OBD.value
}
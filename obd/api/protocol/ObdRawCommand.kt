package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
/**
 * Sends any custom or unspecified OBD command directly to the ELM327 interface.
 *
 * Use this when a command does not have a predefined class.
 */
class ObdRawCommand : ObdProtocolCommand {

    constructor(command: String) : super(command)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = "Custom command $command"
}
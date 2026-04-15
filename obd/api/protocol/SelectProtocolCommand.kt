package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.ObdProtocols
/**
 * Select the protocol to use.
 *
 * Sends "AT SP <protocol>" to the OBD-II interface.
 * Use this command to manually select a specific OBD protocol.
 */
class SelectProtocolCommand : ObdProtocolCommand {

    private val protocol: ObdProtocols

    constructor(protocol: ObdProtocols) : super("AT SP ${protocol.value}") {
        this.protocol = protocol
    }

    constructor(other: SelectProtocolCommand) : this(other.protocol)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = "Select Protocol ${protocol.name}"
}
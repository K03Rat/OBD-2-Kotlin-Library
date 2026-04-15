package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * CloseCommand is used to close the current OBD-II protocol connection on an ELM327 adapter.
 *
 * Usage:
 * 1. If your ELM327 loses connection or you want to reset the communication,
 *    you can send this command to "close" the current protocol.
 * 2. This is especially important for ISO 9141 and ISO 14230 protocols, which
 *    require a special initialization sequence.
 * 3. After sending this command, you can reopen the connection by sending a
 *    standard PID request like `01 00`. Do NOT send ATZ or AT SP0, as this
 *    can interfere with the initialization of the protocol.
 *
 */
class CloseCommand : ObdProtocolCommand {

    constructor() : super("AT PC")
    constructor(other: CloseCommand) : super(other)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.CLOSE_PROTOCOL.value
}
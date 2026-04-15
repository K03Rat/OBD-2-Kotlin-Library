package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Command to turn off headers in OBD-II responses.
 *
 * When headers are off, the responses from the ELM327 adapter will
 * not include the CAN/OBD-II header information. This can simplify
 * parsing if you do not need frame IDs.
 */
class HeadersOffCommand : ObdProtocolCommand {

    constructor() : super("ATH0")

    constructor(other: HeadersOffCommand) : super(other)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.HEADERS_OFF.value
}
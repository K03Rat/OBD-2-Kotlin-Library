package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Command to turn off line feeds in OBD-II responses.
 *
 * When line feeds are disabled, the ELM327 will not include
 * newline characters in responses. This can simplify parsing
 * if you want a single continuous response string.
 */
class LineFeedOffCommand : ObdProtocolCommand {

    constructor() : super("AT L0")

    constructor(other: LineFeedOffCommand) : super(other)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.LINE_FEED_OFF.value
}
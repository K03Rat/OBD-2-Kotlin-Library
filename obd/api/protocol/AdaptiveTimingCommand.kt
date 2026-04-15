package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Sets the adaptive timing mode on the OBD interface.
 *
 * Uses the AT AT command with a mode parameter.
 */
class AdaptiveTimingCommand : ObdProtocolCommand {

    constructor(mode: Int) : super("AT AT$mode")
    constructor(other: AdaptiveTimingCommand) : super(other)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.ADAPTIVE_TIMING.value
}
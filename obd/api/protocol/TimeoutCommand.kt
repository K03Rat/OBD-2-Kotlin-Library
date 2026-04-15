package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Sets the timeout value (in milliseconds) for the OBD-II interface.
 *
 * Sends the "AT ST xx" command, where xx is the hex representation
 * of the timeout value. If the ECU does not respond within this
 * time, "NO DATA" is returned.
 *
 * @param timeout Value between 0 and 255; multiplied by 4 gives the
 * desired timeout in milliseconds.
 */
class TimeoutCommand : ObdProtocolCommand {

    constructor(timeout: Int) : super("AT ST ${timeout.coerceIn(0, 255).toString(16)}")

    constructor(other: TimeoutCommand) : this(0)

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.TIMEOUT.value
}
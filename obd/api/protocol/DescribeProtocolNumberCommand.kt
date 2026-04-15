package com.obd.api.protocol

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
import com.obd.enums.ObdProtocols
/**
 * Retrieves the numeric code of the current OBD-II protocol.
 *
 * Sends the `AT DPN` command to the ELM327 adapter.
 * Returns a number (or 'A' + number if automatic mode is enabled)
 * representing the current protocol. This number corresponds
 * to the codes used by `SetProtocolCommand` and `TestProtocolCommand`.
 *
 * Useful for verifying the protocol in use for diagnostics or logging.
 * Does not change the protocol, only reports it.
 */
class DescribeProtocolNumberCommand : ObdCommand {

    var obdProtocol: ObdProtocols = ObdProtocols.AUTO
        private set

    constructor() : super("AT DPN")

    constructor(other: DescribeProtocolNumberCommand) : super(other)

    override fun performCalculations() {
        val result = getResult()
        val protocolNumber = if (result.length == 2) result[1] else result[0]
        obdProtocol = ObdProtocols.values().firstOrNull { it.value == protocolNumber } ?: ObdProtocols.AUTO
    }

    override fun getFormattedResult(): String = getResult()

    override fun getCalculatedResult(): String = obdProtocol.name

    override fun getName(): String = AvailableCommandNames.DESCRIBE_PROTOCOL_NUMBER.value
}
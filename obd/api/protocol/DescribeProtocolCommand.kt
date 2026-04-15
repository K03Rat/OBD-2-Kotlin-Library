package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * DescribeProtocolCommand retrieves a description of the current OBD-II protocol in use.
 *
 * Usage:
 * 1. Sends the `AT DP` command to the ELM327 adapter.
 * 2. Returns the actual protocol name being used, not the numeric code.
 * 3. If automatic protocol selection is enabled (AT SP0), the result will include "AUTO"
 *    before the protocol description.
 *
 * Notes:
 * - Useful for debugging or verifying which protocol the adapter is currently using.
 * - Does not change the protocol, only describes it.
 */
class DescribeProtocolCommand : ObdProtocolCommand {
    constructor() : super("AT DP")

    override fun getFormattedResult(): String = getResult()
    override fun getName(): String = AvailableCommandNames.DESCRIBE_PROTOCOL.value
}
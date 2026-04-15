package com.obd.api.protocol

import com.obd.commands.ObdProtocolCommand
import com.obd.enums.AvailableCommandNames

/**
 * Turn off spaces in the OBD-II response.
 *
 * Sends "ATS0" to the OBD-II interface.
 * This removes extra spaces in the returned data for easier parsing.
 */
class SpacesOffCommand : ObdProtocolCommand {

    constructor() : super("ATS0")

    constructor(other: SpacesOffCommand) : this()

    override fun getFormattedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.SPACES_OFF.value
}
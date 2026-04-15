package com.obd.api.protocol

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames

/**
 * Reset trouble codes.
 *
 * Sends "04" to the OBD-II interface.
 * Use this command to clear all stored Diagnostic Trouble Codes (DTCs)
 * and turn off the MIL (Check Engine Light).
 */
class ResetTroubleCodesCommand : ObdCommand {

    constructor() : super("04")

    constructor(other: ResetTroubleCodesCommand) : this()

    override fun performCalculations() {}

    override fun getFormattedResult(): String = getResult()

    override fun getCalculatedResult(): String = getResult()

    override fun getName(): String = AvailableCommandNames.RESET_TROUBLE_CODES.value
}
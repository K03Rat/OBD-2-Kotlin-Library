package com.obd.api.engine

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the throttle position as a percentage.
 *
 * PID: 01 11
 * Unit: %
 */
class ThrottlePositionCommand : PercentageObdCommand {

    constructor() : super("01 11")

    constructor(other: ThrottlePositionCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.THROTTLE_POS.value
}
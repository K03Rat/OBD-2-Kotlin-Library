package com.obd.api.engine

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the absolute engine load.
 *
 * PID: 01 04
 * Unit: %
 */
class LoadCommand : PercentageObdCommand {

    constructor() : super("01 04")

    constructor(other: LoadCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.LOAD.value
}
package com.obd.api.control

import com.obd.commands.PercentageObdCommand
import com.obd.enums.AvailableCommandNames

class TimingAdvanceCommand : PercentageObdCommand {

    constructor() : super("01 0E")

    constructor(other: TimingAdvanceCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.TIMING_ADVANCE.value
}
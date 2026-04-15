package com.obd.api.protocol

import com.obd.commands.AvailablePidsCommand
import com.obd.enums.AvailableCommandNames

class AvailablePidsCommand_41_to_60 : AvailablePidsCommand {

    constructor() : super("01 40")
    constructor(other: AvailablePidsCommand_41_to_60) : super(other)

    override fun getName(): String = AvailableCommandNames.PIDS_41_60.value
}
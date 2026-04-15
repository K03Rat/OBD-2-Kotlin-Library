package com.obd.api.protocol

import com.obd.commands.AvailablePidsCommand
import com.obd.enums.AvailableCommandNames

class AvailablePidsCommand_21_to_40: AvailablePidsCommand {

    constructor() : super("01 20")
    constructor(other: AvailablePidsCommand_21_to_40) : super(other)

    override fun getName(): String = AvailableCommandNames.PIDS_21_40.value
}
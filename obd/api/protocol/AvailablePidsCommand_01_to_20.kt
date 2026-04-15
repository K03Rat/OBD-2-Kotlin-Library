package com.obd.api.protocol

import com.obd.commands.AvailablePidsCommand
import com.obd.enums.AvailableCommandNames

class AvailablePidsCommand_01_to_20 : AvailablePidsCommand {

    constructor() : super("01 00")

    constructor(other: AvailablePidsCommand_01_to_20) : super(other)

    override fun getName(): String = AvailableCommandNames.PIDS_01_20.value
}
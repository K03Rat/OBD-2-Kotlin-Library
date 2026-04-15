package com.obd.api.pressure

import com.obd.commands.PressureCommand
import com.obd.enums.AvailableCommandNames
/**
 * Reads the intake manifold absolute pressure (MAP).
 *
 * PID: 01 0B
 * Formula: Pressure (kPa) = A
 */
class IntakeManifoldPressureCommand : PressureCommand {

    constructor() : super("01 0B")

    constructor(other: IntakeManifoldPressureCommand) : super(other)

    override fun getName(): String = AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.value
}

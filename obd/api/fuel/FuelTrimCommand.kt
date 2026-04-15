package com.obd.api.fuel

import com.obd.commands.PercentageObdCommand
import com.obd.enums.FuelTrim
/**
 * Reads short-term or long-term fuel trim percentage for a given bank.
 *
 * Standard PIDs:
 * - 01 06 → Short Term Fuel Trim (Bank 1)
 * - 01 07 → Long Term Fuel Trim (Bank 1)
 * - 01 08 → Short Term Fuel Trim (Bank 2)
 * - 01 09 → Long Term Fuel Trim (Bank 2)
 *
 * Formula: (A - 128) * (100 / 128)
 */
class FuelTrimCommand(
    private val bank: FuelTrim = FuelTrim.SHORT_TERM_BANK_1
) : PercentageObdCommand(bank.buildObdCommand()) {

    constructor(other: FuelTrimCommand) : this(other.bank)

    override fun performCalculations() {
        val A = buffer[2]
        percentage = (A - 128f) * (100f / 128f)
    }

    fun getFuelTrim(): Float = percentage

    fun getBank(): String = bank.bank

    override fun getName(): String = bank.bank
}
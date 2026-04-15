package com.obd.enums

enum class FuelTrim(val value: Int, val bank: String) {
    LONG_TERM_BANK_1(0x07, "Long Term Fuel Trim Bank 1"),
    LONG_TERM_BANK_2(0x09, "Long Term Fuel Trim Bank 2"),
    SHORT_TERM_BANK_1(0x06, "Short Term Fuel Trim Bank 1"),
    SHORT_TERM_BANK_2(0x08, "Short Term Fuel Trim Bank 2");

    companion object {
        private val map = FuelTrim.entries.associateBy(FuelTrim::value)

        fun fromValue(value: Int): FuelTrim? = map[value]
    }

    /**
     * Builds the OBD command string for this fuel trim bank.
     */
    fun buildObdCommand(): String = "01 0${value.toString(16).uppercase()}"
}
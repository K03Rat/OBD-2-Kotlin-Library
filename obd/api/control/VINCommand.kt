package com.obd.api.control

import com.obd.commands.PersistentObdCommand
import com.obd.enums.AvailableCommandNames
import java.util.regex.Pattern

/**
 * OBD-II Mode 09 PID 02 (VIN). Framing differs by protocol: multi-frame CAN uses `:` markers;
 * ISO 9141-2 / KWP2000 use a simpler hex layout.
 */
class VINCommand : PersistentObdCommand {

    var vin: String = ""

    constructor() : super("09 02")
    constructor(other: VINCommand) : super(other.command)

    override fun performCalculations() {
        val result = getResult()
        val workingData: String = if (result.contains(":")) {
            var temp = result.replace(Regex(".:"), "").substring(9)
            val matcher = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
                .matcher(convertHexToString(temp))
            if (matcher.find()) {
                temp = result.replace(Regex("0:49"), "").replace(Regex(".:"), "")
            }
            temp
        } else {
            result.replace(Regex("49020."), "")
        }

        vin = convertHexToString(workingData).replace(Regex("[\\u0000-\\u001f]"), "")
    }

    override fun getFormattedResult(): String = vin

    override fun getCalculatedResult(): String = vin

    override fun getName(): String = AvailableCommandNames.VIN.value

    private fun convertHexToString(hex: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < hex.length - 1) {
            val decimal = hex.substring(i, i + 2).toInt(16)
            sb.append(decimal.toChar())
            i += 2
        }
        return sb.toString()
    }
}
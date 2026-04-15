package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
import java.io.IOException
import java.io.InputStream

class TroubleCodesCommand : ObdCommand {

    companion object {
        private val dtcLetters = charArrayOf('P', 'C', 'B', 'U')
        private val hexArray = "0123456789ABCDEF".toCharArray()
    }

    private var codes: StringBuilder = StringBuilder()

    constructor() : super("03")

    constructor(other: TroubleCodesCommand) : super(other) {
        codes = StringBuilder()
    }

    override fun performCalculations() {
        val result = getResult()
        val workingData: String
        var startIndex = 0

        val canOneFrame = result.replace("\r", "").replace("\n", "")
        workingData = when {
            canOneFrame.length <= 16 && canOneFrame.length % 4 == 0 -> {
                startIndex = 4
                canOneFrame
            }
            result.contains(":") -> {
                startIndex = 7
                result.replace(Regex("[\r\n].:"), "")
            }
            else -> {
                result.replace(Regex("^43|[\r\n]43|[\r\n]"), "")
            }
        }

        var begin = startIndex
        while (begin < workingData.length) {
            var dtc = ""
            val b1 = hexStringToByteArray(workingData[begin])
            val ch1 = (b1.toInt() and 0xC0) shr 6
            val ch2 = (b1.toInt() and 0x30) shr 4
            dtc += dtcLetters[ch1]
            dtc += hexArray[ch2]
            dtc += workingData.substring(begin + 1, begin + 4)
            if (dtc == "P0000") return
            codes.append(dtc).append('\n')
            begin += 4
        }
    }

    private fun hexStringToByteArray(s: Char): Byte {
        return (Character.digit(s, 16) shl 4).toByte()
    }

    @Deprecated("Use getCalculatedResult() instead")
    fun formatResult(): String = codes.toString()

    override fun getCalculatedResult(): String = codes.toString()

    @Throws(IOException::class)
    override suspend fun readRawData(input: InputStream) {
        val res = StringBuilder()
        while (true) {
            val b = input.read().toByte()
            if (b.toInt() == -1) break
            val c = b.toInt().toChar()
            if (c == '>') break
            if (c != ' ') res.append(c)
        }
        rawData = res.toString().trim()
    }

    override fun getFormattedResult(): String = codes.toString()

    override fun getName(): String = AvailableCommandNames.TROUBLE_CODES.value
}
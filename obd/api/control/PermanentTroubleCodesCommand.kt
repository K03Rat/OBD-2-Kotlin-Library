package com.obd.api.control

import com.obd.commands.ObdCommand
import com.obd.enums.AvailableCommandNames
import java.io.IOException
import java.io.InputStream

/**
 * Permanent Trouble Codes command.
 *
 * Reads stored permanent DTCs from the ECU.
 */
class PermanentTroubleCodesCommand : ObdCommand {

    companion object {
        private val dtcLetters = charArrayOf('P', 'C', 'B', 'U')
        private val hexArray = "0123456789ABCDEF".toCharArray()
    }

    private val codes = StringBuilder()

    constructor() : super("0A")

    constructor(other: PermanentTroubleCodesCommand) : super(other)

    override fun performCalculations() {
        val result = getResult()
        val workingData: String
        var startIndex = 0

        val canOneFrame = result.replace(Regex("[\r\n]"), "")
        val canOneFrameLength = canOneFrame.length

        workingData = when {
            // CAN (ISO-15765) one frame
            canOneFrameLength <= 16 && canOneFrameLength % 4 == 0 -> {
                startIndex = 4
                canOneFrame
            }
            // CAN multi-frame
            result.contains(":") -> {
                startIndex = 7
                result.replace(Regex("[\r\n].:"), "")
            }
            // ISO9141-2 / KWP2000 protocols
            else -> {
                result.replace(Regex("^4A|[\r\n]4A|[\r\n]"), "")
            }
        }

        var begin = startIndex
        while (begin < workingData.length) {
            var dtc = ""
            val b1 = hexCharToByte(workingData[begin])
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

    private fun hexCharToByte(c: Char): Byte {
        return (Character.digit(c, 16) shl 4).toByte()
    }

    @Deprecated("Use getCalculatedResult() instead")
    fun formatResult(): String = codes.toString()

    override fun getCalculatedResult(): String = codes.toString()

    @Throws(IOException::class)
    override suspend fun readRawData(input: InputStream) {
        val res = StringBuilder()
        var b: Int

        while (true) {
            b = input.read()
            if (b == -1) break
            val c = b.toChar()
            if (c == '>') break
            if (c != ' ') res.append(c)
        }

        rawData = res.toString().trim()
    }

    override fun getFormattedResult(): String = codes.toString()

    override fun getName(): String = AvailableCommandNames.PERMANENT_TROUBLE_CODES.value
}
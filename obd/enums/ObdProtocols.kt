package com.obd.enums

/**
 * ELM327 `AT SP x` protocol selector: [value] is the single character the datasheet uses
 * (e.g. `6` = ISO 15765-4 CAN 11-bit / 500 kbaud). See ELM327 documentation for full mapping.
 */
enum class ObdProtocols(val value: Char) {
    AUTO('0'),
    ISO_14230_4_KWP('4'),
    ISO_14230_4_KWP_FAST('5'),
    ISO_15765_4_CAN('6'),
    ISO_15765_4_CAN_B('7'),
    ISO_15765_4_CAN_C('8'),
    ISO_15765_4_CAN_D('9'),
    ISO_9141_2('3'),
    SAE_J1850_PWM('1'),
    SAE_J1850_VPW('2'),
    SAE_J1939_CAN('A'),
    USER1_CAN('B'),
    USER2_CAN('C');

    companion object {
        private val map = values().associateBy(ObdProtocols::value)
        fun fromValue(value: Char): ObdProtocols? = map[value]
    }
}

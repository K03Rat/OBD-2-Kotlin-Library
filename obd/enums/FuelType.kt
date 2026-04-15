package com.obd.enums

/**
 * Enum representing fuel types and their OBD-II codes.
 */
enum class FuelType(val value: Int, val description: String) {
    BIFUEL_CNG(0x0D, "Biodiesel + Natural Gas"),
    BIFUEL_ELECTRIC(0x0F, "Biodiesel + Electric"),
    BIFUEL_ETHANOL(0x0B, "Biodiesel + Ethanol"),
    BIFUEL_GASOLINE(0x09, "Biodiesel + Gasoline"),
    BIFUEL_GASOLINE_ELECTRIC(0x10, "Biodiesel + Gasoline/Electric"),
    BIFUEL_LPG(0x0C, "Biodiesel + GPL/LGP"),
    BIFUEL_METHANOL(0x0A, "Biodiesel + Methanol"),
    BIFUEL_PROPANE(0x0E, "Biodiesel + Propane"),
    CNG(0x06, "Natural Gas"),
    DIESEL(0x04, "Diesel"),
    ELECTRIC(0x08, "Electric"),
    ETHANOL(0x03, "Ethanol"),
    GASOLINE(0x01, "Gasoline"),
    HYBRID_DIESEL(0x13, "Hybrid Diesel"),
    HYBRID_ELECTRIC(0x14, "Hybrid Electric"),
    HYBRID_ETHANOL(0x12, "Hybrid Ethanol"),
    HYBRID_GASOLINE(0x11, "Hybrid Gasoline"),
    HYBRID_MIXED(0x15, "Hybrid Mixed"),
    HYBRID_REGENERATIVE(0x16, "Hybrid Regenerative"),
    LPG(0x05, "GPL/LGP"),
    METHANOL(0x02, "Methanol"),
    PROPANE(0x07, "Propane");

    companion object {
        private val map = values().associateBy(FuelType::value)

        fun fromValue(value: Int): FuelType? = map[value]
    }
}
package com.obd.enums

/**
 * An enumeration of all available OBD-II command names.
 */
enum class AvailableCommandNames(val value: String) {
    ABS_LOAD("Absolute load value"),
    COOLANT_TEMP("Engine coolant temperature"),
    DRIVERS_DEMAND_TORQUE("Driver's demand engine - percent torque"),
    INTAKE_AIR_TEMP("Air Intake Temperature"),
    LOAD("Calculated engine load"),
    MAF("Mass air flow sensor (MAF) air flow rate"),
    OIL_TEMP("Engine oil temperature"),
    RPM("Engine RPM"),
    RUNTIME("Engine Runtime"),
    THROTTLE_POS("Throttle Position"),
    AIR_FUEL_RATIO("Air/Fuel Ratio"),
    FUEL_CONSUMPTION_RATE("Fuel Consumption Rate"),
    FUEL_LEVEL("Fuel Level"),
    FUEL_TYPE("Fuel Type"),
    WIDEBAND_AIR_FUEL_RATIO("Wideband Air/Fuel Ratio"),
    BAROMETRIC_PRESSURE("Absolute Barometric Pressure"),
    FUEL_PRESSURE("Fuel Pressure"),
    FUEL_RAIL_PRESSURE("Fuel Rail Gauge Pressure"),
    INTAKE_MANIFOLD_PRESSURE("Intake manifold absolute pressure"),
    AMBIENT_AIR_TEMP("Ambient Air Temperature"),
    ADAPTIVE_TIMING("Adaptive Timing"),
    CLOSE_PROTOCOL("Close Protocol"),
    CONTROL_MODULE_VOLTAGE("Control Module Voltage"),
    DESCRIBE_PROTOCOL("Describe Protocol"),
    DESCRIBE_PROTOCOL_NUMBER("Describe Protocol Number"),
    DISTANCE_TRAVELED_AFTER_CODES_CLEARED("Distance traveled since codes cleared"),
    DISTANCE_TRAVELED_MIL_ON("Distance traveled with malfunction indicator lamp (MIL) on"),
    ECHO_OFF("Echo Off"),
    HEADERS_OFF("Headers disabled"),
    IGNITION_MONITOR("Ignition Monitor"),
    LINE_FEED_OFF("Line Feed Off"),
    PENDING_TROUBLE_CODES("Pending Trouble Codes"),
    PERMANENT_TROUBLE_CODES("Permanent Trouble Codes"),
    PIDS_01_20("PIDs 01-20"),
    PIDS_21_40("PIDs 21-40"),
    PIDS_41_60("PIDs 41-60"),
    RESET_OBD("Reset OBD"),
    RESET_TROUBLE_CODES("Reset Trouble Codes"),
    SPACES_OFF("Spaces Off"),
    TIMEOUT("Timeout"),
    TIMING_ADVANCE("Timing Advance"),
    TROUBLE_CODES("Trouble Codes"),
    VEHICLE_SPEED("Vehicle Speed"),
    VIN("VIN"),
    WARMSTART_OBD("Warmstart OBD");

    override fun toString(): String = value
}
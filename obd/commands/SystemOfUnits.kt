package com.obd.commands

fun interface SystemOfUnits {
    /**
     * Returns the value in imperial units.
     */
    fun getImperialUnit(): Float
}
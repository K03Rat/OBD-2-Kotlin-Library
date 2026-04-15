# `com.obd` — OBD-II / ELM327 Command Layer

A Kotlin library for communicating with ELM327-compatible OBD-II adapters over Bluetooth Serial Port Profile (SPP). Sends AT setup strings and SAE J1979 Mode/PID requests, parses hex responses into typed values.
This library is inspired by the exisiting library written in Java by http://github.com/pires/obd-java-api

## Features

- Coroutine-based async I/O with mutex-guarded command execution
- Adaptive read timeouts that adjust to ECU response speed
- Persistent command caching for static data (VIN, fuel type)
- Comprehensive error detection (NO DATA, BUS ERROR, CAN ERROR, BUFFER FULL, etc.)
- Support for multi-frame CAN responses (ISO 15765) and legacy protocols (ISO 9141-2, KWP2000)
- Trouble code reading (stored, pending, permanent) and clearing

## Package Layout

| Package | Role |
|---------|------|
| `commands/` | Base classes: `ObdCommand` (PID traffic), `ObdProtocolCommand` (AT-only), `PersistentObdCommand` (cached static data), helpers for temperature, pressure, percentages, and supported-PID bitmasks. |
| `api/engine/` | Engine metrics: RPM, load, coolant temp, MAF, throttle position, oil temp, intake air temp, runtime. |
| `api/fuel/` | Fuel metrics: consumption rate (PID 5E), fuel level, air-fuel ratio, wideband AFR, fuel trim, fuel type. |
| `api/pressure/` | Pressure metrics: barometric, fuel pressure, fuel rail, intake manifold. |
| `api/control/` | Vehicle control: speed, VIN, trouble codes (stored/pending/permanent), ignition monitor, module voltage. |
| `api/protocol/` | ELM327 setup: reset, echo off, headers off, spaces off, protocol selection, adaptive timing, available PIDs. |
| `api/temperature/` | Ambient air temperature. |
| `enums/` | `ObdProtocols` (ELM AT SP characters), `AvailableCommandNames` (display strings), `FuelType`, `FuelTrim`. |

## Adding a New PID

1. Subclass `ObdCommand` (or an existing abstract like `PercentageObdCommand` if the formula fits).
2. Pass the mode/PID hex string in the constructor, e.g. `super("01 XX")`.
3. Implement `performCalculations()` using `buffer` indices per the SAE J1979 formula for that PID.
4. Implement `getFormattedResult()`, `getCalculatedResult()`, and `getName()`.
5. Register a display name in `AvailableCommandNames` if the app selects commands by name.

## Supported Protocols

The library works with any ELM327-compatible adapter. Protocol selection is handled via `SelectProtocolCommand` using the standard ELM327 AT SP codes defined in `ObdProtocols`.

## Dependencies

- Android SDK (uses `android.util.Log` for debug logging)
- Kotlin Coroutines (`kotlinx.coroutines`)

## References

- SAE J1979 — OBD-II PIDs and response formulas
- ELM327 datasheet — AT commands and protocol selection
- ISO 15765-4 — CAN bus diagnostics
- ISO 9141-2 / ISO 14230 (KWP2000) — Legacy OBD protocols

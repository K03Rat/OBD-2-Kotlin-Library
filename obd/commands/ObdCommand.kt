package com.obd.commands

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

/**
 * Base OBD-II command class with detailed debug logging, improved error handling, and performance optimizations.
 */
abstract class ObdCommand(private val commandString: String) {

    protected var command: String = commandString
    protected val buffer = ArrayList<Int>()
    protected var rawData: String = ""
    protected var responseDelayMs: Long? = null
    protected var useImperialUnits: Boolean = false
    protected var start: Long = 0L
    protected var end: Long = 0L
    private val mutex = Mutex()

    companion object {
        private const val TAG = "ObdCommand"
        /** When true, logs per-command timing and responses (noisy on the polling loop). */
        private const val DEBUG = false
        /** Read timeout bounds; adjusted from measured ECU response times. */
        private const val MIN_TIMEOUT_MS = 200L
        private const val MAX_TIMEOUT_MS = 800L
        private const val DEFAULT_READ_TIMEOUT_MS = 500L
        @Volatile
        private var adaptiveTimeout = DEFAULT_READ_TIMEOUT_MS

        private const val READ_BUFFER_SIZE = 128

        private val WHITESPACE_PATTERN = Regex("\\s+")
        private val BUSINIT_PATTERN = Regex("(BUS INIT)|(BUSINIT)|(\\.)", RegexOption.IGNORE_CASE)
        private val SEARCHING_PATTERN = Regex("SEARCHING", RegexOption.IGNORE_CASE)
        private val HEX_PATTERN = Regex("^[0-9A-Fa-f]+$")
        private val CLEAN_PATTERN = Regex("(BUS INIT|BUSINIT|\\.|SEARCHING|\\s+|:)", RegexOption.IGNORE_CASE)
    }

    init {
        require(command.isNotEmpty()) { "Command cannot be empty" }
    }

    constructor(other: ObdCommand) : this(other.command) {
        this.responseDelayMs = other.responseDelayMs
        this.useImperialUnits = other.useImperialUnits
    }

    /**
     * Resends the command prompt to reset the ELM327 adapter if needed.
     */
    @Throws(IOException::class)
    protected open suspend fun resendCommand(output: OutputStream) {
        try {
            debug("Resending command prompt (\\r)...")
            withContext(Dispatchers.IO) {
                output.write("\r".toByteArray())
                output.flush()
            }
            responseDelayMs?.let { delay ->
                if (delay > 0) {
                    debug("Waiting $delay ms after resend")
                    delay(delay)
                }
            }
        } catch (e: IOException) {
            error("Failed to resend command: ${e.message}")
            throw IOException("Failed to resend command: ${e.message}", e)
        }
    }

    /**
     * Core send/read pipeline (mutex, timing, adaptive timeout).
     * Subclasses that cache (e.g. [PersistentObdCommand]) call this on a cache miss instead of
     * [super.run], avoiding Kotlin JVM IR synthetic-accessor bugs with `super.run` in suspend overrides.
     */
    @Throws(IOException::class, InterruptedException::class)
    protected open suspend fun runCommandPipeline(input: InputStream, output: OutputStream) {
        mutex.withLock {
            start = System.currentTimeMillis()
            sendCommand(output)
            readResult(input)
            end = System.currentTimeMillis()

            val responseTime = end - start
            adjustAdaptiveTimeout(responseTime)

            if (DEBUG) {
                val duration = end - start
                debug("Command $command completed in ${duration}ms (timeout: ${adaptiveTimeout}ms)")
            }
        }
    }

    /**
     * Executes the OBD command asynchronously, ensuring thread safety with a mutex.
     */
    @Throws(IOException::class, InterruptedException::class)
    open suspend fun run(input: InputStream, output: OutputStream) {
        runCommandPipeline(input, output)
    }
    
    /** Nudges [adaptiveTimeout] toward fast ECUs (&lt;100ms replies) or slow ones (&gt;400ms). */
    private fun adjustAdaptiveTimeout(responseTime: Long) {
        when {
            responseTime < 100 -> {
                adaptiveTimeout = maxOf(MIN_TIMEOUT_MS, adaptiveTimeout - 50)
                if (DEBUG) debug("Fast ECU detected, reducing timeout to ${adaptiveTimeout}ms")
            }
            responseTime > 400 -> {
                adaptiveTimeout = minOf(MAX_TIMEOUT_MS, adaptiveTimeout + 50)
                if (DEBUG) debug("Slow ECU detected, increasing timeout to ${adaptiveTimeout}ms")
            }
            else -> {
                if (adaptiveTimeout < DEFAULT_READ_TIMEOUT_MS) {
                    adaptiveTimeout = minOf(DEFAULT_READ_TIMEOUT_MS, adaptiveTimeout + 25)
                } else if (adaptiveTimeout > DEFAULT_READ_TIMEOUT_MS) {
                    adaptiveTimeout = maxOf(DEFAULT_READ_TIMEOUT_MS, adaptiveTimeout - 25)
                }
            }
        }
    }

    /**
     * Sends the command to the output stream.
     */
    @Throws(IOException::class)
    protected open suspend fun sendCommand(output: OutputStream) {
        try {
            debug("Sending command: \"$command\\r\"")
            withContext(Dispatchers.IO) {
                val commandBytes = (command + "\r").toByteArray(Charsets.US_ASCII)
                output.write(commandBytes)
                output.flush()
            }
            responseDelayMs?.let { delay ->
                if (delay > 0) {
                    debug("Waiting $delay ms before reading response")
                    delay(delay)
                }
            }
        } catch (e: IOException) {
            error("Failed to send command: ${e.message}")
            throw IOException("Failed to send command: ${e.message}", e)
        }
    }

    /**
     * Parses raw OBD response into [rawData] and [buffer], then [performCalculations].
     * Used by [readResult] and by [PersistentObdCommand] instead of [super.readResult] to avoid
     * Kotlin JVM IR issues with `super` in suspend overrides.
     */
    @Throws(IOException::class)
    protected open suspend fun readResultPipeline(input: InputStream) {
        readRawData(input)
        if (DEBUG) debug("Raw response: \"$rawData\"")
        checkForErrors()
        fillBuffer()
        if (DEBUG) debug("Parsed buffer: $buffer")
        performCalculations()
    }

    /**
     * Reads and processes the result from the input stream.
     */
    @Throws(IOException::class)
    protected open suspend fun readResult(input: InputStream) {
        readResultPipeline(input)
    }

    /**
     * Abstract method for subclasses to implement specific calculations based on the buffer.
     */
    protected abstract fun performCalculations()

    /**
     * Fills the buffer by parsing the cleaned hex response.
     */
    protected open fun fillBuffer() {
        val cleanData = CLEAN_PATTERN.replace(rawData, "").trim()

        if (!HEX_PATTERN.matches(cleanData)) {
            if (DEBUG) error("Non-hex response detected: $cleanData")
            throw Obd2Exception.NonHexResponseException(cleanData)
        }

        buffer.clear()
        try {
            if (cleanData.length % 2 == 0) {
                buffer.ensureCapacity(cleanData.length / 2)
                var i = 0
                while (i < cleanData.length) {
                    val hexByte = cleanData.substring(i, i + 2)
                    buffer.add(hexByte.toInt(16))
                    i += 2
                }
            }
        } catch (e: NumberFormatException) {
            if (DEBUG) error("Invalid hex value in response: $cleanData")
            throw IOException("Invalid hex value in response: $cleanData", e)
        }
    }

    /**
     * Reads raw data from the input stream until the prompt '>' or timeout.
     * Uses a StringBuilder for efficiency and adds a timeout to prevent hanging.
     */
    @Throws(IOException::class)
    protected open suspend fun readRawData(input: InputStream) = withContext(Dispatchers.IO) {
        val result = StringBuilder(64)
        val buffer = ByteArray(READ_BUFFER_SIZE)
        
        val timeoutResult = withTimeoutOrNull(adaptiveTimeout) {
            while (true) {
                val bytesRead = input.read(buffer)
                if (bytesRead == -1) break

                for (i in 0 until bytesRead) {
                    val byte = buffer[i].toInt() and 0xFF
                    val char = byte.toChar()
                    if (char == '>') {
                        if (i > 0) {
                            result.append(buffer.decodeToString(0, i))
                        }
                        return@withTimeoutOrNull
                    }
                    result.append(char)
                }
            }
        }
        
        if (timeoutResult == null) {
            error("Timeout (${adaptiveTimeout}ms) while reading response for command: $command")
            throw Obd2Exception.ResponseTimeoutException("Timeout reading response for command: $command")
        }
        rawData = result.toString()
    }

    /**
     * Checks for common OBD-II error messages in the raw response.
     */
    protected fun checkForErrors() {
        val upperData = rawData.uppercase()

        when {
            upperData.contains("UNABLE TO CONNECT") -> {
                error("OBD-II error: UNABLE TO CONNECT (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'UNABLE TO CONNECT'", command)
            }
            upperData.contains("NO DATA") -> {
                error("OBD-II error: NO DATA (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'NO DATA'", command)
            }
            upperData.contains("BUS ERROR") -> {
                error("OBD-II error: BUS ERROR (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'BUS ERROR'", command)
            }
            upperData.contains("CAN ERROR") -> {
                error("OBD-II error: CAN ERROR (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'CAN ERROR'", command)
            }
            upperData.contains("ERROR") && !upperData.contains("NO ERROR") -> {
                error("OBD-II error: ERROR (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'ERROR'", command)
            }
            upperData.contains("STOPPED") -> {
                error("OBD-II error: STOPPED (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'STOPPED'", command)
            }
            upperData.contains("DATA ERROR") -> {
                error("OBD-II error: DATA ERROR (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'DATA ERROR'", command)
            }
            upperData.contains("NO ELM") -> {
                error("OBD-II error: NO ELM (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'NO ELM'", command)
            }
            upperData.contains("BUFFER FULL") -> {
                error("OBD-II error: BUFFER FULL (cmd=$command)")
                throw Obd2Exception.ResponseException("OBD-II response error: 'BUFFER FULL'", command)
            }
        }
    }

    fun getResult(): String = rawData
    abstract fun getFormattedResult(): String
    abstract fun getCalculatedResult(): String
    abstract fun getName(): String

    fun useImperialUnits(): Boolean = useImperialUnits
    open fun getResultUnit(): String = ""
    fun useImperialUnits(isImperial: Boolean) { this.useImperialUnits = isImperial }
    fun getResponseTimeDelay(): Long? = responseDelayMs
    fun setResponseTimeDelay(responseDelayIns: Long?) { this.responseDelayMs = responseDelayIns }

    val commandPID: String get() = if (command.length > 2) command.substring(3) else ""
    val commandMode: String get() = if (command.length >= 2) command.substring(0, 2) else command

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObdCommand) return false
        return command == other.command
    }

    override fun hashCode(): Int = command.hashCode()

    /** ELM/ECU fault strings and parse failures mapped to typed [IOException]s. */
    sealed class Obd2Exception(message: String) : IOException(message) {
        class NonHexResponseException(rawData: String) : Obd2Exception("Non-hex response: $rawData")
        class NonNumericResponseException(rawData: String) : Obd2Exception("Non-numeric response: $rawData")
        class ResponseTimeoutException(message: String) : Obd2Exception(message)
        class ResponseException(message: String, val command: String?) : Obd2Exception(message)
    }

    private fun debug(msg: String) {
        if (DEBUG) Log.d(TAG, msg)
    }

    private fun error(msg: String) {
        if (DEBUG) Log.e(TAG, msg)
    }
}
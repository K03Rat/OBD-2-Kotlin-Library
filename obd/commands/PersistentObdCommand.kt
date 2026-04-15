package com.obd.commands

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds cache state outside the class companion to avoid Kotlin JVM IR backend issues
 * (synthetic accessors + super calls in suspend overrides) on Kotlin 2.0.x.
 */
private object PersistentObdCommandCache {
    private const val TAG = "PersistentObdCommand"
    private const val DEBUG = false

    val cachedRawData: MutableMap<String, String> = ConcurrentHashMap()
    val cachedBuffers: MutableMap<String, List<Int>> = ConcurrentHashMap()

    fun clearCache() {
        cachedRawData.clear()
        cachedBuffers.clear()
        if (DEBUG) Log.d(TAG, "Cache cleared")
    }

    fun debug(msg: String) {
        if (DEBUG) Log.d(TAG, msg)
    }
}

abstract class PersistentObdCommand : ObdCommand {

    /** Constructor from AvailableCommand enum */
    constructor(command: String) : super(command)

    /** Constructor from existing Obd2Command */
    constructor(other: ObdCommand) : super(other)

    /** Generates a unique cache key based on class name and command string */
    private fun cacheKey(): String = "${this::class.simpleName}_$command"

    private fun debug(msg: String) {
        PersistentObdCommandCache.debug(msg)
    }

    /**
     * Reads the response from the vehicle and stores it in the cache.
     */
    @Throws(IOException::class)
    override suspend fun readResult(input: InputStream) {
        readResultPipeline(input)
        val key = cacheKey()
        PersistentObdCommandCache.cachedRawData[key] = rawData
        PersistentObdCommandCache.cachedBuffers[key] = buffer.toList()
        debug("Result cached for key=$key, rawData=$rawData, buffer=${buffer.joinToString()}")
    }

    /**
     * Executes the command. Returns cached values if available.
     */
    @Throws(IOException::class, InterruptedException::class)
    override suspend fun run(input: InputStream, output: OutputStream) {
        val key = cacheKey()
        val rawCache = PersistentObdCommandCache.cachedRawData
        val bufCache = PersistentObdCommandCache.cachedBuffers
        if (rawCache.containsKey(key) && bufCache.containsKey(key)) {
            rawData = rawCache[key] ?: ""
            buffer.clear()
            buffer.addAll(bufCache[key] ?: emptyList())
            performCalculations()
            debug("Cache hit for command=$command, rawData=$rawData, buffer=${buffer.joinToString()}")
        } else {
            debug("Cache miss for command=$command, executing...")
            val startTime = System.currentTimeMillis()
            runCommandPipeline(input, output)
            val elapsed = System.currentTimeMillis() - startTime
            debug("Command executed: command=$command, elapsed=${elapsed}ms")
        }
    }

    companion object {
        /** Clears all cached values. */
        fun clearCache() {
            PersistentObdCommandCache.clearCache()
        }
    }
}

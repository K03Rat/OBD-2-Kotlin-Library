package com.obd.commands

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

/**
 * Container and executor for multiple OBD-II commands.
 */
class ObdMultipleCommand {

    private val commands = mutableListOf<ObdCommand>()
    private val mutex = Mutex()

    companion object {
        private const val TAG = "ObdMultiCommand"
        private const val DEBUG = false
    }

    fun add(command: ObdCommand) {
        commands.add(command)
        debug("Added command: ${command.getName()} (${command.javaClass.simpleName})")
    }

    fun remove(command: ObdCommand) {
        if (commands.remove(command)) {
            debug("Removed command: ${command.getName()}")
        } else {
            debug("Command not found: ${command.getName()}")
        }
    }

    /**
     * Clears all commands in the batch.
     */
    fun clear() {
        commands.clear()
    }

    /**
     * Runs all added commands **sequentially** (thread-safe).
     */
    suspend fun runAll(input: InputStream, output: OutputStream) = withContext(Dispatchers.IO) {
        if (commands.isEmpty()) {
            debug("No commands to execute.")
            return@withContext
        }

        mutex.withLock {
            debug("Executing ${commands.size} OBD-II commands sequentially...")

            for (cmd in commands) {
                try {
                    val start = System.currentTimeMillis()
                    cmd.run(input, output)
                    val end = System.currentTimeMillis()
                    val duration = end - start

                    debug("[Sequential] Command${cmd.getName()} → ${cmd.getFormattedResult()} (${duration}ms)")
                } catch (e: Exception) {
                    error("[Sequential] Command ${cmd.getName()} failed: ${e.message}")
                }
            }
        }
    }

    fun getFormattedResult(): String {
        if (commands.isEmpty()) return ""
        return commands.joinToString(", ") {
            try {
                "${it.getName()}: ${it.getFormattedResult()}"
            } catch (e: Exception) {
                "${it.getName()}: ERR"
            }
        }.also {
            debug("Formatted batch result → $it")
        }
    }

    fun getRawResults(): Map<String, String> {
        return commands.associate { cmd ->
            cmd.getName() to cmd.getResult()
        }
    }

    fun getCommands(): List<ObdCommand> = commands.toList()

    private fun debug(msg: String) {
        if (DEBUG) Log.d(TAG, msg)
    }

    private fun error(msg: String) {
        if (DEBUG) Log.e(TAG, msg)
    }
}


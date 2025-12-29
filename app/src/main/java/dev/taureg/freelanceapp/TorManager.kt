/*
 * Copyright (c) 2025 mittwerk
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package dev.taureg.freelanceapp

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.torproject.jni.TorService

object TorManager {
    var isTorReady by mutableStateOf(false)
        private set

    fun startTor(context: Context) {
        val intent = Intent(context, TorService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        checkTorStatus()
    }

    private fun checkTorStatus() {
        Thread {
            while (!isTorReady) {
                try {
                    val socket = java.net.Socket("127.0.0.1", 9050)
                    socket.close()
                    isTorReady = true
                } catch (e: Exception) {
                    Thread.sleep(2000)
                    e.message
                }
            }
        }.start()
    }
}

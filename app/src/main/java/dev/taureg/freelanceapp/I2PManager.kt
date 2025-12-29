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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.concurrent.thread

object I2PManager {
    var isI2PReady by mutableStateOf(false)
        private set

    fun startI2P(context: Context) {
        val controller = I2PDController(context)

        thread(start = true) {
            controller.startRouter()
            checkI2PStatus()
        }
    }

    private fun checkI2PStatus() {
        while (!isI2PReady) {
            try {
                val socket = java.net.Socket("127.0.0.1", 4444)
                socket.close()
                isI2PReady = true
            } catch (e: Exception) {
                Thread.sleep(3000)
                e.printStackTrace()
            }
        }
    }
}

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
import android.util.Log
import dev.taureg.libi2pd.JNI
import java.io.File
import java.io.FileOutputStream

class I2PDController(
    private val context: Context,
) {
    private val TAG = "I2PD_CONTROLLER"

    private val i2pdDir: File = File(context.filesDir, "i2pd")

    fun startRouter(): String {
        try {
            Log.d(TAG, "Preparing router environment...")
            if (!i2pdDir.exists()) i2pdDir.mkdirs()
            copyAssetsDir("i2pd", i2pdDir)

            JNI.setDataDir(i2pdDir.absolutePath)

            Log.d(TAG, "Calling JNI startDaemon...")
            val result = JNI.startDaemon()
            Log.d(TAG, "JNI Result: $result")

            val webConsole = JNI.getWebConsAddr()
            Log.d(TAG, "I2P Console available at: $webConsole")

            return result ?: "Started"
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start i2pd", e)
            return "Error: ${e.message}"
        }
    }

    fun stopRouter() {
        Log.d(TAG, "Stopping router...")
        JNI.stopDaemon()
    }

    private fun copyAssetsDir(
        assetPath: String,
        targetDir: File,
    ) {
        val assets = context.assets.list(assetPath) ?: return

        if (assets.isEmpty()) {
            Log.w(TAG, "Asset path $assetPath is empty. Check your symlinks!")
        }

        for (item in assets) {
            val fullAssetPath = "$assetPath/$item"
            val targetFile = File(targetDir, item)

            if (!item.contains(".")) {
                targetFile.mkdirs()
                copyAssetsDir(fullAssetPath, targetFile)
            } else {
                if (!targetFile.exists()) {
                    context.assets.open(fullAssetPath).use { input ->
                        FileOutputStream(targetFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }
}

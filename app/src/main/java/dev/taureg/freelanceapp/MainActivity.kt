package dev.taureg.freelanceapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dev.taureg.freelanceapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        I2PManager.startI2P(this)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                    ) {
                        Button(
                            onClick = {
                                lifecycleScope.launch {
                                    try {
                                        val response = api.sayHello()
                                        Toast
                                            .makeText(
                                                this@MainActivity,
                                                "I2P Response: ${response.message}",
                                                Toast.LENGTH_LONG,
                                            ).show()
                                    } catch (e: Exception) {
                                        Log.e("API", "Failed: ${e.message}")
                                        Toast
                                            .makeText(
                                                this@MainActivity,
                                                "Error: ${e.localizedMessage}",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                    }
                                }
                            },
                            enabled = I2PManager.isI2PReady,
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            if (I2PManager.isI2PReady) {
                                Text("Test .i2p API")
                            } else {
                                Text("I2P: Building Tunnels...")
                            }
                        }
                    }
                }
            }
        }
    }
}

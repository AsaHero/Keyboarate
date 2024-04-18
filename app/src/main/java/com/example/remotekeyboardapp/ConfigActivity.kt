package com.example.remotekeyboardapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.Socket

class ConfigActivity : AppCompatActivity() {
    private lateinit var editTextIp: EditText
    private lateinit var editTextPort: EditText
    private lateinit var buttonConnect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        editTextIp = findViewById(R.id.editTextIp)
        editTextPort = findViewById(R.id.editTextPort)
        buttonConnect = findViewById(R.id.buttonConnect)

        buttonConnect.setOnClickListener {
            val ip = editTextIp.text.toString().trim()
            val port = editTextPort.text.toString().toIntOrNull()

            if (port != null) {
                tryToConnect(ip, port)
            } else {
                Toast.makeText(this, "Please enter a valid port number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tryToConnect(ip: String, port: Int) {
        Thread {
            try {
                val socket = Socket(ip, port)
                socket.close()  // Immediately close the socket after checking the connection
                runOnUiThread {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("IP_ADDRESS", ip)
                        putExtra("PORT", port)
                    }
                    startActivity(intent)
                    finish()  // Close the current activity
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Failed to connect: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}

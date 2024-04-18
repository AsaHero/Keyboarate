package com.example.remotekeyboardapp

import android.os.Bundle
import android.widget.Button
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.Socket
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CustomKeyboardView.KeyPressListener {
    private lateinit var customKeyboardView: CustomKeyboardView
    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null
    private lateinit var serverIp: String                 // Now declared lateinit
    private var serverPort: Int = 0                       // Initialized as 0
    private val ioScope = CoroutineScope(Dispatchers.IO)  // Coroutine scope for IO operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverIp = intent.getStringExtra("IP_ADDRESS") ?: "192.168.1.1"  // Default IP
        serverPort = intent.getIntExtra("PORT", 50050)        // Default port

        customKeyboardView = findViewById(R.id.customKeyboardView)
        customKeyboardView.keyPressListener = this
        customKeyboardView.requestFocus()

        setupNetworking()

        val buttonUp = findViewById<Button>(R.id.button_up)
        val buttonDown = findViewById<Button>(R.id.button_down)
        val buttonLeft = findViewById<Button>(R.id.button_left)
        val buttonRight = findViewById<Button>(R.id.button_right)

        buttonUp.setOnClickListener { sendCommand("Up") }
        buttonDown.setOnClickListener { sendCommand("Down") }
        buttonLeft.setOnClickListener { sendCommand("Left") }
        buttonRight.setOnClickListener { sendCommand("Right") }
    }

    private fun setupNetworking() {
        ioScope.launch {
            try {
                socket = Socket(serverIp, serverPort)
                printWriter = PrintWriter(socket!!.getOutputStream(), true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onKeyPress(key: String) {
        Log.d("RemoteKeyboardCommand", "Key pressed: $key")
        sendCommand(key)
    }

    private fun sendCommand(command: String) {
        ioScope.launch {
            if (printWriter != null) {
                printWriter!!.println(command)
                printWriter!!.flush()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        customKeyboardView.requestFocus()  // Ensure the custom view is focused to receive key events
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.launch {
            try {
                printWriter?.close()
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

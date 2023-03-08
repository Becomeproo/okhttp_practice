package com.example.todaynotificationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.serverHostEditText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val informationTextView = findViewById<TextView>(R.id.informationTextView)
        val client = OkHttpClient()
        var serverHost = ""

        editText.addTextChangedListener {
            serverHost = it.toString()
        }

        confirmButton.setOnClickListener {
            val request: Request = Request.Builder()
                .url("http://${serverHost}:8080")
                .build()

            val callback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "수신에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("Client", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val response = response.body?.string()

                        val message = Gson().fromJson(response, Message::class.java)

                        runOnUiThread {
                            informationTextView.visibility = View.VISIBLE
                            informationTextView.text = message.message

                            editText.visibility = View.GONE
                            confirmButton.visibility = View.GONE
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "수신에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            client.newCall(request).enqueue(callback)
        }


//        Thread {
//            try {
//                val socket = Socket("172.30.1.48", 8080)
//                val printer = PrintWriter(socket.getOutputStream())
//                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
//
//                printer.println("GET / HTTP/1.1")
//                printer.println("Host: 127.0.0.1:8080")
//                printer.println("User-Agent: android")
//                printer.println("\r\n")
//                printer.flush()
//
//                var input: String? = "-1"
//                while(input != null) {
//                    input = reader.readLine()
//                    Log.e("Client", input)
//                }
//                reader.close()
//                printer.close()
//                socket.close()
//            } catch (e: Exception) {
//                Log.e("Client", e.toString())
//            }
//        }.start()
    }
}
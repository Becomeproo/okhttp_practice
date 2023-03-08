package com.example.todaynotificationapp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

fun main() {

    Thread {
        val port = 8080
        val server = ServerSocket(port)

        while(true) {
            val socket = server.accept()

            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val printer = PrintWriter(socket.getOutputStream())

            var input: String? = "-1"
            while (input != null && input != "") {
                input = reader.readLine()
            }

            println("READ DATA $input")

            printer.println("HTTP/1.1 200 OK") // 어떤 규격을 사용하는지 명시
            printer.println("Content-Type: text/html\r\n") // header 부분

            printer.println("{\"message\" : \"Hello World\"}") // body 부분
            printer.println("\r\n")
            printer.flush() // 잔여 버퍼가 남아있다면 배출
            printer.close()

            reader.close()

            socket.close()
        }
    }.start()
}
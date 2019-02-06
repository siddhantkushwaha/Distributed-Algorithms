package com.siddhantkushwaha.dc.oddeven

import com.siddhantkushwaha.dc.Message
import java.util.*

fun main() {

    val comparator = Process.Comparator<Int> { data1, data2 -> data1 < data2 }
    val onMessage = object : Message.OnMessage<Int> {
        override fun onSend(processNumber: Int, data: Int?, roundNumber: Int) {

        }

        override fun onReceive(processNumber: Int, data: Int?, roundNumber: Int) {

        }
    }

    val n = 5
    val processes = Array<Process<Int>?>(n) { null }
    val messages = Array(2) { Array<Message<Int>?>(2) { null } }
    messages[1][0] = Message(onMessage)
    messages[1][1] = Message(onMessage)

    for (i in 1 until n + 1) {
        processes[i - 1] = Process(Random().nextInt(n * 1000), i, n, messages, comparator)

        if (i < n) {
            messages[0][0] = messages[1][0]
            messages[0][1] = messages[1][1]
            if (i < n - 1) {
                messages[1][0] = Message(onMessage)
                messages[1][1] = Message(onMessage)
            } else {
                messages[1][0] = null
                messages[1][1] = null
            }
        }
    }

    System.out.println("Initial Sequence ->")
    for (process in processes)
        System.out.printf("%d ", process?.data)
    System.out.println()

    for (process in processes)
        process?.start()
}
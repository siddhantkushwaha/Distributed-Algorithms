package com.siddhantkushwaha.dc.sasaki

import com.siddhantkushwaha.dc.Channel
import com.siddhantkushwaha.dc.Comparator
import com.siddhantkushwaha.dc.ProcessOutput

class Process<T>(data: T, mark: Int, private val processNumber: Int, private val n: Int, private val channels: Array<Channel<Data<T>?>?>, private val comparator: Comparator<T>, private val processOutput: ProcessOutput<T>) : Runnable {

    private var data1: Data<T>? = null
    private var data2: Data<T>? = null

    private var area: Int = 0

    init {
        when (mark) {
            -1 -> {
                data2 = Data()
                data2?.data = data
                data2?.marked = true
            }

            0 -> {
                data1 = Data()
                data1?.data = data
                data1?.marked = true

                data2 = Data()
                data2?.data = data
                data2?.marked = true
            }

            1 -> {
                data1 = Data()
                data1?.data = data
                data1?.marked = true
            }
        }
    }

    fun start() {
        Thread(this@Process, "P$processNumber").start()
    }

    override fun run() {

        for (roundNumber in 1 until n) {

            var t1: Thread? = null
            var t2: Thread? = null

            if (data1 != null) {
                t1 = Thread(Runnable {

                    val newData = channels[0]!!.receive(processNumber, processNumber - 1, roundNumber)
                    if (comparator.compare(data1!!.data, newData!!.data)) {

                        val oldData = data1
                        data1 = newData

                        if (data1!!.marked)
                            area--

                        if (oldData!!.marked)
                            area++

                        channels[1]!!.send(processNumber, processNumber - 1, oldData, roundNumber)
                    } else
                        channels[1]!!.send(processNumber, processNumber - 1, newData, roundNumber)

                }, "P$processNumber-1")
                t1.start()
            }

            if (data2 != null) {
                t2 = Thread(Runnable {

                    channels[2]!!.send(processNumber, processNumber + 1, data2, roundNumber)
                    data2 = channels[3]!!.receive(processNumber, processNumber + 1, roundNumber)

                }, "P$processNumber-2")
                t2.start()
            }

            t1?.join()
            t2?.join()

            if (comparator.compare(data2?.data ?: continue, data1?.data ?: continue)) {
                val temp = data1
                data1 = data2
                data2 = temp
            }

            processOutput.onRoundComplete(processNumber, data1?.data, data2?.data, area, roundNumber)
        }

        processOutput.onFinish(processNumber, data1?.data, data2?.data, area, n - 1)
    }
}
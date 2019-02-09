package com.siddhantkushwaha.dc.sasaki

import com.siddhantkushwaha.dc.Channel
import com.siddhantkushwaha.dc.Comparator

class Systemkt<T>(data: T, mark: Int, private val processNumber: Int, private val n: Int, private val channels: Array<Array<Channel<Data<T>?>?>>, private val comparator: Comparator<T>) : Runnable {

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
        Thread(this@Systemkt, "P$processNumber").start()
    }

    override fun run() {

        for (roundNumber in 1 until n) {

            var t1: Thread? = null
            var t2: Thread? = null

            if (data1 != null) {
                t1 = Thread(Runnable {

                    val newData = channels[0][0]!!.receive(processNumber, roundNumber)
                    if (comparator.compare(data1!!.data, newData!!.data)) {

                        val oldData = data1
                        data1 = newData

                        if (data1!!.marked)
                            area--

                        if (oldData!!.marked)
                            area++

                        channels[0][1]!!.send(processNumber, oldData, roundNumber)
                    } else
                        channels[0][1]!!.send(processNumber, newData, roundNumber)

                }, "P$processNumber-1")
                t1.start()
            }

            if (data2 != null) {
                t2 = Thread(Runnable {

                    channels[1][0]!!.send(processNumber, data2, roundNumber)
                    data2 = channels[1][1]!!.receive(processNumber, roundNumber)

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
        }

        println("Process-$processNumber -> ${data1?.data ?: "null"} ${data2?.data ?: "null"} --- $area")
    }
}
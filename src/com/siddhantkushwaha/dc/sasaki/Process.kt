package com.siddhantkushwaha.dc.sasaki

import com.siddhantkushwaha.dc.Channel
import com.siddhantkushwaha.dc.Comparator
import com.siddhantkushwaha.dc.ProcessOutput

/*
    This class represents a typical process in Sasaki's, how it executes.
    This class implements runnable since each instance of this class needs to
    run on a different thread for parallel execution.
*/

class Process<T>(data: T, mark: Int, private val processNumber: Int, private val n: Int, private val channels: Array<Channel<Data<T>?>?>, private val comparator: Comparator<T>, private val processOutput: ProcessOutput<T>) : Runnable {

    private var data1: Data<T>? = null
    private var data2: Data<T>? = null

    private var area: Int = 0

    init {

        /* Assigning the data values based on the position */
        when (mark) {
            -1 -> {
                data2 = Data()
                data2?.data = data
                data2?.marked = true
            }

            0 -> {
                data1 = Data()
                data1?.data = data
                data1?.marked = false

                data2 = Data()
                data2?.data = data
                data2?.marked = false
            }

            1 -> {
                data1 = Data()
                data1?.data = data
                data1?.marked = true
            }
        }
    }

    fun start() {

        /* The below line calls the run method defined below on a new thread */
        Thread(this@Process, "P$processNumber").start()
    }

    override fun run() {

        for (roundNumber in 1 until n) {


            /* both send and receive on either of the sockets happen parallel to each other so they are executed on two different threads */
            var t1: Thread? = null
            var t2: Thread? = null

            /* wait for receive on the left-socket if it's not null */
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

            /* send data available on the right socket if it's not null */
            if (data2 != null) {
                t2 = Thread(Runnable {

                    channels[2]!!.send(processNumber, processNumber + 1, data2, roundNumber)
                    data2 = channels[3]!!.receive(processNumber, processNumber + 1, roundNumber)

                }, "P$processNumber-2")
                t2.start()
            }

            /* wait for both the sockets to receive and send data respectively*/
            t1?.join()
            t2?.join()

            /* swap data if needed */
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
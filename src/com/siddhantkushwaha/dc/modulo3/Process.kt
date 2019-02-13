package com.siddhantkushwaha.dc.modulo3

/*
    This class represents a typical process in an alternative n-1 rounds algorithm, also called Modulo3 algorithm, how it executes.
    This class implements runnable since each instance of this class needs to
    run on a different thread for parallel execution.
*/

import com.siddhantkushwaha.dc.Channel
import com.siddhantkushwaha.dc.Comparator
import com.siddhantkushwaha.dc.ProcessOutput

class Process<T>(private var data: T, private val processNumber: Int, private val n: Int, private val channels: Array<Channel<T?>?>, private val comparator: Comparator<T>, private val processOutput: ProcessOutput<T>) : Runnable {


    private val leftReceiveChannel = channels[0]
    private val leftSendChannel = channels[1]
    private val rightSendChannel = channels[2]
    private val rightReceiveChannel = channels[3]


    fun start() {

        /* The below line calls the run method defined below on a new thread */
        Thread(this@Process, "P$processNumber").start()
    }

    override fun run() {

        var idx = processNumber
        for (roundNumber in 1 until n) {
            idx %= 3

            /* defining a task based on the index */
            when (idx) {

                /* when the process is a right process */
                0 -> {
                    if (leftSendChannel != null)
                        leftSendChannel.send(processNumber, processNumber - 1, data, roundNumber)

                    if (leftReceiveChannel != null)
                        data = leftReceiveChannel.receive(processNumber, processNumber - 1, roundNumber)!!
                }

                /* when the process is a left process */
                1 -> {
                    if (rightSendChannel != null)
                        rightSendChannel.send(processNumber, processNumber + 1, data, roundNumber)

                    if (rightReceiveChannel != null)
                        data = rightReceiveChannel.receive(processNumber, processNumber + 1, roundNumber)!!
                }


                /* when the process is a middle process */
                2 -> {
                    val dataArr = ArrayList<T>()
                    dataArr.add(data)

                    /* since we need to receive from two ends simultaneously, we run these tasks on two different threads */
                    var t1: Thread? = null
                    var t2: Thread? = null

                    if (leftReceiveChannel != null) {
                        t1 = Thread(Runnable {
                            dataArr.add(leftReceiveChannel.receive(processNumber, processNumber - 1, roundNumber)!!)
                        }, "P$processNumber-1")
                        t1.start()
                    }

                    if (rightReceiveChannel != null) {
                        t2 = Thread(Runnable {
                            dataArr.add(rightReceiveChannel.receive(processNumber, processNumber + 1, roundNumber)!!)
                        }, "P$processNumber-2")
                        t2.start()
                    }

                    /* wait for receive from both ends */
                    t1?.join()
                    t2?.join()

                    /* sort based on comparator */
                    dataArr.sortWith(kotlin.Comparator { o1, o2 ->
                        if (comparator.compare(o1, o2))
                            -1
                        else
                            1
                    })

                    when {

                        leftSendChannel != null && rightSendChannel != null -> {

                            data = dataArr[1]!!

                            /* since we need to send back to two ends simultaneously, we run these tasks on two different threads */
                            val t3 = Thread(Runnable {
                                leftSendChannel.send(processNumber, processNumber - 1, dataArr[0]!!, roundNumber)
                            }, "P$processNumber-3")
                            t3.start()

                            val t4 = Thread(Runnable {
                                rightSendChannel.send(processNumber, processNumber + 1, dataArr[2]!!, roundNumber)
                            }, "P$processNumber-4")
                            t4.start()

                            t3.join()
                            t4.join()
                        }

                        leftSendChannel != null -> {

                            data = dataArr[1]!!
                            leftSendChannel.send(processNumber, processNumber - 1, dataArr[0]!!, roundNumber)
                        }

                        rightSendChannel != null -> {

                            data = dataArr[0]!!
                            rightSendChannel.send(processNumber, processNumber + 1, dataArr[1]!!, roundNumber)
                        }
                    }
                }

                else -> throw RuntimeException("idx error")
            }
            idx += 2

            processOutput.onRoundComplete(processNumber, data, data, 0, roundNumber)
        }

        processOutput.onFinish(processNumber, data, data, 0, n - 1)
    }
}
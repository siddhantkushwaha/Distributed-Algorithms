package com.siddhantkushwaha.dc.modulo3

import com.siddhantkushwaha.dc.Channel
import com.siddhantkushwaha.dc.Comparator

class Process<T>(private var data: T, private val processNumber: Int, private val n: Int, private val channels: Array<Channel<T?>?>, private val comparator: Comparator<T>) : Runnable {

    var idx = processNumber;

    val leftReceiveChannel = channels[0]
    val leftSendChannel = channels[1]
    val rightSendChannel = channels[2]
    val rightReceiveChannel = channels[3]


    fun start() {
        Thread(this@Process, "P$processNumber").start()
    }

    override fun run() {

        for (roundNumber in 1 until n) {

            idx %= 3
            when (idx) {


                0 -> {
                    if (leftSendChannel != null)
                        leftSendChannel.send(processNumber, data, roundNumber)

                    if (leftReceiveChannel != null)
                        data = leftReceiveChannel.receive(processNumber, roundNumber)!!
                }

                1 -> {
                    if (rightSendChannel != null)
                        rightSendChannel.send(processNumber, data, roundNumber)

                    if (rightReceiveChannel != null)
                        data = rightReceiveChannel.receive(processNumber, roundNumber)!!
                }

                2 -> {
                    val dataArr = ArrayList<T>()
                    dataArr.add(data)

                    var t1: Thread? = null
                    var t2: Thread? = null

                    if (leftReceiveChannel != null) {
                        t1 = Thread(Runnable {
                            dataArr.add(leftReceiveChannel.receive(processNumber, roundNumber)!!)
                        }, "P$processNumber-1")
                        t1.start()
                    }

                    if (rightReceiveChannel != null) {
                        t2 = Thread(Runnable {
                            dataArr.add(rightReceiveChannel.receive(processNumber, roundNumber)!!)
                        }, "P$processNumber-2")
                        t2.start()
                    }

                    t1?.join()
                    t2?.join()

                    dataArr.sortWith(kotlin.Comparator { o1, o2 ->
                        if (comparator.compare(o1, o2))
                            -1
                        else
                            1
                    })

                    when {

                        leftSendChannel != null && rightSendChannel != null -> {

                            data = dataArr[1]!!

                            val t3 = Thread(Runnable {
                                leftSendChannel.send(processNumber, dataArr[0]!!, roundNumber)
                            }, "P$processNumber-3")
                            t3.start()

                            val t4 = Thread(Runnable {
                                rightSendChannel.send(processNumber, dataArr[2]!!, roundNumber)
                            }, "P$processNumber-4")
                            t4.start()

                            t3.join()
                            t4.join()
                        }

                        leftSendChannel != null -> {

                            data = dataArr[1]!!
                            leftSendChannel.send(processNumber, dataArr[0]!!, roundNumber)
                        }

                        rightSendChannel != null -> {

                            data = dataArr[0]!!
                            rightSendChannel.send(processNumber, dataArr[1]!!, roundNumber)
                        }
                    }
                }

                else -> throw RuntimeException("idx error")
            }

            idx++
        }

        println("Process-$processNumber -> $data")
    }
}
package com.siddhantkushwaha.dc.sasaki;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

public class System<T> implements Runnable {

    private int n;
    private int area;
    private int roundNumber;
    private int processNumber;
    private Data<T> data1;
    private Data<T> data2;
    private Channel<Data<T>>[][] channels;

    private Comparator<T> comparator;

    System(T data, int mark, int processNumber, int n, Channel<Data<T>>[][] channels, Comparator<T> comparator) {

        this.n = n;

        this.area = 0;

        if (mark == -1) {
            this.data2 = new Data<>();
            this.data2.data = data;
            this.data2.marked = true;
        } else if (mark == 1) {
            this.data1 = new Data<>();
            this.data1.data = data;
            this.data1.marked = true;
        } else {
            this.data1 = new Data<>();
            this.data2 = new Data<>();
            this.data1.data = data;
            this.data2.data = data;
        }

        this.roundNumber = 1;
        this.processNumber = processNumber;

        this.channels = channels;

        this.comparator = comparator;
    }

    void start() {
        Thread t = new Thread(this, "P" + processNumber);
        t.start();
    }

    @Override
    public void run() {

        while (roundNumber <= n - 1) {

            Thread t1 = null, t2 = null;

            if (data1 != null) {
                t1 = new Thread(() -> {

                    Data<T> newData = channels[0][0].receive(processNumber, roundNumber);

                    if (comparator.compare(data1.data, newData.data)) {
                        Data<T> oldData = data1;
                        data1 = newData;

                        if (data1.marked)
                            area--;
                        if (oldData.marked)
                            area++;

                        channels[0][1].send(processNumber, oldData, roundNumber);
                    } else {
                        channels[0][1].send(processNumber, newData, roundNumber);
                    }

                }, "P" + processNumber + "-1");
                t1.start();
            }

            if (data2 != null) {
                t2 = new Thread(() -> {

                    channels[1][0].send(processNumber, data2, roundNumber);
                    data2 = channels[1][1].receive(processNumber, roundNumber);

                }, "P" + processNumber + "-2");
                t2.start();
            }

            if (t1 != null) {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (t2 != null) {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (data1 != null && data2 != null) {

                if (comparator.compare(data2.data, data1.data)) {
                    Data<T> temp = data1;
                    data1 = data2;
                    data2 = temp;
                }
            }

            roundNumber++;
        }

        String d1 = "null";
        if (data1 != null)
            d1 = String.valueOf(data1.data);

        String d2 = "null";
        if (data2 != null)
            d2 = String.valueOf(data2.data);

        java.lang.System.out.println("Process-" + processNumber + " -> " + d1 + " " + d2 + " --- " + area);
    }
}

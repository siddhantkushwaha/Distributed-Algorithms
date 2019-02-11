package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

public class Process<T> implements Runnable {

    private int n;
    private T data;
    private int roundNumber;
    private int processNumber;
    private Channel<T>[] channels;

    private Comparator<T> comparator;

    Process(T data, int processNumber, int n, Channel<T>[] channels, Comparator<T> comparator) {

        this.n = n;
        this.data = data;
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

        while (roundNumber <= n) {

            int val = 0;
            val = val | (roundNumber & 1);
            val = val | ((processNumber & 1) << 1);

            if (val == 0 || val == 3) {
                if (channels[2] != null) {

                    channels[2].send(processNumber, processNumber + 1, data, roundNumber);
                    data = channels[3].receive(processNumber, processNumber + 1, roundNumber);
                }
            } else {
                if (channels[0] != null) {

                    T newData = channels[0].receive(processNumber, processNumber - 1, roundNumber);
                    if (comparator.compare(data, newData)) {
                        T oldData = data;
                        data = newData;
                        channels[1].send(processNumber, processNumber - 1, oldData, roundNumber);
                    } else
                        channels[1].send(processNumber, processNumber - 1, newData, roundNumber);
                }
            }

            roundNumber++;
        }

        System.out.println("Completed, Process " + processNumber + ", Data -> " + data + ", Round Number -> " + (roundNumber - 1));
    }

    T getData() {
        return data;
    }
}
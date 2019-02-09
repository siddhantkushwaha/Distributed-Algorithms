package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

public class System<T> implements Runnable {

    private int n;
    private T data;
    private int roundNumber;
    private int processNumber;
    private Channel<T>[] channels;

    private Comparator<T> comparator;

    System(T data, int processNumber, int n, Channel<T>[] channels, Comparator<T> comparator) {

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

                    channels[2].send(processNumber, data, roundNumber);
                    data = channels[3].receive(processNumber, roundNumber);
                }
            } else {
                if (channels[0] != null) {

                    T newData = channels[0].receive(processNumber, roundNumber);
                    if (comparator.compare(data, newData)) {
                        T oldData = data;
                        data = newData;
                        channels[1].send(processNumber, oldData, roundNumber);
                    } else
                        channels[1].send(processNumber, newData, roundNumber);
                }
            }

            roundNumber++;
        }

        java.lang.System.out.println("Completed, System " + processNumber + ", Data -> " + data + ", Round Number -> " + (roundNumber - 1));
    }

    T getData() {
        return data;
    }
}
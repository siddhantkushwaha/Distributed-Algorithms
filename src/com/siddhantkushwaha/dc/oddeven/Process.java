package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Message;

public class Process<T> implements Runnable {

    private int n;
    private T data;
    private int roundNumber;
    private int processNumber;
    private Message<T>[][] messages = new Message[2][2];

    private Comparator<T> comparator;

    public Process(T data, int processNumber, int n, Message<T>[][] messages, Comparator<T> comparator) {

        this.n = n;
        this.data = data;
        this.roundNumber = 1;
        this.processNumber = processNumber;

        this.messages[0][0] = messages[0][0];
        this.messages[0][1] = messages[0][1];
        this.messages[1][0] = messages[1][0];
        this.messages[1][1] = messages[1][1];

        this.comparator = comparator;
    }

    public void start() {
        new Thread(this, "P" + processNumber).start();
    }

    @Override
    public void run() {

        while (roundNumber <= n) {

            int val = 0;
            val = val | (roundNumber & 1);
            val = val | ((processNumber & 1) << 1);

            if (val == 0 || val == 3) {
                if (messages[1][0] != null) {

                    messages[1][0].send(processNumber, data, roundNumber);
                    data = messages[1][1].receive(processNumber, roundNumber);

                }
            } else {
                if (messages[0][0] != null) {

                    T newData = messages[0][0].receive(processNumber, roundNumber);
                    if (comparator.compare(data, newData)) {
                        T oldData = data;
                        data = newData;
                        messages[0][1].send(processNumber, oldData, roundNumber);
                    } else
                        messages[0][1].send(processNumber, newData, roundNumber);
                }
            }

            // System.out.printf("P%d,  Round -> %d, Value -> %d\n", processNumber, roundNumber, data);

            roundNumber++;
        }

        System.out.println("Completed Process " + processNumber + ", Data -> " + data + ", Round Number -> " + (roundNumber - 1));
    }

    public T getData() {
        return data;
    }

    public interface Comparator<T> {
        boolean compare(T data1, T data2);
    }
}
package com.siddhantkushwaha.dc;

public class Message<T> {

    private T data;
    private boolean flag = false;

    private OnMessage<T> onMessage;

    public Message(OnMessage<T> onMessage) {
        this.onMessage = onMessage;
    }

    public synchronized void send(int processNumber, T data, int roundNumber) {

        if (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onMessage.onSend(processNumber, data, roundNumber);

        this.data = data;
        flag = true;
        notify();
    }

    public synchronized T receive(int processNumber, int roundNumber) {

        if (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onMessage.onReceive(processNumber, data, roundNumber);

        flag = false;
        notify();

        return data;
    }

    public interface OnMessage<T> {

        void onSend(int processNumber, T data, int roundNumber);

        void onReceive(int processNumber, T data, int roundNumber);
    }
}

package com.siddhantkushwaha.dc;

public class Channel<T> {

    private T data;
    private boolean flag = false;

    private OnMessage<T> onMessage;

    public Channel(OnMessage<T> onMessage) {
        this.onMessage = onMessage;
    }

    public synchronized void send(int sourceProcess, int destinationProcess, T data, int roundNumber) {

        if (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onMessage.onSend(sourceProcess, destinationProcess, data, roundNumber);

        this.data = data;
        flag = true;
        notify();
    }

    public synchronized T receive(int sourceProcess, int destinationProcess, int roundNumber) {

        if (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onMessage.onReceive(sourceProcess, destinationProcess, data, roundNumber);

        flag = false;
        notify();

        return data;
    }

    public interface OnMessage<T> {

        void onSend(int sourceProcess, int destinationProcess, T data, int roundNumber);

        void onReceive(int sourceProcess, int destinationProcess, T data, int roundNumber);
    }
}

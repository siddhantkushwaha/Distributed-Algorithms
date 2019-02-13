package com.siddhantkushwaha.dc;

/*
    This class represents a Channel from what we know in distributed sorting.
    It is a bidirectional channel
*/

public class Channel<T> {

    private T data;
    private boolean flag = false;

    private OnMessage<T> onMessage;

    public Channel(OnMessage<T> onMessage) {
        this.onMessage = onMessage;
    }

    /* method to send a message over an instance of Channel */
    public synchronized void send(int sourceProcess, int destinationProcess, T data, int roundNumber) {

        /* wait for send if a previous message not already received */
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

    /* method to receive a message over an instance of Channel */
    public synchronized T receive(int sourceProcess, int destinationProcess, int roundNumber) {

        /* Wait for receive if message not already sent */
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

    /*
        This interface defines methods to print output in successful transmission of messages
    */
    public interface OnMessage<T> {

        /* This method is called when a message is successfully sent over a channel */
        void onSend(int sourceProcess, int destinationProcess, T data, int roundNumber);

        /* This method is called when a message is successfully received over a channel */
        void onReceive(int sourceProcess, int destinationProcess, T data, int roundNumber);
    }
}

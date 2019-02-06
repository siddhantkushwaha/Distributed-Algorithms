public class Message {

    private int data = -1;
    private boolean flag = false;

    private OnMessage onMessage;

    public Message(OnMessage onMessage) {
        this.onMessage = onMessage;
    }

    public synchronized void send(int processNumber, int data, int roundNumber) {

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

    public synchronized int receive(int processNumber, int roundNumber) {

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

    public interface OnMessage {

        void onSend(int processNumber, int data, int roundNumber);

        void onReceive(int processNumber, int data, int roundNumber);
    }
}

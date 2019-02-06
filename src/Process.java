public class Process implements Runnable {

    private int n;
    private int data;
    private int roundNumber;
    private int processNumber;
    private Message[][] messages = new Message[2][2];

    private OnProcessRun onProcessRun;

    public Process(int data, int processNumber, int n, Message[][] messages, OnProcessRun onProcessRun) {

        this.n = n;
        this.data = data;
        this.roundNumber = 1;
        this.processNumber = processNumber;

        this.messages[0][0] = messages[0][0];
        this.messages[0][1] = messages[0][1];
        this.messages[1][0] = messages[1][0];
        this.messages[1][1] = messages[1][1];

        this.onProcessRun = onProcessRun;
    }

    public void start() {
        new Thread(this, "P" + processNumber).start();
    }

    @Override
    public void run() {

        onProcessRun.run(data, processNumber, roundNumber, n, messages);
    }

    public interface OnProcessRun {
        void run(int data, int processNumber, int roundNumber, int n, Message[][] messages);
    }

    public int getData() {
        return data;
    }
}
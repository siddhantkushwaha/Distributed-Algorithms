import java.util.Random;

public class OddEven {

    public static void main(String[] args) {

        Process.OnProcessRun<Integer> onProcessRun = (data, processNumber, roundNumber, n, messages) -> {

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

                        Integer newData = messages[0][0].receive(processNumber, roundNumber);
                        if (newData > data) {
                            int oldData = data;
                            data = newData;
                            messages[0][1].send(processNumber, oldData, roundNumber);
                        } else
                            messages[0][1].send(processNumber, newData, roundNumber);
                    }
                }

                // System.out.printf("P%d,  Round -> %d, Value -> %d\n", processNumber, roundNumber-1, data);

                roundNumber++;
            }

            System.out.printf("P%d,  Round -> %d, Value -> %d\n", processNumber, roundNumber - 1, data);
        };

        Message.OnMessage<Integer> onMessage = new Message.OnMessage<Integer>() {
            @Override
            public void onSend(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("Process P%d has sent %d in round %d\n", processNumber, data, roundNumber);
            }

            @Override
            public void onReceive(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("Process P%d has received %d in round %d\n", processNumber, data, roundNumber);
            }
        };


        int n = 5;
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // pass
        }

        Message<Integer>[][] messages = new Message[2][2];
        messages[1][0] = new Message<>(onMessage);
        messages[1][1] = new Message<>(onMessage);

        Process<Integer>[] processes = new Process[n];
        for (int i = 1; i <= n; i++) {

            processes[i - 1] = new Process<>(new Random().nextInt(n * 100), i, n, messages, onProcessRun);

            if (i < n) {
                messages[0][0] = messages[1][0];
                messages[0][1] = messages[1][1];
                if (i < n - 1) {
                    messages[1][0] = new Message<>(onMessage);
                    messages[1][1] = new Message<>(onMessage);
                } else {
                    messages[1][0] = null;
                    messages[1][1] = null;
                }
            }
        }

        System.out.print("Initial Sequence -> ");
        for (Process<Integer> process1 : processes) {
            System.out.printf("%d ", process1.getData());
        }
        System.out.println();

        for (Process process : processes)
            process.start();
    }
}

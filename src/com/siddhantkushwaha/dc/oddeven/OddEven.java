package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Message;

import java.util.Random;

public class OddEven {

    public static void main(String[] args) {

        Process.Comparator<Integer> comparator = (data1, data2) -> data1 < data2;

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

            processes[i - 1] = new Process<>(new Random().nextInt(n * 100), i, n, messages, comparator);

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

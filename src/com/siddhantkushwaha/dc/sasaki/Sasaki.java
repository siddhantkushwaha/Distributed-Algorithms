package com.siddhantkushwaha.dc.sasaki;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;
import com.siddhantkushwaha.dc.ProcessOutput;

import java.util.Arrays;
import java.util.HashMap;

public class Sasaki {

    public static void sasaki(int[] arr, boolean order, boolean printEachRoundResult, boolean printSendReceiveMessages) {

        Comparator<Integer> comparator = (data1, data2) -> order ^ (data1 < data2);

        Channel.OnMessage<Data<Integer>> onMessage = new Channel.OnMessage<Data<Integer>>() {
            @Override
            public void onSend(int sourceProcess, int destinationProcess, Data<Integer> data, int roundNumber) {

                if (printSendReceiveMessages)
                    System.out.printf("P%d sent %d to P%d in round %d\n", sourceProcess, data.data, destinationProcess, roundNumber);
            }

            @Override
            public void onReceive(int sourceProcess, int destinationProcess, Data<Integer> data, int roundNumber) {

                if (printSendReceiveMessages)
                    System.out.printf("P%d received %d from P%d in round %d\n", sourceProcess, data.data, destinationProcess, roundNumber);
            }
        };

        HashMap<Integer, Integer> fin = new HashMap<>();
        ProcessOutput<Integer> processOutput = new ProcessOutput<Integer>() {
            @Override
            public void onRoundComplete(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

                if (printEachRoundResult)
                    System.out.printf("P%d, Round - %d, data -> %d %d, area -> %d\n", processNumber, roundNumber, leftData, rightData, area);
            }

            @Override
            public void onFinish(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

                System.out.printf("Finished P%d, Round - %d, data -> %d %d, area - %d\n", processNumber, roundNumber, leftData, rightData, area);

                if (leftData == null || area < 0)
                    fin.put(processNumber, rightData);
                else
                    fin.put(processNumber, leftData);

                if (fin.size() == arr.length) {
                    System.out.println("\nFinal Output ->");
                    for (int i = 1; i <= arr.length; i++) {
                        System.out.printf("%d ", fin.get(i));
                    }
                    System.out.println();
                }
            }
        };

        int n = arr.length;

        Channel[] channels = new Channel[2 * (n + 1)];
        for (int i = 2; i <= channels.length - 3; i++)
            channels[i] = new Channel<>(onMessage);

        Process[] processes = new Process[n];
        for (int i = 0, k = 0; i < n; i++, k += 2) {

            int mark = 0;
            if (i == 0)
                mark = -1;
            else if (i == n - 1)
                mark = 1;

            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            processes[i] = new Process<Integer>(arr[i], mark, i + 1, n, _channels, comparator, processOutput);
        }

        for (Process process : processes) {
            process.start();
        }
    }
}

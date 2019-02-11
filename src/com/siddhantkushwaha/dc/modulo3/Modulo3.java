package com.siddhantkushwaha.dc.modulo3;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;
import com.siddhantkushwaha.dc.ProcessOutput;

import java.util.Arrays;
import java.util.HashMap;

public class Modulo3 {

    public static void modulo3(int[] arr, boolean order, boolean printEachRoundResult, boolean printSendReceiveMessages) {
        Comparator<Integer> comparator = (data1, data2) -> order ^ (data1 < data2);

        Channel.OnMessage<Integer> onMessage = new Channel.OnMessage<Integer>() {
            @Override
            public void onSend(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {

                if (printSendReceiveMessages)
                    System.out.printf("P%d sent %d to P%d in round %d\n", sourceProcess, data, destinationProcess, roundNumber);
            }

            @Override
            public void onReceive(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {

                if (printSendReceiveMessages)
                    System.out.printf("P%d received %d from P%d in round %d\n", sourceProcess, data, destinationProcess, roundNumber);
            }
        };

        HashMap<Integer, Integer> fin = new HashMap<>();
        ProcessOutput<Integer> processOutput = new ProcessOutput<Integer>() {
            @Override
            public void onRoundComplete(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

                if (printEachRoundResult)
                    System.out.printf("P%d, Round - %d, data -> %d\n", processNumber, roundNumber, leftData);
            }

            @Override
            public void onFinish(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

                System.out.printf("Finished P%d, Round - %d, data -> %d\n", processNumber, roundNumber, leftData);

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
            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            processes[i] = new Process<Integer>(arr[i], i + 1, n, _channels, comparator, processOutput);
        }

        System.out.println("Starting..");
        for (Process process : processes)
            process.start();
    }
}
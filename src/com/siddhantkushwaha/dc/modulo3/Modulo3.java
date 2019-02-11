package com.siddhantkushwaha.dc.modulo3;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;
import com.siddhantkushwaha.dc.ProcessOutput;

import java.util.Arrays;

public class Modulo3 {

    public static void modulo3(int[] arr, boolean order, boolean printSendReceiveMessages) {
        Comparator<Integer> comparator = (data1, data2) -> order ^ (data1 < data2);

        Channel.OnMessage<Integer> onMessage = new Channel.OnMessage<Integer>() {
            @Override
            public void onSend(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {
                if (printSendReceiveMessages)
                    System.out.println("");
            }

            @Override
            public void onReceive(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {
                if (printSendReceiveMessages)
                    System.out.println("");
            }
        };

        ProcessOutput<Integer> processOutput = new ProcessOutput<Integer>() {
            @Override
            public void onRoundComplete(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

            }

            @Override
            public void onFinish(int processNumber, Integer leftData, Integer rightData, int area, int roundNumber) {

                System.out.printf("Process P%d finished -> %d in round %d", processNumber, leftData, roundNumber);
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

        for (Process process : processes)
            process.start();
    }
}
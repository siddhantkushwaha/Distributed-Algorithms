package com.siddhantkushwaha.dc.sasaki;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;
import com.siddhantkushwaha.dc.ProcessOutput;

import java.util.Arrays;

public class Sasaki {

    public static void sasaki(int[] arr, boolean order, boolean printSendReceiveMessages) {

        Comparator<Integer> comparator = (data1, data2) -> order ^ (data1 < data2);

        Channel.OnMessage<Data<Integer>> onMessage = new Channel.OnMessage<Data<Integer>>() {
            @Override
            public void onSend(int sourceProcess, int destinationProcess, Data<Integer> data, int roundNumber) {

                if (printSendReceiveMessages)
                    System.out.println("");
            }

            @Override
            public void onReceive(int sourceProcess, int destinationProcess, Data<Integer> data, int roundNumber) {
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

                System.out.printf("Process P%d finished -> %d %d, area - %d, in round %d\n", processNumber, leftData, rightData, area, roundNumber);
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

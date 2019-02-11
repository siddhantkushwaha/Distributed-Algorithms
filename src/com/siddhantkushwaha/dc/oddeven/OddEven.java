package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

import java.util.Arrays;

public class OddEven {

    public static void oddeven(int[] arr) {
        Comparator<Integer> comparator = (data1, data2) -> data1 < data2;

        Channel.OnMessage<Integer> onMessage = new Channel.OnMessage<Integer>() {
            @Override
            public void onSend(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {

            }

            @Override
            public void onReceive(int sourceProcess, int destinationProcess, Integer data, int roundNumber) {

            }
        };

        int n = arr.length;

        Channel[] channels = new Channel[2 * (n + 1)];
        for (int i = 2; i <= channels.length - 3; i++)
            channels[i] = new Channel<>(onMessage);

        Process[] processes = new Process[n];
        for (int i = 0, k = 0; i < n; i++, k += 2) {
            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            processes[i] = new Process<Integer>(arr[i], i + 1, n, _channels, comparator);
        }

        for (Process process : processes)
            System.out.printf("%d ", (Integer) process.getData());
        System.out.println();

        for (Process process : processes)
            process.start();
    }
}

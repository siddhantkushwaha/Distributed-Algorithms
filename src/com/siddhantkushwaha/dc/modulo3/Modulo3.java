package com.siddhantkushwaha.dc.modulo3;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

import java.util.Arrays;

public class Modulo3 {

    public static void main(String[] args) {

        Comparator<Integer> comparator = (data1, data2) -> data1 < data2;

        Channel.OnMessage<Integer> onMessage = new Channel.OnMessage<Integer>() {
            @Override
            public void onSend(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("Process P%d has sent %d in round %d\n", processNumber, data, roundNumber);
            }

            @Override
            public void onReceive(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("Process P%d has received %d in round %d\n", processNumber, data, roundNumber);
            }
        };

        int n = 500;
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // pass
        }

        Channel[] channels = new Channel[2 * (n + 1)];
        for (int i = 2; i <= channels.length - 3; i++)
            channels[i] = new Channel<>(onMessage);

        Process[] processes = new Process[n];
        for (int i = 1, k = 0; i <= n; i++, k += 2) {

            // int data = new Random().nextInt(n * 100);
            int data = n - i + 1;

            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            processes[i - 1] = new Process<Integer>(data, i, n, _channels, comparator);
        }

        for (Process process : processes)
            process.start();
    }
}
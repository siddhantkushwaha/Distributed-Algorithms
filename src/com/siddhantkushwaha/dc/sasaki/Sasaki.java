package com.siddhantkushwaha.dc.sasaki;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

import java.util.Arrays;

public class Sasaki {

    public static void main(String[] args) {

        Comparator<Integer> comparator = (data1, data2) -> data1 < data2;

        Channel.OnMessage<Data<Integer>> onMessage = new Channel.OnMessage<Data<Integer>>() {
            @Override
            public void onSend(int processNumber, Data<Integer> data, int roundNumber) {

            }

            @Override
            public void onReceive(int processNumber, Data<Integer> data, int roundNumber) {

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

            int mark = 0;
            if (i == 1)
                mark = -1;
            else if (i == n)
                mark = 1;

            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            processes[i - 1] = new Process<Integer>(data, mark, i, n, _channels, comparator);
        }

        for (Process process : processes) {
            process.start();
        }
    }
}

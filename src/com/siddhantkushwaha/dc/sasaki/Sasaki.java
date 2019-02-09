package com.siddhantkushwaha.dc.sasaki;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

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

        int n = 8;
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // pass
        }

        Channel<Data<Integer>>[][] channels = new Channel[2][2];
        channels[1][0] = new Channel(onMessage);
        channels[1][1] = new Channel(onMessage);

        System<Integer>[] systems = new System[n];
        for (int i = 1; i <= n; i++) {

            // int data = new Random().nextInt(n * 100);
            int data = n - i + 1;

            int mark = 0;
            if (i == 1)
                mark = -1;
            else if (i == n)
                mark = 1;

            Channel<Data<Integer>>[][] _channels = new Channel[2][2];
            _channels[0][0] = channels[0][0];
            _channels[0][1] = channels[0][1];
            _channels[1][0] = channels[1][0];
            _channels[1][1] = channels[1][1];
            systems[i - 1] = new System(data, mark, i, n, _channels, comparator);

            if (i < n) {
                channels[0][0] = channels[1][0];
                channels[0][1] = channels[1][1];
                if (i < n - 1) {
                    channels[1][0] = new Channel(onMessage);
                    channels[1][1] = new Channel(onMessage);
                } else {
                    channels[1][0] = null;
                    channels[1][1] = null;
                }
            }
        }

        for (System system : systems)
            system.start();
    }
}

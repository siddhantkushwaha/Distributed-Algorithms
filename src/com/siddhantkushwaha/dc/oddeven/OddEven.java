package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

public class OddEven {

    public static void main(String[] args) {

        Comparator<Integer> comparator = (data1, data2) -> data1 < data2;

        Channel.OnMessage<Integer> onMessage = new Channel.OnMessage<Integer>() {
            @Override
            public void onSend(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("System P%d has sent %d in round %d\n", processNumber, data, roundNumber);
            }

            @Override
            public void onReceive(int processNumber, Integer data, int roundNumber) {
                // System.out.printf("System P%d has received %d in round %d\n", processNumber, data, roundNumber);
            }
        };

        int n = 5;
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // pass
        }

        Channel<Integer>[][] channels = new Channel[2][2];
        channels[1][0] = new Channel<>(onMessage);
        channels[1][1] = new Channel<>(onMessage);

        System<Integer>[] systems = new System[n];
        for (int i = 1; i <= n; i++) {

            // int data = new Random().nextInt(n * 100);
            int data = n - i + 1;

            Channel<Integer>[][] _channels = new Channel[2][2];
            _channels[0][0] = channels[0][0];
            _channels[0][1] = channels[0][1];
            _channels[1][0] = channels[1][0];
            _channels[1][1] = channels[1][1];
            systems[i - 1] = new System<>(data, i, n, _channels, comparator);

            if (i < n) {
                channels[0][0] = channels[1][0];
                channels[0][1] = channels[1][1];
                if (i < n - 1) {
                    channels[1][0] = new Channel<>(onMessage);
                    channels[1][1] = new Channel<>(onMessage);
                } else {
                    channels[1][0] = null;
                    channels[1][1] = null;
                }
            }
        }

        for (System<Integer> system : systems) {
            java.lang.System.out.printf("%d ", system.getData());
        }
        java.lang.System.out.println();

        for (System system : systems)
            system.start();
    }
}

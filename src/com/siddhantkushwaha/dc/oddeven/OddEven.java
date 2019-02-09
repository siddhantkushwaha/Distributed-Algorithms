package com.siddhantkushwaha.dc.oddeven;

import com.siddhantkushwaha.dc.Channel;
import com.siddhantkushwaha.dc.Comparator;

import java.util.Arrays;

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

        Channel[] channels = new Channel[4 * (n - 2)];
        for (int i = 2; i <= channels.length - 3; i++)
            channels[i] = new Channel<>(onMessage);

        System[] systems = new System[n];
        for (int i = 1, k = 0; i <= n; i++, k += 2) {

            // int data = new Random().nextInt(n * 100);
            int data = n - i + 1;

            Channel[] _channels = Arrays.copyOfRange(channels, k, k + 4);
            systems[i - 1] = new System<Integer>(data, i, n, _channels, comparator);
        }

        for (System system : systems) {
            java.lang.System.out.printf("%d ", (Integer) system.getData());
        }
        java.lang.System.out.println();

        for (System system : systems)
            system.start();
    }
}

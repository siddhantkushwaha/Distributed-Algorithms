package com.siddhantkushwaha.dc;

import com.siddhantkushwaha.dc.modulo3.Modulo3;
import com.siddhantkushwaha.dc.sasaki.Sasaki;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {

        System.out.println("Choose - \n");
        System.out.println("\t1 - Sasaki");
        System.out.println("\t2 - Modulo3");

        System.out.println("\n\tAny other key to quit.");

        String choice = scanner.next();

        switch (choice) {

            case "1":
            case "2":
                System.out.println("Number of elements ?");
                int n = scanner.nextInt();

                System.out.println("Sorting order ? \n\t D - Descending \n\t Any other key - Ascending");
                String order = scanner.next().toLowerCase();

                System.out.println("Print process result every round? \n\tY - Yes ? \n\tAny other key - No");
                String pr = scanner.next().toLowerCase();

                System.out.println("Print send and receive messages? \n\tY - Yes ? \n\tAny other key - No");
                String sr = scanner.next().toLowerCase();

                System.out.println("How to generate Elements? \n\t1 - Random Input ? \n\t2 - Worst Case Input ? \n\tAny other key - Enter manually");
                String c = scanner.next();

                int[] arr = new int[n];
                for (int i = 0; i < n; i++) {
                    switch (c) {
                        case "1":
                            arr[i] = random.nextInt(n * 100);
                            break;
                        case "2":
                            arr[i] = n - i;
                            break;
                        default:
                            arr[i] = scanner.nextInt();
                            break;
                    }
                }
                callMethod(choice, arr, order.equals("d"), pr.equals("y"), sr.equals("y"));
                break;
            default:
                System.out.println("Quitting...");
                break;
        }
    }

    private static void callMethod(String choice, int[] arr, boolean order, boolean printEachRoundResult, boolean printSendReceiveMessages) {

        switch (choice) {
            case "1":
                Sasaki.sasaki(arr, order, printEachRoundResult, printSendReceiveMessages);
                break;
            case "2":
                Modulo3.modulo3(arr, order, printEachRoundResult, printSendReceiveMessages);
                break;
            default:
                throw new RuntimeException("Invalid, no function for this choice");
        }
    }
}

package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(java.util.Locale.US);

        int size = readInt(scanner);
        if (size <= 0) {
            System.out.println("Input error. Size <= 0");
            return;
        }

        double[] numbers = new double[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = readDouble(scanner);
        }

        selectionSort(numbers);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(numbers[i]);
        }
        System.out.println(sb.toString());
    }

    private static void selectionSort(double[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // Swap
            double temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Could not parse a number. Please try again");
                scanner.next();
            }
        }
    }

    private static double readDouble(Scanner scanner) {
        while (true) {
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            } else {
                System.out.println("Could not parse a number. Please try again");
                scanner.next();
            }
        }
    }
}

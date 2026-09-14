package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int size = readInt(scanner);
        if (size <= 0) {
            System.out.println("Input error. Size <= 0");
            return;
        }

        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = readInt(scanner);
        }

        // Find arithmetic mean of negatives
        int sum = 0;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (numbers[i] < 0) {
                sum += numbers[i];
                count++;
            }
        }

        if (count == 0) {
            System.out.println("There are no negative elements");
        } else {
            System.out.println(sum / count);
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
}

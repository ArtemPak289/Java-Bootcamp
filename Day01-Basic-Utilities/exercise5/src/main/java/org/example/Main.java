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
        int i = 0;
        while (i < size) {
            numbers[i] = readInt(scanner);
            i++;
        }

        // Collect numbers with matching first and last digits
        int[] matched = new int[size];
        int matchCount = 0;
        int j = 0;
        while (j < size) {
            if (hasMatchingFirstAndLastDigit(numbers[j])) {
                matched[matchCount++] = numbers[j];
            }
            j++;
        }

        if (matchCount == 0) {
            System.out.println("There are no such elements");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < matchCount; k++) {
                if (k > 0) sb.append(" ");
                sb.append(matched[k]);
            }
            System.out.println(sb.toString());
        }
    }

    private static boolean hasMatchingFirstAndLastDigit(int n) {
        if (n < 0) n = -n;
        int lastDigit = n % 10;
        int firstDigit = n;
        while (firstDigit >= 10) {
            firstDigit /= 10;
        }
        return firstDigit == lastDigit;
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

package org.example;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int seconds = input();
        if (seconds < 0) {
            System.out.println("Incorrect time.");
            return;
        }
        int[] result = calculate(seconds);
        output(result);
    }

    private static int input() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Could not parse a number. Please try again");
                scanner.next();
            }
        }
    }

    private static int[] calculate(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return new int[]{hours, minutes, seconds};
    }

    private static void output(int[] hms) {
        System.out.printf("%02d:%02d:%02d%n", hms[0], hms[1], hms[2]);
    }
}

package org.example;

import java.util.Scanner;

public class Main {
    // Max safe n for long Fibonacci: fib(92) fits in long, fib(93) overflows
    private static final int MAX_N = 92;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = readInt(scanner);

        if (n < 0 || n > MAX_N) {
            System.out.println("Too large n");
            return;
        }

        long result = fibonacci(n);
        System.out.println(result);
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else if (scanner.hasNextLong()) {
                // Number too large for int — definitely too large for fib
                scanner.nextLong();
                System.out.println("Too large n");
                System.exit(0);
            } else {
                System.out.println("Could not parse a number. Please try again");
                scanner.next();
            }
        }
    }

    private static long fibonacci(int n) {
        if (n <= 1) return n;
        long a = 0;
        long b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }
}

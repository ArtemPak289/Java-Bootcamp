package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(java.util.Locale.US);

        double x1 = readDouble(scanner);
        double y1 = readDouble(scanner);
        double x2 = readDouble(scanner);
        double y2 = readDouble(scanner);
        double x3 = readDouble(scanner);
        double y3 = readDouble(scanner);

        double a = distance(x1, y1, x2, y2);
        double b = distance(x2, y2, x3, y3);
        double c = distance(x1, y1, x3, y3);

        if (isTriangle(a, b, c)) {
            double perimeter = a + b + c;
            System.out.printf("Perimeter: %.3f%n", perimeter);
        } else {
            System.out.println("It's not a triangle");
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

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static boolean isTriangle(double a, double b, double c) {
        // A triangle is valid if all sides are > 0 and triangle inequality holds
        return a > 0 && b > 0 && c > 0
                && a + b > c
                && a + c > b
                && b + c > a;
    }
}

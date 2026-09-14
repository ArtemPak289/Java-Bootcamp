package org.example;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in).useLocale(java.util.Locale.US);
        String filePath = consoleScanner.nextLine().trim();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Input error. File doesn't exist");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            if (!fileScanner.hasNextInt()) {
                System.out.println("Input error. Size <= 0");
                return;
            }

            int size = fileScanner.nextInt();
            if (size <= 0) {
                System.out.println("Input error. Size <= 0");
                return;
            }

            double[] numbers = new double[size];
            int count = 0;

            while (fileScanner.hasNext() && count < size) {
                if (fileScanner.hasNextDouble()) {
                    numbers[count++] = fileScanner.nextDouble();
                } else {
                    fileScanner.next(); // skip invalid token
                }
            }

            if (count < size) {
                System.out.println("Input error. Insufficient number of elements");
                return;
            }

            System.out.println(count);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; i++) {
                if (i > 0) sb.append(" ");
                sb.append(numbers[i]);
            }
            System.out.println(sb.toString());

            double min = numbers[0];
            double max = numbers[0];
            for (int i = 1; i < count; i++) {
                if (numbers[i] < min) min = numbers[i];
                if (numbers[i] > max) max = numbers[i];
            }

            System.out.println("Saving min and max values to file");

            try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
                writer.println(min + " " + max);
            }
        } catch (IOException e) {
            System.out.println("Input error. File doesn't exist");
        }
    }
}

package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int count = 0;
        int ordinal = 0;
        int prevValue = Integer.MIN_VALUE;
        int outOfOrderOrdinal = -1;

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                ordinal++;
                count++;

                if (outOfOrderOrdinal == -1 && ordinal > 1 && value <= prevValue) {
                    outOfOrderOrdinal = ordinal;
                }
                prevValue = value;
            } else {
                // Non-parseable token — stop reading
                break;
            }
        }

        if (count == 0) {
            System.out.println("Input error");
        } else if (outOfOrderOrdinal != -1) {
            System.out.println("The sequence is not ordered from the ordinal number of the number " + outOfOrderOrdinal);
        } else {
            System.out.println("The sequence is in ascending order");
        }
    }
}

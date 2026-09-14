package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> users = new ArrayList<>();

        int count = readInt(scanner);

        for (int i = 0; i < count; i++) {
            String name = scanner.next();
            int age = readAge(scanner);
            if (age <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }
            users.add(new User(name, age));
        }

        List<String> adultNames = users.stream()
                .filter(u -> u.getAge() >= 18)
                .map(User::getName)
                .collect(Collectors.toList());

        if (!adultNames.isEmpty()) {
            System.out.println(String.join(", ", adultNames));
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

    private static int readAge(Scanner scanner) {
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

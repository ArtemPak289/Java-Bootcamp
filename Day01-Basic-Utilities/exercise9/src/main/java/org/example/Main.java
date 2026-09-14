package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int count = Integer.parseInt(scanner.nextLine().trim());
        List<String> strings = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            strings.add(scanner.nextLine());
        }

        String substring = scanner.nextLine();

        List<String> filtered = filter(strings, substring);

        if (!filtered.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < filtered.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(filtered.get(i));
            }
            System.out.println(sb.toString());
        }
    }

    private static List<String> filter(List<String> strings, String substring) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if (s.contains(substring)) {
                result.add(s);
            }
        }
        return result;
    }
}

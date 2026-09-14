package school21.day02.exercise0;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer count = parseInteger(scanner);
        if (count == null) {
            return;
        }

        List<Animal> pets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String type = readNonEmptyLine(scanner);
            if (type == null) {
                break;
            }
            if (!type.equals("dog") && !type.equals("cat")) {
                System.out.println("Incorrect input. Unsupported pet type");
                continue;
            }
            String name = readNonEmptyLine(scanner);
            if (name == null) {
                break;
            }
            Integer age = parseInteger(scanner);
            if (age == null) {
                break;
            }
            if (age <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }
            if (type.equals("dog")) {
                pets.add(new Dog(name, age));
            } else {
                pets.add(new Cat(name, age));
            }
        }

        for (Animal pet : pets) {
            System.out.println(pet);
        }
    }

    private static Integer parseInteger(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Could not parse a number. Please, try again");
            }
        }
        return null;
    }

    private static String readNonEmptyLine(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return null;
    }
}

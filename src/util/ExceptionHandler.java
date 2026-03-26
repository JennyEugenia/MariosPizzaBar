package util;

import java.util.Scanner;

public class ExceptionHandler {
    public static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
                scanner.nextLine();
            }
        }
    }

    public static int getIntInRange(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();

                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Indtast et tal mellem " + min + " og " + max);
                }

            } catch (Exception e) {
                System.out.println("Ugyldigt input. Indtast venligst et tal.");
                scanner.nextLine();
            }
        }
    }
    //Validering af der bliver skrevet et navn (ikke tal eller tomt felt)
    public static String readName(Scanner scanner, String prompt) {
        String name = "";
        while (name.isEmpty()) {
            System.out.print(prompt);
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Navn må ikke være tomt – prøv igen.");
            } else if (name.matches(".*\\d.*")) {
                System.out.println("Navn må ikke indeholde tal – prøv igen.");
                name = "";
            }
        }
        return name;
    }
}
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


}
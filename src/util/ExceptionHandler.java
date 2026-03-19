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


}
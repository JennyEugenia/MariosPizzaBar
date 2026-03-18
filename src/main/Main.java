package main;

import ui.PizzaBarUI;

public class Main {

    public static void main(String[] args) {

        // Create the UI
        PizzaBarUI ui = new PizzaBarUI();

        // Start the main loop
        ui.start();

        // Program ends when user exits
        System.out.println("Program lukket.");
    }
}
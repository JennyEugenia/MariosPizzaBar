package main;

import ui.PizzaBarUI;

public class Main {

    public static void main(String[] args) {

        // Instantiates the UI object
        PizzaBarUI ui = new PizzaBarUI();

        // Start the main loop
        ui.start();

        // Program ends when user exits
        System.out.println("Program lukket.");
    }
}
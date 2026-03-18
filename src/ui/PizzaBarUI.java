package ui;

import model.*;
import service.OrderManager;

import java.time.LocalDateTime;
import java.util.Scanner;

public class PizzaBarUI {

    private Scanner scanner;
    private OrderManager orderManager;
    private Menu menu;

    public PizzaBarUI() {
        scanner = new Scanner(System.in);
        orderManager = new OrderManager();
        menu = new Menu(); // husk: Menu skal have constructor, som loader pizzaer
    }

    // Main loop
    public void start() {
        while (true) {
            System.out.println("\n--- PIZZABAR ---");
            System.out.println("1. Modtag ordre");
            System.out.println("2. Aktive ordrer");
            System.out.println("3. Færdige ordrer + statistik");
            System.out.println("0. Afslut");

            int choice = scanner.nextInt();
            scanner.nextLine(); // ryd buffer

            switch (choice) {
                case 1 -> receiveOrder();
                case 2 -> showActiveOrders();
                case 3 -> showPastOrders();
                case 0 -> {
                    System.out.println("Farvel!");
                    return;
                }
                default -> System.out.println("Ugyldigt valg!");
            }
        }
    }

    // 1) Modtag ordre
    private void receiveOrder() {
        System.out.println("\nIndtast navn:");
        String name = scanner.nextLine();

        // Spørg efter kundetype
        System.out.println("Vælg kundetype: 1 = Normal, 2 = VIP, 3 = Medarbejder");
        int type = scanner.nextInt();
        scanner.nextLine(); // ryd buffer

        // Opret kunden korrekt
        Customer customer = orderManager.createCustomer(type, name);

        // Opret ordre med afhentningstid +15 min
        Order order = orderManager.createOrder(customer, LocalDateTime.now().plusMinutes(15));

        // Vis menu
        menu.displayMenu();

        System.out.println("\nVælg pizza numre (fx 2,5,7) og tryk Enter:");
        String input = scanner.nextLine();
        String[] choices = input.split(",");

        // Tilføj pizzaer til ordren
        for (String choiceStr : choices) {
            try {
                int pizzaNumber = Integer.parseInt(choiceStr.trim());
                Pizza pizza = menu.findPizza(pizzaNumber);
                if (pizza != null) {
                    orderManager.addPizzaToOrder(order, pizza);
                } else {
                    System.out.println("Pizza nummer " + pizzaNumber + " findes ikke.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ugyldigt nummer: " + choiceStr);
            }
        }

        System.out.println("\nOrdre oprettet:");
        System.out.println(order);
    }

    // 2) Vis aktive ordrer
    private void showActiveOrders() {
        System.out.println("\n--- AKTIVE ORDRER ---");

        boolean hasActive = false;
        for (Order order : orderManager.getOrders()) {
            if (order.getStatus() != OrderStatus.AFHENTET) {
                System.out.println(order.getOrderId() + ": " + order);
                hasActive = true;
            }
        }
        if (!hasActive) System.out.println("Ingen aktive ordrer.");

        System.out.println("\nIndtast ordre ID for afhentning (0 = tilbage):");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == 0) return;

        if (orderManager.updateOrderStatus(id, OrderStatus.AFHENTET)) {
            System.out.println("Ordre markeret som afhentet!");
        } else {
            System.out.println("Ordre ikke fundet.");
        }
    }

    // 3) Vis færdige ordrer + statistik
    private void showPastOrders() {
        System.out.println("\n--- TIDLIGERE ORDRER ---");

        boolean hasPast = false;
        for (Order order : orderManager.getOrders()) {
            if (order.getStatus() == OrderStatus.AFHENTET) {
                System.out.println(order);
                hasPast = true;
            }
        }
        if (!hasPast) System.out.println("Ingen færdige ordrer.");

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Antal færdige ordrer: " +
                orderManager.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.AFHENTET).count());
        System.out.println("Total omsætning: " + orderManager.getTotalRevenue() + " kr");
    }
}
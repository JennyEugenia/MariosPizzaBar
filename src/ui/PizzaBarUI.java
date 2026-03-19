package ui;

import file.FileHandler;
import model.*;
import service.OrderManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class PizzaBarUI {

    private Scanner scanner;
    private OrderManager orderManager;
    private Menu menu;

    public PizzaBarUI() {
        scanner = new Scanner(System.in);
        orderManager = new OrderManager();
        menu = new Menu();

        // Indlæs tidligere ordrer fra fil ved opstart
        ArrayList<Order> loadedOrders = FileHandler.readOrders(menu);
        orderManager.loadOrders(loadedOrders);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- PIZZABAR ---");
            System.out.println("1. Modtag ordre");
            System.out.println("2. Aktive ordrer");
            System.out.println("3. Færdige ordrer + statistik");
            System.out.println("0. Afslut");

            int choice = scanner.nextInt();
            scanner.nextLine();

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

    private void receiveOrder() {
        System.out.println("\nIndtast navn:");
        String name = scanner.nextLine();

        System.out.println("Vælg kundetype: 1 = Normal, 2 = VIP, 3 = Medarbejder");
        int type = scanner.nextInt();
        scanner.nextLine();

        Customer customer = orderManager.createCustomer(type, name);
        Order order = orderManager.createOrder(customer, LocalDateTime.now().plusMinutes(15));

        menu.displayMenu();

        System.out.println("\nVælg pizza numre (fx 2,5,7) og tryk Enter:");
        String input = scanner.nextLine();
        String[] choices = input.split(",");

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

        // Gem alle ordrer til fil
        FileHandler.writeOrders(orderManager.getOrders());
    }

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
            // Gem efter statusændring
            FileHandler.writeOrders(orderManager.getOrders());
        } else {
            System.out.println("Ordre ikke fundet.");
        }
    }

    private void showPastOrders() {
        System.out.println("\n--- TIDLIGERE ORDRER ---");

        boolean hasPast = false;
        for (Order order : orderManager.getOrders()) {
            if (order.getStatus() == OrderStatus.AFHENTET) {
                System.out.println("Ordre #" + order.getOrderId() +
                        " | " + order.getCustomer().getName() +
                        " | Afhentet: " + order.getFormattedPickupTimeWithDate() +
                        " | " + order.getTotalPrice() + " kr.");
                hasPast = true;
            }
        }
        if (!hasPast) System.out.println("Ingen færdige ordrer.");

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Antal færdige ordrer: " +
                orderManager.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.AFHENTET).count());
        System.out.println("Total omsætning: " + orderManager.getTotalRevenue() + " kr");
    }
}2
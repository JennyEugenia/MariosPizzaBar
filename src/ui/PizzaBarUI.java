package ui;

import file.FileHandler;
import model.*;
import service.OrderManager;
import util.ExceptionHandler;
import util.OrderSorter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// PizzaBarUI = user interface class (blueprint)
public class PizzaBarUI {

    private Scanner scanner;
    private OrderManager orderManager;
    private Menu menu;

    // Constructor to create an actual PizzaBarUI object
    public PizzaBarUI() {
        scanner = new Scanner(System.in);
        orderManager = new OrderManager();
        menu = new Menu();

        ArrayList<Order> loadedOrders = FileHandler.readOrders(menu);
        orderManager.loadOrders(loadedOrders);
    }

    // -------------------------------------------------------
    // MAIN LOOP — the program lives here until user exits
    // -------------------------------------------------------

    public void start() {
        while (true) {
            System.out.println("\n--- PIZZABAR ---");
            System.out.println("1. Modtag ordre");
            System.out.println("2. Aktive ordrer");
            System.out.println("3. Færdige ordrer + statistik");
            System.out.println("0. Afslut");

            int choice = ExceptionHandler.getIntInRange(scanner, 0, 3);
            scanner.nextLine();

            switch (choice) {
                case 1 -> receiveOrder();
                case 2 -> showActiveOrders();
                case 3 -> showPastOrders();
                case 0 -> {
                    System.out.println("Farvel!");
                    return;
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------
    // RECEIVE ORDER "Coordinator" following Single Responsibility principle (If user typed "1")
    // -----------------------------------------------------------------------------------------

    // Coordinates the full process of creating an order from user input

    // receiveOrder()
    //    ├── selectPizzas()
    //    │       ├── choosePizzas()
    //    │       │       └── parsePizzaChoice()
    //    │       └── printSelectedPizzas()
    //    ├── readComment()
    //    └── readPickupTime()

    private void receiveOrder() {
        String name = ExceptionHandler.readName(scanner, "\nIndtast navn: ");

        System.out.println("Vælg kundetype: 1 = Normal, 2 = VIP, 3 = Medarbejder");
        int type = ExceptionHandler.getIntInRange(scanner, 1, 3);
        scanner.nextLine();

        Customer customer = orderManager.createCustomer(type, name);

        menu.displayMenu();

        ArrayList<Pizza> selectedPizzas = selectPizzas();
        String comment = readComment();
        LocalDateTime pickupTime = readPickupTime();

        Order order = orderManager.createOrder(customer, pickupTime);
        if (!comment.isEmpty()) {
            order.setComment(comment);
            System.out.println("Kommentar tilføjet.");
        }

        for (Pizza pizza : selectedPizzas) {
            orderManager.addPizzaToOrder(order, pizza);
        }

        System.out.println("\nOrdre oprettet:");
        System.out.println(order);

        FileHandler.writeOrders(orderManager.getOrders());
    }

    // -------------------------------------------
    // selectPizzas (HELPER used in RECEIVE ORDER)
    // -------------------------------------------

    //Lets the user choose pizzas and repeats until the selection is confirmed
    private ArrayList<Pizza> selectPizzas() {
        ArrayList<Pizza> selectedPizzas = new ArrayList<>();
        boolean confirmed = false;

        while (!confirmed) {

            selectedPizzas = choosePizzas();

            printSelectedPizzas(selectedPizzas);

            System.out.println("\nEr denne bestilling korrekt? (1 = Ja, 2 = Nej – start forfra)");
            int confirm = ExceptionHandler.getIntInRange(scanner, 1, 2);
            scanner.nextLine();

            if (confirm == 1) {
                confirmed = true;
            } else {
                System.out.println("Okay, lad os starte forfra :D.");
            }
        }

        return selectedPizzas;
    }

    // -------------------------------------------
    // choosePizzas (HELPER used in RECEIVE ORDER)
    // -------------------------------------------

    //Handles user input of pizza numbers and manages invalid selections
    private ArrayList<Pizza> choosePizzas() {
        ArrayList<Pizza> selected = new ArrayList<>();
        ArrayList<Integer> notFound = new ArrayList<>();

        System.out.println("\nVælg pizza numre (fx 2,5,7) og tryk Enter:");
        String input = scanner.nextLine().trim();

        for (String choiceStr : input.split(",")) {
            parsePizzaChoice(choiceStr, selected, notFound);
        }

        while (!notFound.isEmpty()) {
            System.out.println("\nOBS! Disse pizzanumre findes ikke: " + notFound);
            System.out.println("Vælg " + notFound.size() + " erstatning(er):");
            notFound.clear();

            String replacement = scanner.nextLine().trim();
            for (String choiceStr : replacement.split(",")) {
                parsePizzaChoice(choiceStr, selected, notFound);
            }
        }

        return selected;
    }

    // -------------------------------------------------------
    // parsePizzaChoice (HELPER used in RECEIVE ORDER)
    // -------------------------------------------------------

    // Validates a single pizza input and adds it to selected or notFound lists
    private void parsePizzaChoice(String choiceStr,
                                  ArrayList<Pizza> selected,
                                  ArrayList<Integer> notFound) {
        try {
            int number = Integer.parseInt(choiceStr.trim());
            Pizza pizza = menu.findPizza(number);
            if (pizza != null) {
                selected.add(pizza);
                System.out.println(pizza.getName() + " tilføjet ✓");
            } else {
                notFound.add(number);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ugyldigt nummer: '" + choiceStr.trim() + "' – ignoreret.");
        }
    }

    // -------------------------------------------------------
    // printSelectedPizzas (HELPER used in RECEIVE ORDER)
    // -------------------------------------------------------

    // Displays all selected pizzas with their names and prices
    private void printSelectedPizzas(ArrayList<Pizza> pizzas) {
        System.out.println("\nDu har valgt følgende " + pizzas.size() + " pizzaer:");
        for (Pizza pizza : pizzas) {
            System.out.println("  - " + pizza.getName() +
                    " (" + pizza.getPrice() + " kr.)");
        }
    }

    // -------------------------------------------------------
    // readComment (HELPER used in RECEIVE ORDER)
    // -------------------------------------------------------

    // Reads an optional comment or special request from the user
    private String readComment() {
        System.out.println("\nEr der særlige ønsker/allergener? (fx 'ingen løg' ellers tryk Enter for ingen kommentar):");
        return scanner.nextLine().trim();
    }

    // -------------------------------------------------------
    // readPickupTime (HELPER used in RECEIVE ORDER)
    // -------------------------------------------------------

    // Validates and converts user input into a pickup time (LocalDateTime)
    private LocalDateTime readPickupTime() {
        while (true) {
            System.out.println("\nIndtast afhentingstidspunkt (HH:mm):");
            String input = scanner.nextLine().trim();
            try {
                LocalTime parsedTime = LocalTime.parse(input,
                        DateTimeFormatter.ofPattern("HH:mm"));
                return LocalDate.now().atTime(parsedTime); // combine today's date with entered time
            } catch (Exception e) {
                System.out.println("Ugyldigt format – prøv igen (fx 18:30).");
            }
        }
    }

    // -------------------------------------------------------
    // SHOW ACTIVE ORDERS (If user typed "2")
    // -------------------------------------------------------

    // Displays active orders, allows marking them as picked up, and saves changes
    private void showActiveOrders() {
        System.out.println("\n--- AKTIVE ORDRER ---");

        ArrayList<Order> activeOrders = new ArrayList<>();
        for (Order order : orderManager.getOrders()) {
            if (order.getStatus() != OrderStatus.AFHENTET) {
                activeOrders.add(order);
            }
        }

        if (activeOrders.isEmpty()) {
            System.out.println("Ingen aktive ordrer.");
        } else {
            OrderSorter.sortOrders(activeOrders);
            for (Order order : activeOrders) {
                System.out.println(order.getOrderId() + ": " + order);
            }
        }

        System.out.println("\nIndtast ordre ID for afhentning (0 = tilbage):");
        int id = ExceptionHandler.getIntInput(scanner);
        scanner.nextLine();

        if (id == 0) return;

        if (orderManager.updateOrderStatus(id, OrderStatus.AFHENTET)) {
            System.out.println("Ordre markeret som afhentet!");
            FileHandler.writeOrders(orderManager.getOrders());
        } else {
            System.out.println("Ordre ikke fundet.");
        }
    }

    // -------------------------------------------------------
    // SHOW PAST ORDERS + STATISTICS (If user typed "3")
    // -------------------------------------------------------

    // Shows completed orders and prints statistics like revenue and popular pizzas
    private void showPastOrders() {
        System.out.println("\n--- TIDLIGERE ORDRER ---");

        boolean hasPast = false;
        for (Order order : orderManager.getOrders()) {
            if (order.getStatus() == OrderStatus.AFHENTET) {
                System.out.println("Ordre #" + order.getOrderId() +
                        " | " + order.getCustomer().getName() +
                        " | Afhentet: " + order.getFormattedPickupTimeWithDate() +
                        " | " + String.format("%.2f", order.getTotalPrice()) + " kr.");
                hasPast = true;
            }
        }
        if (!hasPast) System.out.println("Ingen færdige ordrer.");

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Antal færdige ordrer: " +
                orderManager.getOrders().stream()
                        .filter(o -> o.getStatus() == OrderStatus.AFHENTET)
                        .count());
        System.out.println("Total omsætning: " +
                String.format("%.2f", orderManager.getTotalRevenueByStatus(OrderStatus.AFHENTET)) + " kr");

        System.out.println("\n--- MEST SOLGTE PIZZAER ---");
        orderManager.printPizzaRanking(menu, OrderStatus.AFHENTET);
        for (Pizza pizza : menu.getPizzas()) {
            int count = orderManager.countPizzaByName(pizza.getName(), OrderStatus.AFHENTET);
            if (count > 0) {
                System.out.println(pizza.getName() + " (#" + pizza.getPizzaNumber() + ") : " + count + " stk.");
            }
        }
    }
}
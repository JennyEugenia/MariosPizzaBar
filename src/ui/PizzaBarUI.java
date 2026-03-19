package ui;

import file.FileHandler;
import model.*;
import service.OrderManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import util.OrderSorter;

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

        menu.displayMenu();

        System.out.println("\nVælg pizza numre (fx 2,5,7) og tryk Enter:");
        String input = scanner.nextLine();
        String[] choices = input.split(",");

        // Midlertidig liste til de valgte pizzaer
        ArrayList<Pizza> selectedPizzas = new ArrayList<>();

        for (String choiceStr : choices) {
            try {
                int pizzaNumber = Integer.parseInt(choiceStr.trim());
                Pizza pizza = menu.findPizza(pizzaNumber);
                if (pizza != null) {
                    selectedPizzas.add(pizza);
                } else {
                    System.out.println("Pizza nummer " + pizzaNumber + " findes ikke.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ugyldigt nummer: " + choiceStr);
            }
        }

        // Indtast afhentingstidspunkt
        LocalDateTime pickupTime = null;
        while (pickupTime == null) {
            System.out.println("\nIndtast afhentingstidspunkt (HH:mm):");
            String timeInput = scanner.nextLine();
            try {
                LocalTime parsedTime = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
                pickupTime = LocalDate.now().atTime(parsedTime);
            } catch (Exception e) {
                System.out.println("Ugyldigt format – prøv igen (fx 18:30).");
            }
        }

        // Opret ordre med det korrekte afhentingstidspunkt
        Order order = orderManager.createOrder(customer, pickupTime);

        // Tilføj de valgte pizzaer til ordren
        for (Pizza pizza : selectedPizzas) {
            orderManager.addPizzaToOrder(order, pizza);
        }

        System.out.println("\nOrdre oprettet:");
        System.out.println(order);

        // Gem alle ordrer til fil
        FileHandler.writeOrders(orderManager.getOrders());
    }

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
        System.out.println("Total omsætning: " + orderManager.getTotalRevenueByStatus(OrderStatus.AFHENTET) + " kr");

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
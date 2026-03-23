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
import util.ExceptionHandler;

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
        String name = ExceptionHandler.readName(scanner, "\nIndtast navn: ");

        System.out.println("Vælg kundetype: 1 = Normal, 2 = VIP, 3 = Medarbejder");
        int type = ExceptionHandler.getIntInRange(scanner, 1, 3);
        scanner.nextLine();

        Customer customer = orderManager.createCustomer(type, name);

        menu.displayMenu();

        ArrayList<Pizza> selectedPizzas = new ArrayList<>();
        boolean confirmed = false;

        while (!confirmed) {
            selectedPizzas.clear();

            System.out.println("\nVælg pizza numre (fx 2,5,7) og tryk Enter:");
            String input = scanner.nextLine().trim();
            String[] choices = input.split(",");

            // Hvor mange pizzaer ønskede Alfonso i alt?
            int ønsketAntal = choices.length;
            ArrayList<Integer> ikkeFundet = new ArrayList<>();

            for (String choiceStr : choices) {
                try {
                    int pizzaNumber = Integer.parseInt(choiceStr.trim());
                    Pizza pizza = menu.findPizza(pizzaNumber);
                    if (pizza != null) {
                        selectedPizzas.add(pizza);
                        System.out.println(pizza.getName() + " tilføjet ");
                    } else {
                        ikkeFundet.add(pizzaNumber);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ugyldigt nummer: " + choiceStr);
                    ønsketAntal--; // tæller ikke tomme/ugyldige input med
                }
            }

            // Hvis nogle pizzaer ikke blev fundet – bed om erstatninger
            while (!ikkeFundet.isEmpty()) {
                System.out.println("\nOBS! " + ikkeFundet.size() + " af dine ønskede pizzaer findes ikke:");
                for (int nr : ikkeFundet) {
                    System.out.println("  - Pizza nr. " + nr + " findes ikke i menuen.");
                }
                System.out.println("Du ønskede " + ønsketAntal + " pizzaer i alt – vælg "
                        + ikkeFundet.size() + " erstatning(er):");

                ikkeFundet.clear(); // nulstil listen

                String erstatning = scanner.nextLine().trim();
                String[] erstatninger = erstatning.split(",");

                for (String choiceStr : erstatninger) {
                    try {
                        int pizzaNumber = Integer.parseInt(choiceStr.trim());
                        Pizza pizza = menu.findPizza(pizzaNumber);
                        if (pizza != null) {
                            selectedPizzas.add(pizza);
                            System.out.println(pizza.getName() + " tilføjet ");
                        } else {
                            ikkeFundet.add(pizzaNumber); // stadig ikke fundet – prøv igen
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ugyldigt nummer: " + choiceStr);
                    }
                }
            }

            // Bekræftelse
            System.out.println("\nDu har valgt følgende " + selectedPizzas.size() + " pizzaer:");
            for (Pizza pizza : selectedPizzas) {
                System.out.println("  - " + pizza.getName() + " (" + pizza.getPrice() + " kr.)");
            }

            System.out.println("\nEr denne bestilling korrekt? (1 = Ja, 2 = Nej – start forfra)");
            int confirm = ExceptionHandler.getIntInRange(scanner, 1, 2);
            scanner.nextLine();

            if (confirm == 1) {
                confirmed = true;
            } else {
                System.out.println("Okay, lad os starte forfra :D.");
            }
        }

        System.out.println("\nEr der særlige ønsker til ordren?");
        System.out.println("(fx 'ingen løg', 'ekstra ost' – tryk Enter for ingen kommentar)");
        String comment = scanner.nextLine().trim();

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

        if (!comment.isEmpty()){
        order.setComment(comment);
        System.out.println("Kommentar tilføjet");
    }


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
                System.out.println(order.getOrderId() + ": " + order + " " + order.getComment());
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
                        " | " + String.format("%.2f", order.getTotalPrice()) + " kr.");
                hasPast = true;
            }
        }
        if (!hasPast) System.out.println("Ingen færdige ordrer.");

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Antal færdige ordrer: " +
                orderManager.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.AFHENTET).count());
        System.out.println("Total omsætning: " + String.format("%.2f", orderManager.getTotalRevenueByStatus(OrderStatus.AFHENTET)) + " kr");

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
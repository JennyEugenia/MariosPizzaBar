package file;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILENAME = "orders.csv";

    public static void writeOrders(ArrayList<Order> orders) {
        try {
            // false = overskriv hele filen så vi undgår dubletter
            PrintWriter writer = new PrintWriter(new FileWriter(FILENAME, false));

            for (Order order : orders) {

                StringBuilder pizzaNrs = new StringBuilder();
                for (Pizza pizza : order.getPizzas()) {
                    pizzaNrs.append(pizza.getPizzaNumber()).append(";");
                }

                String customerType = switch (order.getCustomer()) {
                    case VIPCustomer v      -> "VIP";
                    case EmployeeCustomer e -> "EMPLOYEE";
                    default                 -> "NORMAL";
                };

                // Format: orderId,customerType,navn,pizzaNrs,pickupTime,status
                writer.println(
                        order.getOrderId()            + "," +
                                customerType                  + "," +
                                order.getCustomer().getName() + "," +
                                pizzaNrs                      + "," +
                                order.getPickupTime()         + "," +
                                order.getStatus()
                );
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + FILENAME);
        }
    }

    public static ArrayList<Order> readOrders(Menu menu) {
        ArrayList<Order> orders = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int orderId              = Integer.parseInt(parts[0]);
                String customerType      = parts[1];
                String customerName      = parts[2];
                String pizzaNrs          = parts[3];
                LocalDateTime pickupTime = LocalDateTime.parse(parts[4]);
                OrderStatus status       = OrderStatus.valueOf(parts[5]);

                Customer customer = switch (customerType) {
                    case "VIP"      -> new VIPCustomer(customerName);
                    case "EMPLOYEE" -> new EmployeeCustomer(customerName);
                    default         -> new NormalCustomer(customerName);
                };

                Order order = new Order(orderId, customer, pickupTime);
                order.setStatus(status);

                for (String nr : pizzaNrs.split(";")) {
                    if (!nr.isEmpty()) {
                        Pizza pizza = menu.findPizza(Integer.parseInt(nr));
                        if (pizza != null) order.addPizza(pizza);
                    }
                }

                orders.add(order);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Ingen tidligere ordrer fundet.");
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af fil: " + FILENAME);
        }

        return orders;
    }
}
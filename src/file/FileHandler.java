package file;

import model.Order;
import model.Pizza;


import java.io.*; // nemmere og/eller mere overskueligt at håndtere.
import java.util.ArrayList;

public class FileHandler {

    private static final String FILENAME = "orders.csv";

    public static void writeOrders(ArrayList<Order> orders) {
        try {
            // true = append. Betyder vi tilføjer til filen i stedet for at overskrive den nuværende fil.
            PrintWriter writer = new PrintWriter(new FileWriter(FILENAME, true));

            for (Order order : orders) {

                // Byg en semikolon-separeret liste af pizzanumre
                // Vi gemmer nummeret, ikke navnet – så vi kan slå dem op i Menu igen
                StringBuilder pizzaNrs = new StringBuilder();
                for (Pizza pizza : order.getPizzas()) {
                    pizzaNrs.append(pizza.getPizzaNumber()).append(";");
                }

                // Vi skriver én linje pr. ordre i CSV-format:
                // orderId , kundenavn , pizzanummer , afhentingstid , totalpris
                writer.println(
                        order.getOrderId()              + "," +
                                order.getCustomer().getName()   + "," +
                                pizzaNrs                        + "," +
                                order.getPickupTime()            + "," +
                                order.getTotalPrice()
                );
            }
            // VIGTIGT vi altid lukker filen!
            writer.close();

        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + FILENAME);
        }
    }

    public static void readOrders() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line;

            System.out.println("=== Tidligere ekspederede ordrer ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int orderId         = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                String pizzaNrs     = parts[2];
                String pickupTime   = parts[3];
                double totalPrice   = Double.parseDouble(parts[4]);

                System.out.println("Ordre #" + orderId +
                        " | " + customerName +
                        " | Pizzaer: " + pizzaNrs +
                        " | Afhentning: " + pickupTime +
                        " | " + totalPrice + " kr.");
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Ingen tidligere ordrer fundet.");
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af fil: " + FILENAME);
        }
    }
}

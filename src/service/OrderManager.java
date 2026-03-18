package service;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderManager {

    private ArrayList<Order> orders; // liste over alle ordrer
    private int nextOrderId;          // unik ID til næste ordre

    public OrderManager() {
        orders = new ArrayList<>();
        nextOrderId = 1;
    }

    // Opretter kunde baseret på type: 1=Normal, 2=VIP, 3=Employee
    public Customer createCustomer(int type, String name) {
        switch (type) {
            case 2: return new VIPCustomer(name);
            case 3: return new EmployeeCustomer(name);
            default: return new NormalCustomer(name);
        }
    }

    // Opretter ordre med kunde og afhentningstid
    public Order createOrder(Customer customer, LocalDateTime pickupTime) {
        Order order = new Order(nextOrderId++, customer, pickupTime);
        orders.add(order);
        return order;
    }

    // Tilføjer pizza til ordre
    public void addPizzaToOrder(Order order, Pizza pizza) {
        if (order != null && pizza != null) {
            order.addPizza(pizza);
        }
    }

    // Returnerer alle ordrer (bruges til filtrering i UI)
    public ArrayList<Order> getOrders() {
        return orders;
    }

    // Finder ordre efter ID
    public Order findOrderById(int orderId) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) return o;
        }
        return null;
    }

    // Opdater status på ordre
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }

    // Beregn total omsætning
    public double getTotalRevenue() {
        double total = 0;
        for (Order o : orders) total += o.getTotalPrice();
        return total;
    }

    // Returnerer antal ordrer
    public int getOrderCount() {
        return orders.size();
    }
}
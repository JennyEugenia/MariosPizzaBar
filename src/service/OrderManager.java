package service;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderManager {

    private ArrayList<Order> orders;
    private int nextOrderId;

    public OrderManager() {
        orders = new ArrayList<>();
        nextOrderId = 1;
    }

    public Customer createCustomer(int type, String name) {
        switch (type) {
            case 2: return new VIPCustomer(name);
            case 3: return new EmployeeCustomer(name);
            default: return new NormalCustomer(name);
        }
    }

    public Order createOrder(Customer customer, LocalDateTime pickupTime) {
        Order order = new Order(nextOrderId++, customer, pickupTime);
        orders.add(order);
        return order;
    }

    public void addPizzaToOrder(Order order, Pizza pizza) {
        if (order != null && pizza != null) {
            order.addPizza(pizza);
        }
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Order findOrderById(int orderId) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) return o;
        }
        return null;
    }

    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }

    public double getTotalRevenueByStatus(OrderStatus status) {
        double total = 0;
        for (Order o : orders) {
            if(o.getStatus() == status) {
                total += o.getTotalPrice();
            }
        }
        return total;
    }

    public int countPizzaByName(String pizzaName, OrderStatus status) {
        int count = 0;

        for (Order o : orders) {
            if (o.getStatus() == status) {

                for (Pizza pizza : o.getPizzas()) {
                    if (pizza.getName().equalsIgnoreCase(pizzaName)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    // Sort pizzas by popularity
    public void printPizzaRanking(Menu menu, OrderStatus status) {

        ArrayList<Pizza> pizzas = menu.getPizzas();

        for (int i = 0; i < pizzas.size(); i++) {
            for (int j = i + 1; j < pizzas.size(); j++) {

                int count1 = countPizzaByName(pizzas.get(i).getName(), status);
                int count2 = countPizzaByName(pizzas.get(j).getName(), status);

                if (count2 > count1) {
                    Pizza temp = pizzas.get(i);
                    pizzas.set(i, pizzas.get(j));
                    pizzas.set(j, temp);
                }
            }
        }
    }


    public int getOrderCount() {
        return orders.size();
    }

    // Indlæser ordrer fra fil og sætter nextOrderId korrekt
    public void loadOrders(ArrayList<Order> loadedOrders) {
        orders.addAll(loadedOrders);
        for (Order o : loadedOrders) {
            if (o.getOrderId() >= nextOrderId) {
                nextOrderId = o.getOrderId() + 1;
            }
        }
    }
}
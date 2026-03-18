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

        // Creates a Customer based on selected type
        public Customer createCustomer(int type, String name) {
            switch (type) {
                case 2:
                    return new VIPCustomer(name);
                case 3:
                    return new EmployeeCustomer(name);
                default:
                    return new NormalCustomer(name);
            }
        }

        // Creates a new Order and assigns a unique ID
        public Order createOrder(Customer customer, LocalDateTime pickupTime) {
            Order order = new Order(nextOrderId++, customer, pickupTime);
            orders.add(order);
            return order;
        }

        // Prints all orders
        public void printAllOrders() {
            if (orders.isEmpty()) {
                System.out.println("No orders yet.");
                return;
            }

            for (Order o : orders) {
                System.out.println(o);
            }
        }

        // Finds an Order by its ID
        public Order findOrderById(int orderId) {
            for (Order o : orders) {
                if (o.getOrderId() == orderId) {
                    return o;
                }
            }
            return null;
        }

        // Updates the status of an Order
        public boolean updateOrderStatus(int orderId, OrderStatus status) {
            Order order = findOrderById(orderId);

            if (order != null) {
                order.setStatus(status);
                return true;
            }
            return false;
        }

        // Calculates total revenue from all orders
        public double getTotalRevenue() {
            double total = 0;

            for (Order o : orders) {
                total += o.getTotalPrice();
            }

            return total;
        }

        // Returns total number of orders
        public int getOrderCount() {
            return orders.size();
        }
    }



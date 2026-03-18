package util;

import model.Order;
import java.util.ArrayList;
import java.util.Collections;

public class OrderSorter {

    public static void sortOrders(ArrayList<Order> orders) {

        // opretter comparator
        OrderComparator comparator = new OrderComparator();

        // sorter den nuværende liste direkte. så den tidligste afhentning altid er øverst
        Collections.sort(orders, comparator);
    }
}
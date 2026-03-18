package util;

import model.Order;
import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {

        // sammenligner afhentingstidspunkt - efter tidligste
        return o1.getPickupTime().compareTo(o2.getPickupTime());
    }
}
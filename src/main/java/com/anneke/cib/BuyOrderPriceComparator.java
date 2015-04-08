package com.anneke.cib;

import java.util.Comparator;

/**
 *
 * @author anneke
 */
public class BuyOrderPriceComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getPrice() != o2.getPrice()) {
            return o1.getPrice() > o2.getPrice() ? 1 : -1;
        } else {
            return Integer.compare(o1.getOrderID(), o2.getOrderID());
        }
    }
}

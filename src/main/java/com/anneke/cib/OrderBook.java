package com.anneke.cib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author anneke
 */
public class OrderBook {

    private static Map<String, OrderBook> orderBooks = new HashMap<>();
    private String bookID;
    private TreeSet<Order> buyOrders = new TreeSet(new SellOrderPriceComparator());
    private TreeSet<Order> sellOrders = new TreeSet(new BuyOrderPriceComparator());
    // required for effectivly Orders deletion
    private Map<Integer, Order> orderMap = new HashMap<>();

    private OrderBook(String bookID) {
        this.bookID = bookID;
    }

    public static Map<String, OrderBook> getOrderBooks() {
        return orderBooks;
    }

    /**
     * Used Multitone pattern with Lazy Initialization to ensure only limited number of instances to be created. Notice:
     * Not thread safe operation.
     *
     * @param bookID acceptable order books IDs
     * @return OrderBook instance with specified ID
     */
    public static OrderBook getOrderBookById(String bookID) {
        OrderBook book;
        if (!orderBooks.containsKey(bookID)) {
            book = new OrderBook(bookID);
            orderBooks.put(bookID, book);
            
        } else {
            return orderBooks.get(bookID);
        }
        return book;
    }

   private void addSellOrder(Order order) {
       sellOrders.add(order);
       orderMap.put(order.getOrderID(), order);
   }
   
   private void addBuyOrder(Order order) {
       buyOrders.add(order);
       orderMap.put(order.getOrderID(), order);
   }
    private void matchSellOrder(Order order) {
        double sellPrice = order.getPrice();
        int orderVolume = order.getVolume();
        while (!buyOrders.isEmpty() && buyOrders.first().getPrice() >= sellPrice && orderVolume > 0) {
            Order firstBuyOrder = buyOrders.first();
            int buyOrderVolume = firstBuyOrder.getVolume();
            if (buyOrderVolume <= orderVolume) {
                orderVolume -= buyOrderVolume;
                deleteOrder(firstBuyOrder);
            } else {
                buyOrderVolume -= orderVolume;
                //Order newBuyOrder = buyOrders.pollFirst();
                //deleteOrder(newBuyOrder);
                buyOrders.remove(firstBuyOrder);
                firstBuyOrder.setVolume(buyOrderVolume);
                buyOrders.add(firstBuyOrder);
                //addBuyOrder(newBuyOrder);
                orderVolume = 0;
            }
        }
        if (orderVolume > 0) {
            order.setVolume(orderVolume);
            addSellOrder(order);
        }
    }

    private void matchBuyOrder(Order order) {
        double buyPrice = order.getPrice();
        int orderVolume = order.getVolume();
        while (!sellOrders.isEmpty() && sellOrders.first().getPrice() <= buyPrice && orderVolume > 0) {
            Order firstSellOrder = sellOrders.first(); 
            int sellOrderVolume = firstSellOrder.getVolume();
            if (sellOrderVolume <= orderVolume) {
                orderVolume -= sellOrderVolume;
                deleteOrder(firstSellOrder);
            } else {
                sellOrderVolume -= orderVolume;                
                //Order newSellOrder = sellOrders.pollFirst();
                //deleteOrder(firstSellOrder);
                sellOrders.remove(firstSellOrder);
                firstSellOrder.setVolume(sellOrderVolume);
                sellOrders.add(firstSellOrder);
                //addSellOrder(newSellOrder);
                orderVolume = 0;
            }
        }
        if (orderVolume > 0) {
            order.setVolume(orderVolume);
            addBuyOrder(order);
        }
    }

    public synchronized void addOrder(Order order) {
        Order.OperationType operationType = order.getOperationType();
        if (Order.OperationType.SELL.equals(operationType)) {
            matchSellOrder(order);
        } else if (Order.OperationType.BUY.equals(operationType)) {
            matchBuyOrder(order);
        }
    }

    public synchronized void deleteOrder(Order order) {
        Order deleted = this.orderMap.remove(order.getOrderID());
        if (deleted != null) {
            Order.OperationType operationType = deleted.getOperationType();
            if (Order.OperationType.SELL.equals(operationType)) {
                sellOrders.remove(deleted);
            } else if (Order.OperationType.BUY.equals(operationType)) {
                buyOrders.remove(deleted);
            }
        }
    }

    public synchronized String printOrderBook() {
        StringBuilder output = new StringBuilder();
        output.append("Orderbook: ").append(this.bookID).append(System.getProperty("line.separator"));
        output.append("BID\tASK").append(System.getProperty("line.separator"));

        Iterator<Order> sellIt = sellOrders.iterator();
        Iterator<Order> buyIt = buyOrders.iterator();
        while (sellIt.hasNext() || buyIt.hasNext()) {
            output.append(buyIt.hasNext() ? buyIt.next().toString() : "-------");
            output.append(" - ");
            output.append(sellIt.hasNext() ? sellIt.next().toString() : "-------");
            output.append(System.getProperty("line.separator"));
        }
        return output.toString();
    }

    /**
     * Below methods used for testing purposes; No business logic contained
     */
    public TreeSet<Order> getBuyOrders() {
        return buyOrders;
    }

    public TreeSet<Order> getSellOrders() {
        return sellOrders;
    }

    public void cleanSellOrders() {
        this.sellOrders.clear();
    }

    public void cleanBuyOrders() {
        this.buyOrders.clear();
    }
}

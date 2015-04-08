package com.anneke.cib;

/**
 *
 * @author anneke
 */

public class Order {
    
    public enum OperationType {BUY, SELL}
    
    private int orderID;
    private String bookID;
    private double price;
    private int volume;
    private OperationType operationType;

    public Order(int orderID, String bookID, double price, int volume, OperationType operationType) {
        this.orderID = orderID;
        this.bookID = bookID;
        this.price = price;
        this.volume = volume;
        this.operationType = operationType;
    }

    public Order(int orderID, String bookID) {
        this.orderID = orderID;
        this.bookID = bookID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getBookID() {
        return bookID;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        return volume + "@" + price;
    }
}

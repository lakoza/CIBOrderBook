package com.anneke.cib;

import java.util.TreeSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anneke
 */
public class OrderBookTest {

    private OrderBook orderBook;

    public OrderBookTest() {
    }

    @Before
    public void setUp() {
        orderBook = OrderBook.getOrderBookById("book-1");
    }

    @After
    public void tearDown() {
        orderBook.cleanBuyOrders();
        orderBook.cleanSellOrders();
        orderBook = null;
    }

    @Test
    public void testOrderBookCreation() {
    }

    @Test
    public void testBuyOrderSorting() {
        Order order = new Order(1, "book-1", 100.20, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.0, 2, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.20, 3, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.3, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(4, buy.size());
        assertEquals(4, buy.first().getOrderID());
        assertEquals(100.3, buy.first().getPrice(), 0.0);
        assertEquals(2, buy.last().getOrderID());
        assertEquals(100.0, buy.last().getPrice(), 0.0);
    }

    @Test
    public void testSellOrderSorting() {
        Order order = new Order(1, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.0, 2, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.3, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(4, sell.size());
        assertEquals(2, sell.first().getOrderID());
        assertEquals(100.0, sell.first().getPrice(), 0.0);
        assertEquals(4, sell.last().getOrderID());
        assertEquals(100.3, sell.last().getPrice(), 0.0);
    }

    @Test
    public void testSamePriceOrderSorting() {
        Order order = new Order(1, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.3, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(3, sell.size());
        assertEquals(1, sell.first().getOrderID());
        assertEquals(100.2, sell.first().getPrice(), 0.0);
        assertEquals(3, sell.last().getOrderID());
        assertEquals(100.3, sell.last().getPrice(), 0.0);
    }

    @Test
    public void testSamePriceSameVolumeOrder() {
        Order order = new Order(1, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.2, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(3, sell.size());
        assertEquals(1, sell.first().getOrderID());
    }

    @Test
    public void testSameOrder() {
        Order order = new Order(1, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(1, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(1, "book-1", 100.2, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(1, sell.size());
        assertEquals(1, sell.first().getOrderID());
        
        order = new Order(1, "book-1");
        orderBook.deleteOrder(order);
        sell = orderBook.getSellOrders();
        assertEquals(0, sell.size());
    }

        @Test
    public void testDeleteOrder() {
        Order order = new Order(1, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.0, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.2, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(2, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(1, buy.size());
        
        order = new Order(1, "book-1");
        orderBook.deleteOrder(order);
        sell = orderBook.getSellOrders();
        assertEquals(1, sell.size());
        order = new Order(2, "book-1");
        orderBook.deleteOrder(order);
        buy = orderBook.getBuyOrders();
        assertEquals(0, buy.size());
    }

    @Test
    public void testNoMatchingOrders() {
        Order order = new Order(1, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.3, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.1, 3, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.0, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);

        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(2, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(2, sell.size());
    }

    @Test
    public void testModifyOrder1() {
        Order order = new Order(1, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.2, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);

        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(1, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(0, buy.size());
        
        assertEquals(2, sell.first().getVolume());
    }

    @Test
    public void testModifyOrder2() {
        Order order = new Order(1, "book-1", 100.00, 42, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.00, 42, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.00, 77, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.00, 61, Order.OperationType.BUY);
        orderBook.addOrder(order);


        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(0, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(1, buy.size());
        
        assertEquals(54, buy.first().getVolume());
    }


    @Test
    public void testPerfeclyMatchingOrders1() {
        Order order = new Order(1, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.3, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.2, 3, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.3, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);

        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(0, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(0, buy.size());
    }

    @Test
    public void testPerfeclyMatchingOrders2() {
        Order order = new Order(1, "book-1", 100.20, 3, Order.OperationType.SELL);
        orderBook.addOrder(order);
        order = new Order(3, "book-1", 100.2, 1, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(4, "book-1", 100.2, 3, Order.OperationType.BUY);
        orderBook.addOrder(order);
        order = new Order(2, "book-1", 100.20, 1, Order.OperationType.SELL);
        orderBook.addOrder(order);


        TreeSet<Order> sell = orderBook.getSellOrders();
        assertEquals(0, sell.size());
        TreeSet<Order> buy = orderBook.getBuyOrders();
        assertEquals(0, buy.size());
    }

    @Test
     public void testDifferentOrderBooks() {
         OrderBook.getOrderBookById("book-1");
         OrderBook.getOrderBookById("book-2");
         OrderBook.getOrderBookById("book-3");
         OrderBook.getOrderBookById("book-2");
         assertEquals(3, OrderBook.getOrderBooks().size());
     }
}

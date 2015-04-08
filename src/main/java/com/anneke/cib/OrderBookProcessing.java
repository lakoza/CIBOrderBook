package com.anneke.cib;

import java.io.FileNotFoundException;
import java.util.Map;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author anneke
 */
public class OrderBookProcessing {

    public static void main(String[] args) {
        try {
            if (args.length == 0 || args[0].isEmpty()){
                System.out.println("Please provide the path to orders XML");
                System.exit(1);
            }
            String fileName = args[0];
            long before = System.currentTimeMillis();
            ActionProvider actionProvider = new ActionProvider();
            actionProvider.initProvider(fileName);
            while (actionProvider.hasNext()) {
                Action currentAction = actionProvider.getNext();
                if (currentAction != null) {
                    Order currentOrder = currentAction.getOrder();
                    OrderBook currentOrderBook = OrderBook.getOrderBookById(currentOrder.getBookID());
                    if (currentAction.getActionType() == Action.ActionType.AddOrder) {
                        currentOrderBook.addOrder(currentOrder);
                    } else if (currentAction.getActionType() == Action.ActionType.DeleteOrder) {
                        currentOrderBook.deleteOrder(currentOrder);
                    }
                }
            }
            long after = System.currentTimeMillis();

            Map<String, OrderBook> books = OrderBook.getOrderBooks();
            for (OrderBook book : books.values()) {
                System.out.println(book.printOrderBook());
            }
            System.out.println(after - before + " ms");
            
        } catch (XMLStreamException ex) {
            System.err.println(ex);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
}

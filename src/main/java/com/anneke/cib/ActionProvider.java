package com.anneke.cib;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author anneke
 */
public class ActionProvider {

    private XMLStreamReader reader;

    public void initProvider(String filename) throws XMLStreamException, FileNotFoundException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(new FileReader(filename));
    }

    public boolean hasNext() throws XMLStreamException {
        if (reader != null) {
            return reader.hasNext();
        }
        return false;
    }

    public Action getNext() throws IllegalStateException, NoSuchElementException, XMLStreamException {
        if (reader == null) {
            throw new IllegalStateException("Order privider not initialized");
        }
        while (reader.hasNext()) {
            int event = reader.next();
            if (XMLStreamConstants.START_ELEMENT == event) {
                String currentTag = reader.getLocalName();
                if (Action.ActionType.AddOrder.toString().equals(currentTag)) {
                    String bookID = reader.getAttributeValue(null, "book");
                    Order.OperationType operationType = Order.OperationType.valueOf(reader.getAttributeValue(null, "operation"));
                    double price = Double.parseDouble(reader.getAttributeValue(null, "price"));
                    int volume = Integer.parseInt(reader.getAttributeValue(null, "volume"));
                    int orderID = Integer.parseInt(reader.getAttributeValue(null, "orderId"));

                    Order order = new Order(orderID, bookID, price, volume, operationType);
                    return new Action(Action.ActionType.AddOrder, order);
                }
                if (Action.ActionType.DeleteOrder.toString().equals(currentTag)) {
                    String bookID = reader.getAttributeValue(null, "book");
                    int orderID = Integer.parseInt(reader.getAttributeValue(null, "orderId"));

                    Order order = new Order(orderID, bookID);
                    return new Action(Action.ActionType.DeleteOrder, order);
                }
            }
        }
        return null;
        //throw new NoSuchElementException("No next element");
    }
}

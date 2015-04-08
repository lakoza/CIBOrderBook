package com.anneke.cib;

/**
 *
 * @author anneke
 */
public class Action {

    public enum ActionType {AddOrder, DeleteOrder}

    private ActionType actionType;
    private Order order;

    public Action(ActionType actionType, Order order) {
        this.actionType = actionType;
        this.order = order;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Order getOrder() {
        return order;
    }
}

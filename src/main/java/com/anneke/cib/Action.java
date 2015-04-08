/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

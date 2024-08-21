package org.example.BusinessLogic;

import org.example.DataAccess.OrderDAO;
import org.example.Model.OrderProducts;
import org.example.Model.Orders;

import java.sql.Timestamp;
import java.util.List;

/**
 * Business Logic Layer for managing orders.
 */
public class OrderBLL {
    private OrderDAO orderDAO;

    /**
     * Constructs a new OrderBLL object with necessary dependencies.
     */
    public OrderBLL() { this.orderDAO = new OrderDAO(); }

    /**
     * Creates a new order for a client with the specified date.
     *
     * @param clientId The ID of the client placing the order.
     * @param date     The timestamp representing the order date.
     * @return The created order.
     */
    public Orders createOrder(int clientId, Timestamp date) {
       return orderDAO.insert(new Orders(clientId, date));
    }
}

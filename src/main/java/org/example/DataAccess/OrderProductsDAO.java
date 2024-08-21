package org.example.DataAccess;

import org.example.Connection.ConnectionFactory;
import org.example.Model.OrderProducts;
import org.example.Model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for handling operations related to OrderProducts in the database.
 */
public class OrderProductsDAO extends AbstractDAO<OrderProducts> {

    /**
     * Constructs an OrderProductsDAO instance.
     */
    public OrderProductsDAO() { super(); }

    /**
     * Inserts a new order product into the database.
     *
     * @param orderProducts The OrderProducts object representing the new order product to insert.
     * @return The inserted order product object with updated ID if applicable, or null on failure.
     */
    public OrderProducts insert(OrderProducts orderProducts) {
        return super.insert(orderProducts);
    }

    public void deleteOrderProductsByClientID(int clientId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM orderproducts WHERE orderid IN (SELECT id FROM orders WHERE clientid = ?);";
            statement = connection.prepareStatement(query);

            // Set the id parameter
            statement.setInt(1, clientId);

            int affectedRows = statement.executeUpdate();
            //if (affectedRows == 0) {
                //throw new SQLException("Deleting entity failed, no rows affected.");
            //}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void deleteOrderProductsByProductID(int productId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM orderproducts WHERE productid = ?;";
            statement = connection.prepareStatement(query);

            // Set the id parameter
            statement.setInt(1, productId);

            int affectedRows = statement.executeUpdate();
            //if (affectedRows == 0) {
            //throw new SQLException("Deleting entity failed, no rows affected.");
            //}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}

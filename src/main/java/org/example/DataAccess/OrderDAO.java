package org.example.DataAccess;

import org.example.Connection.ConnectionFactory;
import org.example.Model.OrderProducts;
import org.example.Model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) for handling operations related to Orders in the database.
 */
public class OrderDAO extends AbstractDAO<Orders> {
    /**
     * Constructs an OrderDAO instance.
     */
    public OrderDAO() { super(); }

    /**
     * Inserts a new order into the database.
     *
     * @param order The Orders object representing the new order to insert.
     * @return The inserted order object with updated ID if applicable, or null on failure.
     */
    public Orders insert(Orders order) {
        return super.insert(order);
    }


    /**
     * Deletes orders associated with a specific client ID from the database.
     *
     * @param clientId The unique identifier of the client whose orders are to be deleted.
     */
    public void deleteByClientID (int clientId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM orders WHERE clientid = ?;";
            statement = connection.prepareStatement(query);

            // Set the id parameter
            statement.setInt(1, clientId);

            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Deleting entity failed, no rows affected.");
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}

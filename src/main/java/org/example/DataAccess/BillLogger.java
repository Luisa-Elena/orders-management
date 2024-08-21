package org.example.DataAccess;

import org.example.Connection.ConnectionFactory;
import org.example.Model.Bill;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles logging bills to the database and retrieving logged bills.
 */
public class BillLogger {
    /**
     * Adds a bill to the log table in the database.
     * @param bill The Bill object representing the bill to be logged.
     */
    public void addBill(Bill bill) {
        String query = "INSERT INTO log (orderId, sum) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(query);

            statement.setInt(1, bill.orderId());
            statement.setBigDecimal(2, bill.sum());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bill added successfully.");
            } else {
                System.out.println("Failed to add the bill.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of all bills logged in the database.
     * @return A list of Bill objects representing the logged bills.
     */
    public List<Bill> getBills() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM log";

        Connection conn = null;
        Statement statement = null;

        try {
            conn = ConnectionFactory.getConnection();
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                BigDecimal sum = rs.getBigDecimal("sum");
                bills.add(new Bill(orderId, sum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    /**
     * Deletes billing information associated with orders from a specific client ID in the database.
     *
     * @param clientId The unique identifier of the client whose billing information is to be deleted.
     */
    void deleteBillByClientId(int clientId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM log WHERE orderid IN (SELECT id FROM orders WHERE clientid = ?);";
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
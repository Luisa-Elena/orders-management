package org.example.DataAccess;

import org.example.Connection.ConnectionFactory;
import org.example.Model.Clients;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) for handling operations related to Clients in the database.
 */
public class ClientDAO extends AbstractDAO<Clients> {
    private OrderProductsDAO orderProductsDAO;
    private OrderDAO orderDAO;
    private BillLogger billLogger;

    /**
     * Constructs a ClientDAO instance.
     */
    public ClientDAO() {
        super();
        orderProductsDAO = new OrderProductsDAO();
        orderDAO = new OrderDAO();
        billLogger = new BillLogger();
    }

    /**
     * Finds a client by their ID.
     *
     * @param clientId The ID of the client to find.
     * @return The Clients object representing the found client, or null if not found.
     */
    public Clients findById(int clientId) {
        return super.findById(clientId);
    }

    /**
     * Inserts a new client into the database.
     *
     * @param newClient The Clients object representing the new client to insert.
     * @return The inserted client object with updated ID if applicable, or null on failure.
     */
    public Clients insert(Clients newClient) { return super.insert(newClient); }

    /**
     * Updates an existing client in the database.
     *
     * @param clientWithUpdatedValues The Clients object representing the updated client values.
     * @return The updated client object, or null on failure.
     */
    public Clients update(Clients clientWithUpdatedValues) { return super.update(clientWithUpdatedValues); }

    /**
     * Deletes a client from the database by its ID.
     *
     * @param id The ID of the client to delete.
     * @return The deleted client object, or null on failure.
     */
    public Clients delete(int id) {
        //delete from orderproducts
        orderProductsDAO.deleteOrderProductsByClientID(id);

        //delete from bill
        billLogger.deleteBillByClientId(id);

        //delete from orders
        orderDAO.deleteByClientID(id);

        //delete client
        return super.delete(id);
    }

    /**
     * Retrieves a list of all clients from the database.
     *
     * @return A list of Clients objects representing all clients found.
     */
    public List<Clients> findAll() { return super.findAll(); }

    /**
     * Finds a client by their name.
     *
     * @param name The name of the client to find.
     * @return The Clients object representing the found client, or null if not found.
     */
    public Clients findByName(String name) {
        Clients client = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM clients WHERE clientname = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt("id");
                String clientname = resultSet.getString("clientname");
                String email = resultSet.getString("email");
                client = new Clients(id, clientname, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }

    /**
     * Gets a JPanel containing a populated table with data
     *
     * @return The Clients object representing the found client, or null if not found.
     */
    public JPanel findAllPanel() {return super.findAllPanel();}
}

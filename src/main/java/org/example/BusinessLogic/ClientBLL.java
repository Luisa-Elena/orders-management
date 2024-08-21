package org.example.BusinessLogic;

import org.example.BusinessLogic.Validators.EmailValidator;
import org.example.DataAccess.ClientDAO;
import org.example.Model.Clients;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business Logic Layer for managing Clients.
 */
public class ClientBLL {
    private ClientDAO clientDAO;
    private EmailValidator emailValidator;

    /**
     * Constructs a new ClientBLL object with necessary dependencies.
     */
    public ClientBLL() {
        emailValidator = new EmailValidator();
        clientDAO = new ClientDAO();
    }

    /**
     * Retrieves a client by its ID.
     *
     * @param clientId The ID of the client to retrieve.
     * @return The client with the specified ID.
     * @throws NoSuchElementException if the client with the specified ID is not found.
     */
    public Clients findClientById(int clientId) {
        Clients c = clientDAO.findById(clientId);
        if (c == null) {
            throw new NoSuchElementException("The client with id = " + clientId + " was not found.");
        }
        return c;
    }

    /**
     * Inserts a new client after validating its email address.
     *
     * @param newClient The client to insert.
     * @return The inserted client.
     */
    public Clients insertNewClient(Clients newClient) {
        try {
            emailValidator.validate(newClient);
            return clientDAO.insert(newClient);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing client after validating its email address.
     *
     * @param clientWithUpdatedValues The updated client object.
     * @return The updated client.
     */
    public Clients updateClient(Clients clientWithUpdatedValues) {
        try {
            emailValidator.validate(clientWithUpdatedValues);
            return clientDAO.update(clientWithUpdatedValues);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a client by its ID.
     *
     * @param id The ID of the client to delete.
     * @return The deleted client.
     */
    public Clients deleteClient(int id) {
        return clientDAO.delete(id);
    }

    /**
     * Retrieves all clients.
     *
     * @return A list of all clients.
     */
    public List<Clients> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Finds a client by its name.
     *
     * @param name The name of the client to find.
     * @return The client with the specified name.
     */
    public Clients findByName(String name) { return clientDAO.findByName(name); }

    /**
     * Gets a JPanel with a table populated with all data from the database table
     *
     * @return The client with the specified name.
     */
    public JPanel findAllPanel() {return clientDAO.findAllPanel(); }
}

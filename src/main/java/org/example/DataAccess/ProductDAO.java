package org.example.DataAccess;
import org.example.Model.Products;

import javax.swing.*;
import java.util.List;

/**
 * Data Access Object (DAO) for handling operations related to Products in the database.
 */
public class ProductDAO extends AbstractDAO<Products> {
    private OrderProductsDAO orderProductsDAO;
    /**
     * Constructs a ProductDAO instance.
     */
    public ProductDAO() {
        super();
        orderProductsDAO = new OrderProductsDAO();
    }

    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product to find.
     * @return The Products object representing the found product, or null if not found.
     */
    public Products findById(int productId) {
        return super.findById(productId);
    }

    /**
     * Inserts a new product into the database.
     *
     * @param newProduct The Products object representing the new product to insert.
     * @return The inserted product object with updated ID if applicable, or null on failure.
     */
    public Products insert(Products newProduct) { return super.insert(newProduct); }

    /**
     * Updates an existing product in the database.
     *
     * @param productWithUpdatedValues The Products object representing the updated product values.
     * @return The updated product object, or null on failure.
     */
    public Products update(Products productWithUpdatedValues) { return super.update(productWithUpdatedValues); }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete.
     * @return The deleted product object, or null on failure.
     */
    public Products delete(int id) {
        //delete from orderproducts
        orderProductsDAO.deleteOrderProductsByProductID(id);

        return super.delete(id);
    }

    /**
     * Retrieves JTable of all products from the database.
     *
     * @return A JPanle containing a table with all products.
     */
    public JPanel findAllPanel() { return super.findAllPanel(); }
}

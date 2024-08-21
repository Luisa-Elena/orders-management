package org.example.BusinessLogic;

import org.example.BusinessLogic.Validators.AmountValidator;
import org.example.DataAccess.ProductDAO;
import org.example.Model.Products;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business Logic Layer for managing products.
 */
public class ProductBLL {
    private ProductDAO productDAO;
    private AmountValidator amountValidator;

    /**
     * Constructs a new ProductBLL object with necessary dependencies.
     */
    public ProductBLL() {
        this.productDAO = new ProductDAO();
        this.amountValidator = new AmountValidator();
    }

    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product to find.
     * @return The product with the specified ID.
     * @throws NoSuchElementException if the product with the specified ID is not found.
     */
    public Products findProductById(int productId) {
        Products p = productDAO.findById(productId);
        if (p == null) {
            throw new NoSuchElementException("The product with id = " + productId + " was not found.");
        }
        return p;
    }

    /**
     * Inserts a new product.
     *
     * @param newProduct The product to insert.
     * @return The inserted product.
     */
    public Products insertNewProduct(Products newProduct) {
        try {
            amountValidator.validate(newProduct);
            return productDAO.insert(newProduct);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing product.
     *
     * @param productWithUpdatedValues The updated product object.
     * @return The updated product.
     */
    public Products updateProduct(Products productWithUpdatedValues) {
        try {
            amountValidator.validate(productWithUpdatedValues);
            return productDAO.update(productWithUpdatedValues);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return The deleted product.
     */
    public Products deleteProduct(int id) {
        return productDAO.delete(id);
    }

    /**
     * Retrieves all products.
     *
     * @return A list of all products.
     */
    public List<Products> findAllProducts() {
        return productDAO.findAll();
    }


    /**
     * Retrieves all products.
     *
     * @return A JPanel containing a table with all products.
     */
    public JPanel findAllProductsPanel() {
        return productDAO.findAllPanel();
    }


}

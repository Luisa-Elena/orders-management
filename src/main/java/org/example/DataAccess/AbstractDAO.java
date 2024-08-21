package org.example.DataAccess;

import org.example.Connection.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Generic Data Access Object (DAO) for database operations on entities.
 * @param <T> The type of entity handled by this DAO.
 */
public class AbstractDAO<T> {
    //protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructs an AbstractDAO instance.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + "=?");
        return sb.toString();
    }

    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(type.getSimpleName().toLowerCase()).append(" (");
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if(!field.getName().equals("id")) {
                sb.append(field.getName().toLowerCase()).append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        sb.append(") VALUES (");
        for (Field field : fields) {
            if(!field.getName().equals("id")) {
                sb.append("?, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        sb.append(")");
        return sb.toString();
    }

    private String createUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(type.getSimpleName()).append(" SET ");
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (!field.getName().equals("id")) {
                sb.append(field.getName()).append("=?, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        sb.append(" WHERE id=?");
        return sb.toString();
    }

    private String createDeleteQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(type.getSimpleName()).append(" WHERE id=?");
        return sb.toString();
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds all entities of type T in the database.
     * @return A list of all entities found.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return new ArrayList<>(); // Return an empty list on failure
    }

    /**
     * Finds an entity by its ID in the database.
     * @param id The ID of the entity to find.
     * @return The entity found, or null if not found.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            //LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Inserts a new entity into the database.
     * @param t The entity object to insert.
     * @return The inserted entity with updated ID if applicable, or null on failure.
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = createInsertQuery();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int parameterIndex = 1;
            for (Field field : type.getDeclaredFields()) {
                if(!field.getName().equals("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method getter = propertyDescriptor.getReadMethod();
                    Object value = getter.invoke(t);
                    statement.setObject(parameterIndex++, value);
                }
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting entity failed, no rows affected.");
            }

            if(!type.getSimpleName().equals("OrderProducts")) {
                // Retrieve generated keys (including the ID)
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    PropertyDescriptor idPropertyDescriptor = new PropertyDescriptor("id", type);
                    Method idSetter = idPropertyDescriptor.getWriteMethod();
                    idSetter.invoke(t, id);
                } else {
                    throw new SQLException("Inserting entity failed, no generated keys obtained.");
                }
            }

            return t;

        } catch (SQLException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();

        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Updates an existing entity in the database.
     * @param t The entity object to update.
     * @return The updated entity, or null on failure.
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = createUpdateQuery();
            statement = connection.prepareStatement(query);

            int parameterIndex = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method getter = propertyDescriptor.getReadMethod();
                    Object value = getter.invoke(t);
                    statement.setObject(parameterIndex++, value);
                }
            }
            // Set the id parameter
            PropertyDescriptor idPropertyDescriptor = new PropertyDescriptor("id", type);
            Method idGetter = idPropertyDescriptor.getReadMethod();
            Object idValue = idGetter.invoke(t);
            statement.setObject(parameterIndex, idValue);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating entity failed, no rows affected.");
            }
            return t;

        } catch (SQLException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Deletes an entity from the database by its ID.
     * @param id The ID of the entity to delete.
     * @return The deleted entity, or null on failure.
     */
    public T delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        T t = findById(id);

        try {
            connection = ConnectionFactory.getConnection();
            String query = createDeleteQuery();
            statement = connection.prepareStatement(query);

            // Set the id parameter
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting entity failed, no rows affected.");
            }

            return t;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creates and returns a JPanel populated with data - all the thable from the database
     * @return The JPanel populated with data
     */
    public JPanel findAllPanel() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String tableName = type.getSimpleName().toLowerCase(); // Assuming table name matches class name
        String query = "SELECT * FROM " + tableName;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createTablePanel(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return new JPanel(); // Return an empty panel on failure
    }

    /**
     * Creates and returns a JPanel populated with data from a ResultSet
     * @param resultSet the ResultSet from which the table must be populated
     * @return The JPanel populated with data
     */
    private JPanel createTablePanel(ResultSet resultSet) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            tableModel.addColumn(columnName);
        }

        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = resultSet.getObject(i + 1);
            }
            tableModel.addRow(rowData);
        }

        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        return panel;
    }
}

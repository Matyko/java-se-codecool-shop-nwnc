package com.codecool.shop.dao.implementation.database;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.DbConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by abelvaradi on 2017.05.09..
 */
public class SupplierDaoJDBC implements SupplierDao {
    private static SupplierDaoJDBC instance = null;

    private DbConnect dbConnect;
    private static String defaultFilepath = "src/main/resources/connection/properties/connectionProperties.txt";

    private SupplierDaoJDBC() {
        dbConnect = new DbConnect(defaultFilepath);
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    public void setDbConnectForTest(String testFilePath) {
        dbConnect = new DbConnect(testFilePath);
    }

    @Override
    public void add(Supplier supplier) {
        if (getIdByName(supplier.getName()) == 0) {
            try {
                PreparedStatement stmt;
                stmt = dbConnect.getConnection().prepareStatement(
                        ("INSERT INTO supplier (name, description) VALUES (?, ?)"));
                stmt.setString(1, supplier.getName());
                stmt.setString(2, supplier.getDescription());
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                while (resultSet.next()) {
                    supplier.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Supplier find(int id) {

        String query = "SELECT * FROM supplier WHERE id ='" + id + "';";

        try (Connection connection = dbConnect.getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)

        ){
            while (resultSet.next()){
                Supplier supplier = new Supplier(
                        resultSet.getString("name"),
                        resultSet.getString("description"));
                supplier.setId(resultSet.getInt(1));
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean remove(int id) {
        Supplier supplier = find(id);
        if (supplier==null) {
            return false;
        }
        String query = "DELETE FROM supplier WHERE id=" + id + ";";
        try (Connection connection = dbConnect.getConnection();
             Statement statement = connection.createStatement();
        ) {             statement.executeQuery(query) ; }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM supplier;";

        List<Supplier> supplierList = new ArrayList<>();

        try (Connection connection = dbConnect.getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
                supplier.setId(resultSet.getInt(1));
                supplierList.add(supplier);
            }
        }
        catch (SQLException e) {
                e.printStackTrace();
            }
            return supplierList;

    }

    public int getIdByName(String name) {
        int result = 0;
        String query = "SELECT * FROM supplier WHERE name=?;";
        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            PreparedStatement pstmt = connection.prepareStatement( query );
            pstmt.setString( 1, name);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                result = Integer.parseInt((resultSet.getString("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}




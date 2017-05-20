package com.codecool.shop.dao.implementation.database;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Customer;
import com.codecool.shop.util.DbConnect;

import java.sql.*;
import java.util.List;

/**
 * Created by atsidir on 2017.05.09..
 */
public class CustomerDaoJDBC implements CustomerDao {

    private static CustomerDaoJDBC instance = null;


    private CustomerDaoJDBC() {
    }

    public static CustomerDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CustomerDaoJDBC();
        }
        return instance;
    }

    @Override
    public int add(Customer customer) {
       try (
            PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                    ("INSERT INTO customers" +
                            "(firstname," +
                            "lastname," +
                            "email," +
                            "phonenumber," +
                            "billingaddress," +
                            "shippingaddress,"+
                            "shoppingcartid"+") VALUES (?, ?, ?,?,?,?,?)"),Statement.RETURN_GENERATED_KEYS);
            )
           {

            ShoppingCartDao shoppingCartDao=ShoppingCartDaoJDBC.getInstance();
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setInt(5, 1);
            stmt.setInt(6, 1);
            stmt.setInt(7, shoppingCartDao.addNewCartToDb());
            stmt.executeUpdate();
            ResultSet rs= stmt.getGeneratedKeys();

            while (rs.next()){
                return rs.getInt(1);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public Customer find(int customerid) {
       String query = "SELECT * FROM customers WHERE id ='" + customerid + "';";

        try (Connection connection = DbConnect.getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

        ){
            ShoppingCartDao shoppingCartDao=ShoppingCartDaoJDBC.getInstance();


            while (resultSet.next()){

                Customer customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("phonenumber"),
                        shoppingCartDao.find(resultSet.getInt("shoppingcartid")));
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void remove(int id) {

    }

    @Override
    public List<Customer> getAll() {
        return null;
    }

}

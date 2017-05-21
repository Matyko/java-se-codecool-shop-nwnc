package com.codecool.shop.dao.implementation.database;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.util.DbConnect;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShoppingCartDaoJDBC implements ShoppingCartDao {
    private Logger logger = LoggerFactory.getLogger(ShoppingCartDao.class);
    private static ShoppingCartDaoJDBC instance = null;


    private ShoppingCartDaoJDBC() {
    }

    public static ShoppingCartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoJDBC();
        }
        return instance;
    }

    @Override
    public void addNewCartElement(ShoppingCart cart, Product product) {
        if (isElementInCart(cart, product)) {

            try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("UPDATE shoppingcarelements SET productcount = ? WHERE (shoppingcartid = ?) AND (productid = ?)"))
            ) {

                int count = getCartElementCount(cart, product);
                stmt.setInt(1, count + 1);
                stmt.setInt(2, cart.getId());
                stmt.setInt(3, product.getId());
                stmt.executeUpdate();
                logger.debug("New element added to cart "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }

        } else {
            try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("INSERT INTO shoppingcarelements" +
                                    "(shoppingcartid," +
                                    "productid," +
                                    "productcount" + ") VALUES (?, ?, ?)"), Statement.RETURN_GENERATED_KEYS);
            ) {

                stmt.setInt(1, cart.getId());
                stmt.setInt(2, product.getId());
                stmt.setInt(3, 1);
                stmt.executeUpdate();
                logger.debug("Element amount raised in cart "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    public void addNewCartElement(ShoppingCart cart, Product product,int num) {

        if (isElementInCart(cart, product)) {

            try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("UPDATE shoppingcarelements SET productcount = ? WHERE (shoppingcartid = ?) AND (productid = ?)"))
            ) {

                int count = getCartElementCount(cart, product);
                stmt.setInt(1, count+num);
                stmt.setInt(2, cart.getId());
                stmt.setInt(3, product.getId());
                stmt.executeUpdate();
                logger.debug(num+" of a new element added to cart: "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }

        } else {
            try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("INSERT INTO shoppingcarelements" +
                                    "(shoppingcartid," +
                                    "productid," +
                                    "productcount" + ") VALUES (?, ?, ?)"), Statement.RETURN_GENERATED_KEYS);
            ) {

                stmt.setInt(1, cart.getId());
                stmt.setInt(2, product.getId());
                stmt.setInt(3, num);
                stmt.executeUpdate();
                logger.debug(num+" of a element added to cart: "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    public int getCartElementCount(ShoppingCart cart, Product product) {
        String query = "SELECT * FROM shoppingcarelements WHERE (shoppingcartid ='" + cart.getId() + "') AND (productid ='" + product.getId() + "');";

        try (Connection connection = DbConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

        ) {

            while (resultSet.next()) {
                return resultSet.getInt("productcount");
            }

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    public boolean isElementInCart(ShoppingCart cart, Product product) {
        String query = "SELECT * FROM shoppingcarelements WHERE (shoppingcartid ='" + cart.getId() + "') AND (productid ='" + product.getId() + "');";

        try (Connection connection = DbConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

        ) {

            while (resultSet.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int addNewCartToDb() {

        try (
                Connection connection = DbConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO shoppingcarts DEFAULT VALUES;", Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public ShoppingCart find(int id) {
        String query = "SELECT * FROM shoppingcarelements WHERE shoppingcartid ='" + id + "';";

        try (Connection connection = DbConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

        ) {
            ProductDao productDataStore = ProductDaoJDBC.getInstance();
            HashMap<Product, Integer> cartelements = new HashMap<>();

            while (resultSet.next()) {
                cartelements.put(productDataStore.find(resultSet.getInt("productid")), resultSet.getInt("productcount"));
            }

            if (cartelements.size() == 0) {
                return new ShoppingCart(id, cartelements);
            }

            return new ShoppingCart(id, cartelements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeElementFromCart(Product product, ShoppingCart cart) {
        if (getCartElementCount(cart, product) == 1) {
            deleteElementsFromCart(product, cart);
        } else {
            try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("UPDATE shoppingcarelements SET productcount = ? WHERE (shoppingcartid = ?) AND (productid = ?)"))
            ) {
                int count = getCartElementCount(cart, product);
                stmt.setInt(1, count - 1);
                stmt.setInt(2, cart.getId());
                stmt.setInt(3, product.getId());
                stmt.executeUpdate();
                logger.debug("Element removed from cart "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    public void deleteElementsFromCart(Product product,ShoppingCart cart){
        try (
                PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("DELETE FROM shoppingcarelements  WHERE (shoppingcartid = ?) AND (productid = ?)"))
            ) {
                stmt.setInt(1, cart.getId());
                stmt.setInt(2, product.getId());
                stmt.executeUpdate();
            logger.debug("Element deleted from cart "+product.toString());
            } catch (SQLException e) {
                e.getStackTrace();
            }
    }

    @Override
    public void remove(int id) {
    }

    @Override
    public void mergeCarts(ShoppingCart sessionCart,ShoppingCart databaseCart){
        for (Map.Entry<Product,Integer> entry:sessionCart.getAll().entrySet()){
            addNewCartElement(databaseCart,entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void setElementCount(ShoppingCart cart,int productid,int newamount) {
        try (
                    PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                            ("UPDATE shoppingcarelements SET productcount = ? WHERE (shoppingcartid = ?) AND (productid = ?)"))
            ) {
                stmt.setInt(1, newamount);
                stmt.setInt(2, cart.getId());
                stmt.setInt(3, productid);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.getStackTrace();
            }
    }

    @Override
    public List<ShoppingCart> getAll() { return null;}
}

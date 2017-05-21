package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.implementation.database.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.database.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.database.SupplierDaoJDBC;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.DbConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by matyi on 2017.05.10..
 */
class ProductDaoJDBCTest {

    private String filePath = "src/main/resources/connection/properties/connectionPropertiesForTest.txt";
    private DbConnect dbConnect = new DbConnect(filePath);
    private Supplier amazon;
    private ProductCategory tablet;
    private Product product;

    private ProductDaoJDBC productSetConnection() {
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        productDaoJDBC.setDbConnectForTest(filePath);
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        supplierDaoJDBC.setDbConnectForTest(filePath);
        amazon = new Supplier("Amazon", "Digital content and services");
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        productCategoryDaoJDBC.setDbConnectForTest(filePath);
        tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        supplierDaoJDBC.add(amazon);
        productCategoryDaoJDBC.add(tablet);
        product = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", productCategoryDaoJDBC.find(productCategoryDaoJDBC.getIdByName(tablet.getName())), supplierDaoJDBC.find(supplierDaoJDBC.getIdByName(amazon.getName())));
        productDaoJDBC.add(product);
        return productDaoJDBC;
    }


    @BeforeEach
    void setUp() {
        String query = "TRUNCATE TABLE productcategory, supplier, product";
        try {
            Connection connection = dbConnect.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetInstance() {
        ProductDaoJDBC productDaoJDBC1 = ProductDaoJDBC.getInstance();
        ProductDaoJDBC productDaoJDBC2 = ProductDaoJDBC.getInstance();
        assertEquals(productDaoJDBC1.hashCode(), productDaoJDBC2.hashCode());
    }

    @Test
    void testAdd() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        assertEquals(1, productDaoJDBC.getAll().size());
    }

    @Test
    void testFindIfInvalidParameter1() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        assertEquals(null, productDaoJDBC.find(-1));
        assertEquals(null, productDaoJDBC.find(0));
    }

    @Test
    void testGetAll() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        assertEquals(1, productDaoJDBC.getAll().size());
    }

    @Test
    void testGetByProductCategory() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        productCategoryDaoJDBC.setDbConnectForTest(filePath);
        Product test = productDaoJDBC.getBy(productCategoryDaoJDBC.find(productCategoryDaoJDBC.getIdByName(tablet.getName()))).get(0);
        assertEquals(product.getName(), test.getName());
    }

    @Test
    void testGetBySupplier() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        supplierDaoJDBC.setDbConnectForTest(filePath);
        Product test = productDaoJDBC.getBy(supplierDaoJDBC.find(supplierDaoJDBC.getIdByName(amazon.getName()))).get(0);
        assertEquals(product.getName(), test.getName());
    }

    @Test
    void remove() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        productDaoJDBC.remove(productDaoJDBC.getIdByName(product.getName()));
        assertEquals(0, productDaoJDBC.getAll().size());
    }

    @Test
    public void testClearDataStorage() {
        ProductDaoJDBC productDaoJDBC = productSetConnection();
        assertFalse(productDaoJDBC.empty());
    }

}
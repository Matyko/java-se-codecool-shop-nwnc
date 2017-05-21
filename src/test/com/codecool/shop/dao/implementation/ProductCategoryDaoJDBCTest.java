package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.database.ProductCategoryDaoJDBC;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.util.DbConnect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryDaoJDBCTest {

    private String filePath = "src/main/resources/connection/properties/connectionPropertiesForTest.txt";

    @BeforeEach
    void setUp() {
        DbConnect dbConnect = new DbConnect(filePath);
        String query = "TRUNCATE TABLE productcategory, supplier, product";
        try {
            Statement statement = dbConnect.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testIsProductCategoryDaoMemIsSingletone() {
        ProductCategoryDao productCategoryDataStore1 = ProductCategoryDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore2 = ProductCategoryDaoJDBC.getInstance();
        assertEquals(productCategoryDataStore1.hashCode(), productCategoryDataStore2.hashCode());
    }

    @Test
    public void testRemoveById() {
        ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        productCategoryDataStore.setDbConnectForTest(filePath);
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet);
        productCategoryDataStore.remove(1);
        assertEquals(null, productCategoryDataStore.find(1));
    }

    @Test
    public void testRemoveByNonExistingId() {
        ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        productCategoryDataStore.setDbConnectForTest(filePath);
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet);
        assertFalse(productCategoryDataStore.remove(0));
    }

    @Test
    public void testGetAllProductCategory() {
        ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        productCategoryDataStore.setDbConnectForTest(filePath);
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet.");
        productCategoryDataStore.add(tablet);
        ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "Is a small, portable personal computer.");
        productCategoryDataStore.add(laptop);
        ProductCategory videoCard = new ProductCategory("Video Card", "Hardware", "A video card.");
        productCategoryDataStore.add(videoCard);
        assertEquals(3, productCategoryDataStore.getAll().size());
    }

    @Test
    public void testClearDataStorage() {
        ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        assertFalse(productCategoryDataStore.empty());
    }
}
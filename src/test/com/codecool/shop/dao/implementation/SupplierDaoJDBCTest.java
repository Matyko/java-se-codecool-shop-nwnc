package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.implementation.database.SupplierDaoJDBC;
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
class SupplierDaoJDBCTest {

    private static String filepath = "src/main/resources/connection/properties/connectionPropertiesForTest.txt";
    private DbConnect dbConnect = new DbConnect(filepath);


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
    public void testIsProductCategoryDaoMemIsSingletone () {
        SupplierDaoJDBC supplierDataStore2 = SupplierDaoJDBC.getInstance();
        SupplierDaoJDBC supplierDataStore1 = SupplierDaoJDBC.getInstance();
        assertEquals(supplierDataStore1.hashCode(),supplierDataStore2.hashCode());
    }


    @Test
    public void testFindByIdIfNonExistingId() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        assertEquals(null,supplierDataStore.find(-4));
    }

    @Test
    public void testFindByIfIdZero() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        assertEquals(null,supplierDataStore.find(0));
    }

    @Test
    public void testRemoveById() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        supplierDataStore.getIdByName(amazon.getName());
        int id = supplierDataStore.getIdByName(amazon.getName());
        assertEquals(true,supplierDataStore.remove(id));
    }

    @Test
    public void testRemoveByNonExistingId() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        assertEquals(false,supplierDataStore.remove(2));
    }

    @Test
    public void testGetAllSupplier() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);
        Supplier samsung = new Supplier("Samsung", "High-tech electronics manufacturing and digital media.");
        supplierDataStore.add(samsung);
        Supplier gigabyte = new Supplier("Gigabyte", "International manufacturer and distributor of computer hardware products.");
        supplierDataStore.add(gigabyte);
        Supplier msi = new Supplier("MSI", "International manufacturer and distributor of computer hardware products.");
        supplierDataStore.add(msi);
        Supplier asus = new Supplier("Asus", "International manufacturer and distributor of computer hardware products.");
        supplierDataStore.add(asus);
        assertEquals(6,supplierDataStore.getAll().size());
    }

    @Test
    public void testGetIdByName() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        int id = supplierDataStore.getIdByName("Amazon");
        assertNotNull(id,Integer.toString(id));
    }

    @Test
    public void testSameSupplierNameCannotAdd() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        supplierDataStore.setDbConnectForTest(filepath);
        Supplier amazon1 = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon1);
        Supplier amazon2 = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon2);
        int size = supplierDataStore.getAll().size();
        assertEquals(1, size);
    }

    @Test
    public void testClearDataStorage() {
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        assertFalse(supplierDataStore.empty());
    }
}
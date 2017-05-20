package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.memory.SupplierDaoMem;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peter on 2017.05.09..
 */
class SupplierDaoMemTest {

    @BeforeEach
    public void clearDataStore() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        supplierDataStore.empty();
    }

    @AfterEach
    public void clearExampleData() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        for (int i=0; i<supplierDataStore.getAll().size(); i++) {
            supplierDataStore.remove(i);
        }
    }

    @Test
    public void testSameSupplierNameCannotAdd() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        Supplier amazon1 = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon1);
        Supplier amazon2 = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon2);
        int size = supplierDataStore.getAll().size();
        assertEquals(1, size);
    }

    @Test
    public void testIsSupplierDaoMemIsSingletone () {
        SupplierDao supplierDataStore1 = SupplierDaoMem.getInstance();
        SupplierDao supplierDataStore2 = SupplierDaoMem.getInstance();
        assertEquals(supplierDataStore1.hashCode(),supplierDataStore2.hashCode());
    }

    @Test
    public void testFindByIdIfNonExistingId() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        assertEquals(null,supplierDataStore.find(-4));
    }

    @Test
    public void testFindByIfIdZero() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        assertEquals(null,supplierDataStore.find(0));
    }

    @Test
    public void testRemoveById() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        supplierDataStore.remove(1);
        assertEquals(null,supplierDataStore.find(1));
    }

    @Test
    public void testRemoveByNonExistingId() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        assertFalse(supplierDataStore.remove(0));
    }

    @Test
    public void testGetAllSupplier() {
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
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

}
package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.ExampleData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoMemTest {

    @BeforeEach
    void setUp() {
        ProductDao productDao = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDao = SupplierDaoMem.getInstance();
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        productCategoryDao.empty();
        supplierDao.empty();
        productDao.empty();
        productCategoryDao.add(tablet);
        supplierDao.add(amazon);
        productDao.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
    }

    @AfterEach
    void tearDown() {
        ProductDao productDao = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDao = SupplierDaoMem.getInstance();
        productDao.remove(1);
        productCategoryDao.remove(1);
        supplierDao.remove(1);

    }

    @Test
    void testGetInstance() {
        ProductDao product1 = ProductDaoMem.getInstance();
        ProductDao product2 = ProductDaoMem.getInstance();
        assertEquals(product1.hashCode(),product2.hashCode());
    }

    @Test
    void testAdd() {
        ProductDao productDao = ProductDaoMem.getInstance();
        assertEquals(1,productDao.getAll().size());
        productDao.remove(1);
    }

    @Test
    void testFind() {
        ProductDao productDao = ProductDaoMem.getInstance();
        assertEquals("Amazon Fire",productDao.find(1).getName());

    }

    @Test
    void testRemove() {
        ProductDao productDao = ProductDaoMem.getInstance();
        productDao.remove(1);
        assertEquals(0,productDao.getAll().size());
    }

    @Test
    void getAll() {
        ProductDao productDao = ProductDaoMem.getInstance();
        assertEquals(1,productDao.getAll().size());
    }

    @Test
    void testGetBySupplier() {
        ProductDao productDao = ProductDaoMem.getInstance();
        SupplierDao supplierDao = SupplierDaoMem.getInstance();
        assertEquals(1, productDao.getBy(supplierDao.find(1)).size());
    }

    @Test
    void testGetByProductCategory() {
        ProductCategoryDao productCategoryDao = ProductCategoryDaoMem.getInstance();
        ProductDao productDao = ProductDaoMem.getInstance();
        assertEquals(1, productDao.getBy(productCategoryDao.find(1)).size());
    }

}
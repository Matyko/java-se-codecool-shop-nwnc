package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.memory.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peter on 2017.05.09..
 */
class ProductCategoryDaoMemTest {

    @BeforeEach
    public void clearDataStore() {
        ProductCategoryDao productCategoryDao = ProductCategoryDaoMem.getInstance();
        productCategoryDao.empty();
    }

    @AfterEach
    public void clearExampleData() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        for (int i=0; i<productCategoryDataStore.getAll().size(); i++) {
            productCategoryDataStore.remove(i);
        }
    }

    @Test
    public void testSameCategoryNameCannotAdd() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategory tablet1 = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet1);
        ProductCategory tablet2 = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet2);
        int size = productCategoryDataStore.getAll().size();
        assertEquals(1,size);
    }

    @Test
    public void testIsProductCategoryDaoMemIsSingletone () {
        ProductCategoryDao productCategoryDataStore1 = ProductCategoryDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore2 = ProductCategoryDaoMem.getInstance();
        assertEquals(productCategoryDataStore1.hashCode(),productCategoryDataStore2.hashCode());
    }

    @Test
    public void testFindByIdIfNonExistingId() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        assertEquals(null,productCategoryDataStore.find(-4));
    }

    @Test
    public void testFindByIfIdZero() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        assertEquals(null,productCategoryDataStore.find(0));
    }

    @Test
    public void testRemoveById() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet);
        productCategoryDataStore.remove(1);
        assertEquals(null,productCategoryDataStore.find(1));
    }

    @Test
    public void testRemoveByNonExistingId() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer.");
        productCategoryDataStore.add(tablet);
        assertFalse(productCategoryDataStore.remove(0));

    }

    @Test
    public void testGetAllProductCategory() {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet.");
        productCategoryDataStore.add(tablet);
        ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "Is a small, portable personal computer.");
        productCategoryDataStore.add(laptop);
        ProductCategory videoCard = new ProductCategory("Video Card", "Hardware", "A video card.");
        productCategoryDataStore.add(videoCard);
        assertEquals(3,productCategoryDataStore.getAll().size());
    }


}
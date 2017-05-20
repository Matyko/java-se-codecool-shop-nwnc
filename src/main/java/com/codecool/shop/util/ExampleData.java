package com.codecool.shop.util;


import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.database.*;
import com.codecool.shop.dao.implementation.memory.UserDaoMem;
import com.codecool.shop.model.*;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExampleData {

    public static void populateData() {
        DatabaseMetaData dbm = null;
        try {
            dbm = DbConnect.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "product", null);
            if (!tables.next()) {
                fillDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    private static void fillDatabase() {
        try {
            PreparedStatement stmt = DbConnect.getConnection().prepareStatement(
                    ("CREATE TABLE supplier\n" +
                            "(\n" +
                            "  id SERIAL PRIMARY KEY ,\n" +
                            "  name VARCHAR(40),\n" +
                            "  description TEXT\n" +
                            ");\n" +
                            "\n" +
                            "CREATE TABLE productCategory\n" +
                            "(\n" +
                            "  id SERIAL PRIMARY KEY,\n" +
                            "  name VARCHAR(40),\n" +
                            "  department VARCHAR(40),\n" +
                            "  description TEXT\n" +
                            ");\n" +
                            "\n" +
                            "CREATE TABLE product\n" +
                            "(\n" +
                            "  id SERIAL PRIMARY KEY,\n" +
                            "  name VARCHAR(100),\n" +
                            "  description TEXT,\n" +
                            "  defaultPrice FLOAT ,\n" +
                            "  currencyString VARCHAR(100),\n" +
                            "  productCategory INTEGER REFERENCES productCategory(id),\n" +
                            "  supplier INTEGER REFERENCES supplier(id)\n" +
                            ");"));
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        fillDatabaseWithExampleData();
    }

        private static void fillDatabaseWithExampleData() {
//            createExampleUsers();
            fillDbWithSuppliers();
            fillDbWithProductCategory();
            fillDbWithProducts();
            fillDbWithUsers();
        }

        private static void createExampleUsers() {
            UserDao userDataStore = UserDaoMem.getInstance();
            Customer c1 = new Customer("Bruce", "Wayne", "batman@robin.com", "06901111", new Address("USA", "Gotham", "1111", "BatCave"), new Address("USA", "Gotham", "1111", "BatCave"));
            User us1 = new User("batman", "robin");
            User us2 = new User("admin", "admin");
            us2.setAdmin();
            us1.setCustomer(c1);
            userDataStore.add(us1);
            userDataStore.add(us2);
        }

    private static void fillDbWithSuppliers() {
        SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
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
    }

        public static void fillDbWithUsers(){


            CustomerDao CustomerDataStore = CustomerDaoJDBC.getInstance();
            UserDao userJDBC= UserDaoJDBC.getInstance();

            Customer c1 = new Customer("Bruce", "Wayne", "batman@robin.com", "06901111", new Address("USA", "Gotham", "1111", "BatCave"), new Address("USA", "Gotham", "1111", "BatCave"));
            c1.setId(CustomerDataStore.add(c1));

            User us1 = new User("batman", "robin");
            User us2 = new User("admin", "admin");
            us2.setAdmin();
            us1.setCustomer(c1);


            userJDBC.add(us1);
            //userJDBC.add(us2);
           // userJDBC.add(us2);


        }


        private static void fillDbWithProductCategory() {
            ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
            ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
            productCategoryDataStore.add(tablet);
            ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "Is a small, portable personal computer with a \"clamshell\" form factor.");
            productCategoryDataStore.add(laptop);
            ProductCategory videoCard = new ProductCategory("Video Card", "Hardware", "A video card (also called a display card, graphics card) is an expansion card which generates a feed of output images to a display ");
            productCategoryDataStore.add(videoCard);
        }

    private static void fillDbWithProducts() {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", productCategoryDataStore.find(1), supplierDataStore.find(2)));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", productCategoryDataStore.find(1), supplierDataStore.find(2)));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", productCategoryDataStore.find(1), supplierDataStore.find(1)));
        productDataStore.add(new Product("Samsung Chromebook 3 ", 178, "USD", "Unleash the power of modern computing with this Samsung Chromebook laptop.", productCategoryDataStore.find(2), supplierDataStore.find(3)));
        productDataStore.add(new Product("Samsung Notebook 9 pro", 1299, "USD", "You can watch movies and TV shows in rich detail on the 15.6 touch screen, which displays a 4K UHD picture.", productCategoryDataStore.find(2), supplierDataStore.find(3)));
        productDataStore.add(new Product("Gigabyte Geforce GTX 1070 G1", 389, "USD", "GIGABYTE G1 Gaming Series graphics cards are crafted for perfection in pursuit of the ultimate graphics experience for gaming enthusiasts.", productCategoryDataStore.find(3), supplierDataStore.find(4)));
        productDataStore.add(new Product("MSI Radeon RX 470 Gaming", 219, "USD", "The 4th generation GCN architecture is engineered for gamers who play anything from the latest MOBA’s to the most popular AAA titles.", productCategoryDataStore.find(3), supplierDataStore.find(5)));
        productDataStore.add(new Product("Asus ZenPad 8", 149, "USD", "The design of ASUS ZenPad 8.0 carries modern influences and a simple, clean look that gives it an universal and stylish appeal.", productCategoryDataStore.find(1), supplierDataStore.find(6)));
        productDataStore.add(new Product("Asus ZenBook UX530UX", 749, "USD", "Asus - ZenBook Flip UX360CA 2-in-1 13.3\" Touch-Screen Laptop - Intel Core m3 - 8GB Memory - 512GB Solid State Drive - Mineral gray", productCategoryDataStore.find(2), supplierDataStore.find(6)));
        productDataStore.add(new Product("Asus Dual GeForce GTX 580", 1499, "USD", "The MARS II is the first dual GeForce GTX 580 card, and is part of ASUS Republic of Gamers (ROG) brand of premium products targeting the gamer-overclocker market. ", productCategoryDataStore.find(3), supplierDataStore.find(6)));

    }
}

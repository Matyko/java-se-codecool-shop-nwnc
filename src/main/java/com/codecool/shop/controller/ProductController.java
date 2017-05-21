package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.database.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.database.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.database.ShoppingCartDaoJDBC;
import com.codecool.shop.dao.implementation.database.SupplierDaoJDBC;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.ProductFilter;
import spark.Request;

import static com.codecool.shop.model.CurrentUser.*;


import spark.Response;
import spark.ModelAndView;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static com.codecool.shop.util.RequestUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    public static Route renderAllProducts = (Request req, Response res) -> {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        ProductFilter filter = new ProductFilter();
        Map params = new HashMap<>();

        if (getSessionShoppingCart(req) == null) {
            setSessionShoppingcart(req, new ShoppingCart());
        }

        if (req.queryString() != null && req.queryString().length() != 0) {

            String[] categoryNameList = req.queryMap().toMap().get("category");
            String[] supplierNameList = req.queryMap().toMap().get("supplier");

            filter.init(categoryNameList, supplierNameList);

            List<ProductCategory> productCategoryList = getRequestedCategories(categoryNameList);
            List<Supplier> productSupplierList = getRequestedSuppliers(supplierNameList);

            params.put("products", filterBySupplier(productSupplierList));
            params.put("category", productCategoryList);
            params.put("filter", filter);

            logger.debug("All products rendered.");
            return new ThymeleafTemplateEngine().render(new ModelAndView(params, "product/index"));
        }

        filter.init();
        params.put("category", productCategoryDataStore.getAll());
        params.put("products", productDataStore.getAll());
        params.put("filter", filter);
        logger.debug("All products rendered.");
        return new ThymeleafTemplateEngine().render(new ModelAndView(params, "product/index"));
    };


    public static Route addToCart = (Request req, Response res) -> {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();

        if (isUserLoggedIn(req)) {
            int id = Integer.parseInt(req.queryParams("id"));
            ShoppingCartDao shoppingCartDao = ShoppingCartDaoJDBC.getInstance();

            ShoppingCart cart = currentUser(req).getCostumer().getShoppingCart();
            Product prod = productDataStore.find(id);

            shoppingCartDao.addNewCartElement(cart, prod);
            logger.debug("Product added to "+ currentUser(req).getUsername() +" users cart: " + prod.toString());
            return true;
        } else {
            int id = Integer.parseInt(req.queryParams("id"));
            ShoppingCart cart = req.session().attribute("cart");
            cart.add(productDataStore.find(id));
            req.session().attribute("cart", cart);
            logger.debug("Product added to anonymus client's cart: " + productDataStore.find(id).toString());
            return true;
        }
    };

    public static Route categoryListSize = (Request req, Response res) -> {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        return productCategoryDataStore.getAll().size();
    };

    public static Route removeFromCart = (Request req, Response res) -> {
        int id = Integer.parseInt(req.queryParams("id"));

        if (isUserLoggedIn(req)) {
            ProductDao productDao = ProductDaoJDBC.getInstance();

            ShoppingCartDao shoppingCartDao = ShoppingCartDaoJDBC.getInstance();
            shoppingCartDao.removeElementFromCart(productDao.find(id), currentUser(req).getCostumer().getShoppingCart());
            logger.debug("Product removed from "+ currentUser(req).getUsername() +" users cart: " + productDao.find(id).toString());
            return true;
        } else {
            ShoppingCart cart = getSessionShoppingCart(req);
            cart.decrease(id);
            setSessionShoppingcart(req, cart);
            logger.debug("Product removed from anonymus client's cart: " + cart.find(id).toString());
            return true;
        }
    };

    public static Route deleteFromCart = (Request req, Response res) -> {
        int id = Integer.parseInt(req.queryParams("id"));
        if (isUserLoggedIn(req)) {
            ShoppingCartDao shoppingCartDao = ShoppingCartDaoJDBC.getInstance();
            ProductDao productDao = ProductDaoJDBC.getInstance();
            shoppingCartDao.deleteElementsFromCart(productDao.find(id), currentUser(req).getCostumer().getShoppingCart());
            logger.debug("Product deleted from "+ currentUser(req).getUsername() +" users cart: " + productDao.find(id).toString());
            return true;
        } else {
            ShoppingCart cart = getSessionShoppingCart(req);
            cart.remove(id);
            setSessionShoppingcart(req, cart);
            logger.debug("Product deleted from anonymus client's cart: " + cart.find(id).toString());
            return true;
        }
    };

    public static Route renderCart = (Request req, Response res) -> {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        Map params = new HashMap<>();

        if (isUserLoggedIn(req)) {
            ShoppingCart cart = currentUser(req).getCostumer().getShoppingCart();
            params.put("products", productDataStore.getAll());
            params.put("shoppingcart", cart);
            return new ThymeleafTemplateEngine().render(new ModelAndView(params, "product/shoppingcart"));

        }

        ShoppingCart cart = getSessionShoppingCart(req);
        params.put("products", productDataStore.getAll());
        params.put("shoppingcart", cart);
        return new ThymeleafTemplateEngine().render(new ModelAndView(params, "product/shoppingcart"));
    };


    private static List<ProductCategory> getRequestedCategories(String[] categoryNames) {
        if (categoryNames == null) {
            ProductCategoryDao productCategoryDao = ProductCategoryDaoJDBC.getInstance();
            return productCategoryDao.getAll();
        }
        List<ProductCategory> productCategoryList = new ArrayList<>();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        List<ProductCategory> allProductCategories = productCategoryDataStore.getAll();

        for (ProductCategory category : allProductCategories) {
            for (String categoryName : categoryNames) {
                if (category.getName().equals(categoryName)) {
                    productCategoryList.add(category);
                }
            }
        }
        logger.debug("Returning filtered product list: " + productCategoryList.toString());
        return productCategoryList;
    }

    private static List<Supplier> getRequestedSuppliers(String[] supplierNames) {

        if (supplierNames == null) {
            SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
            return supplierDao.getAll();
        }

        List<Supplier> supplierList = new ArrayList<>();
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        List<Supplier> allSuppliers = supplierDao.getAll();

        for (Supplier supplier : allSuppliers) {
            for (String supplierName : supplierNames) {
                if (supplier.getName().equals(supplierName)) {
                    supplierList.add(supplier);
                }
            }
        }
        return supplierList;
    }

    private static Set filterBySupplier(List<Supplier> suppliers) {
        if (suppliers == null) {
            return null;
        }
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        Set<Product> filteredProductList = new HashSet<>();
        for (Supplier supplier : suppliers) {
            for (Product product : productDaoJDBC.getAll()) {
                if (supplier.getName().equals(product.getSupplier().getName())) {
                    filteredProductList.add(product);
                }
            }
        }
        logger.debug("Returning filtered product list: " + filteredProductList.toString());
        return filteredProductList;
    }

    public static Route setAmount = (Request req, Response res) -> {
        int id = Integer.parseInt(req.queryParams("id"));
        int num = Integer.parseInt(req.queryParams("num"));
        if (num < 1) {
            return null;
        }

        if (isUserLoggedIn(req)) {
            ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
            ShoppingCart cart = currentUser(req).getCostumer().getShoppingCart();
            shoppingCartDataStore.setElementCount(cart, id, num);
            return null;
        } else {
            ShoppingCart cart = getSessionShoppingCart(req);
            cart.getAll().put(cart.find(id), num);
            setSessionShoppingcart(req, cart);
            return null;
        }
    };

    public static Route shoppingCartSize = (Request req, Response res) -> {
        if (isUserLoggedIn(req)) {
            return currentUser(req).getCostumer().getShoppingCart().getAllProducts();
        }
        return getSessionShoppingCart(req).getAllProducts();
    };
}
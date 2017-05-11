package com.codecool.shop.controller;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Address;
import com.codecool.shop.model.Customer;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import static com.codecool.shop.model.CurrentUser.currentUser;

public class CustomerController {

    public static void createCustomer(Request req) {
        String checkb = req.queryParams("checkb");
        CustomerDao customerDao = CustomerDaoMem.getInstance();
        customerDao.remove(1);
        System.out.println(checkb);
        if (checkb!=null) {
            Address billingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            Address shippingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            customerDao.add(new Customer(req.queryParams("first_name"), req.queryParams("last_name"), req.queryParams("email"), req.queryParams("phone"),
                    billingAddress, shippingAddress));
        } else {
            Address billingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            Address shippingAddress = new Address(req.queryParams("shippingCountry"), req.queryParams("shippingCity"),
                    req.queryParams("shippingZipcode"), req.queryParams("shippingAddress"));
            customerDao.add(new Customer(req.queryParams("first_name"), req.queryParams("last_name"), req.queryParams("email"), req.queryParams("phone"),
                    billingAddress, shippingAddress));
        }
    }

    public static void registerCustomer(Request req) {
        CustomerDao customerDao = CustomerDaoJDBC.getInstance();
        UserDao userDao = UserDaoJDBC.getInstance();
        String checkb = req.queryParams("checkb");
        if (checkb!=null) {
            Address billingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            Address shippingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            Customer c1 = new Customer(req.queryParams("first_name"), req.queryParams("last_name"), req.queryParams("email"), req.queryParams("phone"),
                    billingAddress, shippingAddress);
            c1.setId(customerDao.add(c1));
            User us1 = new User(req.queryParams("username"), req.queryParams("password"));
            us1.setCustomer(c1);
            userDao.add(us1);
        } else {
            Address billingAddress = new Address(req.queryParams("billingCountry"), req.queryParams("billingCity"),
                    req.queryParams("billingZipcode"), req.queryParams("billingAddress"));
            Address shippingAddress = new Address(req.queryParams("shippingCountry"), req.queryParams("shippingCity"),
                    req.queryParams("shippingZipcode"), req.queryParams("shippingAddress"));
            Customer c1 = new Customer(req.queryParams("first_name"), req.queryParams("last_name"), req.queryParams("email"), req.queryParams("phone"),
                    billingAddress, shippingAddress);
            c1.setId(customerDao.add(c1));
            User us1 = new User(req.queryParams("username"), req.queryParams("password"));
            us1.setCustomer(c1);
              userDao.add(us1);
        }

    }

    public static Route registerUser = (Request req, Response res) -> {
        registerCustomer(req);
            res.redirect("/index");
           return null;
    };

    public static Route redirectCustomer = (Request req, Response res) -> {
        User current=currentUser(req);
        if (current!=null){
            ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoMem.getInstance();
            Map<String, Object> model=new HashMap<>();
            ShoppingCart cart=req.session().attribute("cart");
            model.put("customer",currentUser(req).getCostumer());
            model.put("shoppingcart", cart);
            return new ThymeleafTemplateEngine().render(new ModelAndView(model,"product/payment"));
        }

        CustomerDao customerDao = CustomerDaoMem.getInstance();

        createCustomer(req);
        ShoppingCart cart=req.session().attribute("cart");
        Map params = new HashMap<>();
        params.put("customer", customerDao.find(1));
        params.put("shoppingcart", cart);
        return new ThymeleafTemplateEngine().render(new ModelAndView(params,"product/payment"));
    };
}

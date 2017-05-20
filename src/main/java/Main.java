import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;
import com.codecool.shop.controller.CustomerController;
import com.codecool.shop.controller.ProductController;
import com.codecool.shop.login.LoginHandler;
import com.codecool.shop.util.DbConnect;
import com.codecool.shop.util.ExampleData;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sun.rmi.runtime.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        String CONNECTIONCONFIGFILE="src/main/resources/connection/properties/connectionProperties.txt";
        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);
        DbConnect db=new DbConnect(CONNECTIONCONFIGFILE);

        // populate some data for the memory storage
        ExampleData.populateData();
        logger.info("Example data created for memory usage.");

        get("/", ProductController.renderAllProducts);
        get("/index", ProductController.renderAllProducts);

        post("/login", LoginHandler.manageLogin);
        get("/admin",LoginHandler.adminPage);
        post("/isuserlogged", LoginHandler.isUserLogged);
        get("/logout", LoginHandler.logout);

        get("/shoppingcart", ProductController.renderCart);
        post("/checkout", CustomerController.redirectCustomer);
        get("/checkout", CustomerController.redirectCustomer);

        post("/getCategoryListSize", ProductController.categoryListSize);

        post("/set-amount",ProductController.setAmount);

        post("/add-to-cart", ProductController.addToCart);
        post("/remove-from-cart", ProductController.removeFromCart);
        post("/delete-from-cart", ProductController.deleteFromCart);
        post("/get-shoppingcart-size",ProductController.shoppingCartSize);
        post("/register", CustomerController.registerUser);

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }
}

package com.revature.eMarket.services;

import com.revature.eMarket.daos.*;
import com.revature.eMarket.screens.*;
import com.revature.eMarket.utils.Session;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Scanner;

//@AllArgsConstructor
//@NoArgsConstructor
public class RouterService {
    private Session session;
    private ProductService prodServ;
    private CategoryService catServ;

    private CartService cartService;
//    private CartService cartService;

    public RouterService() {
        this.session = new Session();
        this.prodServ = new ProductService(new ProductDAO(), new ReviewDAO());
        this.catServ = new CategoryService(new CategoryDAO());
        this.cartService = new CartService(new CartDAO(), new CartItemService(new CartItemDAO(), getProdService()), getUserService());
    }

    public void navigate(String path, Scanner scan) {

        switch (path) {
            case "/landing":
                new SignOnScreen(this, this.session).start(scan);
                break;
            case "/home":
                new HomeScreen(this, this.session).start(scan);
                break;
            case "/login":
                new LogInScreen(this,getUserService(), session).start(scan);
                break;
            case "/register":
                new RegisterScreen(getUserService(), this, this.cartService, this.session).start(scan);
                break;
            case "/product":
                new ProductScreen(getProdService(), this, catServ, this.session).start(scan);
                break;
            case "/cart":
                new CartScreen(this, getCartService(), this.session).start(scan);
                break;
            case "/menu":
                new MenuScreen(getCartService(), this, this.session).start(scan);
                break;
            default:
                break;
        }
    }




    /*************************************** Helper Method ************************************/

    private String returnUserSession() {
        return this.session.getId();
    }

    private UserService getUserService() {
        return new UserService(new UserDAO(),getRoleService());
    }

    private RoleService getRoleService() {

        return new RoleService(new RoleDAO());
    }

    private ProductService getProdService() { return new ProductService(new ProductDAO(), new ReviewDAO()); };


    private CartService getCartService() {
        return new CartService(new CartDAO(), getCartItemService(), getUserService());
    }
    private CartItemService getCartItemService() {

        return new CartItemService(new CartItemDAO(), getProdService());
    }
}
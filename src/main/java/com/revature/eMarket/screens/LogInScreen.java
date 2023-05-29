package com.revature.eMarket.screens;

import com.revature.eMarket.models.Cart;
import com.revature.eMarket.models.User;
import com.revature.eMarket.services.CartService;
import com.revature.eMarket.services.RouterService;
import com.revature.eMarket.services.UserService;
import com.revature.eMarket.utils.Session;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class LogInScreen implements IScreen{
    private final RouterService router;
    private final UserService userService;
    private final CartService cartService;
    private Session session;
    private static final Logger logger = LogManager.getLogger(LogInScreen.class);

    @Override
    public void start(Scanner scan) {
        String input = "";
        String username = "";
        String password = "";

        logger.info("Start login process...");

        exit: {
            while(true) {
                logger.info("Welcome to the Login Screen of eMarket!");
                clearScreen();

                System.out.println("Sign in here!");
                System.out.println("[b] Back to main menu");
                System.out.println("[x] Exit");

                // get username
                username = getUsername(scan);
                logger.info("Username: " + username);
//                System.out.println("Username" + username);

                if (username.equals("b")) {
                    logger.info("Returning back to the home screen...");
                    router.navigate("/home", scan);
                    break exit;
                }

                if (username.equals("x")) {
                    logger.info("Leaving the Login Screen...");
                    break exit;
                }


                // get password
                password = getPassword(scan);
                logger.info("Password: " + password );

                if (password.equals("b")) {
                    logger.info("Returning back to the home screen...");
                    router.navigate("/home", scan);
                    break;
                }

                if (password.equals("x")) {
                    logger.info("Leaving the Login Screen...");
                    break exit;
                }

                // confirm user's credentials
                clearScreen();
                System.out.println("Please confirm your credentials");
                System.out.println("\nUsername: " + username);
                System.out.println("Password: " + password);
                System.out.println("\nEnter [y/n]: ");

                switch (scan.nextLine()){
                    case "y":
                        Optional<User> confirmedUser = userService.login(username, password);
                        if(confirmedUser.isEmpty()){
                            clearScreen();
                            System.out.println("Invalid username or password...");
                            System.out.println("\nPress [Enter] to continue...");
                            scan.nextLine();
                            break;
                        }

                        // find the cart
                        //Optional<Cart> cart = cartService.findCartByUserId(confirmedUser.get().getId());
                        // session created
                        session.setSession(confirmedUser);
                        // session successful
                        System.out.println("\nLogin Successful!");
                        System.out.println("\nPress [Enter] to continue...");
                        scan.nextLine();
                        // navigate back to the menu screen
                        router.navigate("/menu", scan);
                        break exit;
                    case "n":
                        clearScreen();
                        System.out.println("Restarting process...");
                        System.out.println("\nPress [Enter] to continue...");
                        scan.nextLine();
                        break;
                    default:
                        clearScreen();
                        System.out.println("Invalid option!");
                        System.out.println("\nPress [Enter] to continue...");
                        scan.nextLine();
                        break;
                }


//                if (!userService.login(username, password)) {
//                    logger.warn("Login Unsuccessful!");
//                    System.out.println("\nNo user found with the combination of this username and password.");
//                    System.out.println("\nPlease try again...");
//                    scan.nextLine();
//                    continue;
//                } else {
//                    System.out.println("\nSuccess!!!");
//                }

//                session.setSession(userService.get());
//                router.navigate("/menu", scan);
                // return back to the home screen after log-in
//                break exit;
            }
        }
    }

    /*************************** Helper methods *****************************************/

    public String getUsername(Scanner scan){
        String username = "";

        while (true){
            System.out.println("\nEnter your username (x to cancel): ");
            username = scan.nextLine();

            if (username.equalsIgnoreCase("x")) {
                return "x";
            }
            if (username.equalsIgnoreCase("b")) {
                return "b";
            }
//        return username.equalsIgnoreCase("x") ? "x" : username.equalsIgnoreCase("b") ? "b" : username;

                if (!userService.isValidUsername(username)) {
                    clearScreen();
                    logger.warn("Login Unsuccessful!");
                    System.out.println("No user found with this username");
                    System.out.println("\nPlease try again...");
                    scan.nextLine();
                    continue;
                }
                break;
        }
        return username;
    }

    public String getPassword(Scanner scan){
        String password = "";

        while (true){
        System.out.println("Enter your password(x to cancel: ");
        password = scan.nextLine();
            if (password.equalsIgnoreCase("x")) {
                return "x";
            }

            if (password.equalsIgnoreCase("b")) {
                return "b";
            }

            if (password.equals("")) {
                clearScreen();
                System.out.println("Password is missing");
                System.out.print("\nPress [Enter] to continue...");
                scan.nextLine();
                continue;
            }

            break;
        }
        return password;
//        return password.equalsIgnoreCase("x") ? "x" : password.equalsIgnoreCase("b") ? "b" : password;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
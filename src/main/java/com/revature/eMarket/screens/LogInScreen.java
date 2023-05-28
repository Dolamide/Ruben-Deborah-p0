package com.revature.eMarket.screens;

import com.revature.eMarket.services.RouterService;
import com.revature.eMarket.services.UserService;
import com.revature.eMarket.utils.Session;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class LogInScreen implements IScreen{
    private final RouterService router;
    private final UserService userService;
    private Session session;

    @Override
    public void start(Scanner scan) {
        String username = "";
        String password = "";

        while(true){
            System.out.println("Sign in here");
            System.out.println("[b] Back to main menu");
            System.out.println("[x] Exit");

            username = getUsername(scan);

            if(username.equals("x")){
                break;
            }

            if(username.equals("b")){
                router.navigate("/home", scan);
                break;
            }

            password = getPassword(scan);

            if(password.equals("x")){
                break;
            }

            if(password.equals("b")){
                router.navigate("/home", scan);
                break;
            }

            if(!userService.login(username, password)){
                System.out.println("\nNo user found with that combination of username and password found");
                System.out.println("\nTry again...");
                continue;
            }else{
                System.out.println("\nSuccess!!!");
            }

            //to-do:
            //go to screen thats available after log-in
            break;

        }
    }

    public String getUsername(Scanner scan){
        String username = "";

        System.out.println("\nEnter your username: ");
        username = scan.nextLine();

        return username.equalsIgnoreCase("x") ? "x" : username.equalsIgnoreCase("b") ? "b" : username;
    }

    public String getPassword(Scanner scan){
        String password = "";

        System.out.println("\nEnter your password: ");
        password = scan.nextLine();

        return password.equalsIgnoreCase("x") ? "x" : password.equalsIgnoreCase("b") ? "b" : password;
    }

}
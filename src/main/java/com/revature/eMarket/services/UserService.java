package com.revature.eMarket.services;

import com.revature.eMarket.daos.UserDAO;
import com.revature.eMarket.models.Role;
import com.revature.eMarket.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

//@AllArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final RoleService roleService;


    // dependency injection
    public UserService (UserDAO userDAO, RoleService roleService) {
        this.userDAO = userDAO;
        this.roleService = roleService;
    }

    // register new user to database
    public User register(String username, String password) {
        System.out.println("UserService > register method ERROR");

        Role foundFound = roleService.findByName("USER");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt()); // one way encryption salt key
        User newUser = new User(username, hashed, foundFound.getId());
        userDAO.save(newUser);
        return newUser;

    }

    // if username is valid
    public boolean isValidUsername(String username) {
        // checks if username 8-20 characters long, no _ or . at end, no _ or . at beginning, a-z, A-Z, 0-9
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    // verify if passwords are the same
    public boolean isSamePassword(String password, String confirmPass) {
        return password.equals(confirmPass);
    }

    // if username exists or not
    public boolean isUniqueUsername(String username) {

        Optional<User> userOpt = userDAO.findByUsername(username);
        if(userOpt.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) {
        Optional<User> user = userDAO.findByUsername(username);
        if(user.isEmpty()){
            return false;
        }
        System.out.println(user.get().getPassword());
        return BCrypt.checkpw(password, user.get().getPassword());

    }
}

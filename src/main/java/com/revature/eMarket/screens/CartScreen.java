package com.revature.eMarket.screens;

import com.revature.eMarket.daos.CartDAO;
import com.revature.eMarket.models.Cart;
import com.revature.eMarket.models.CartItem;
import com.revature.eMarket.services.CartItemService;
import com.revature.eMarket.services.CartService;
import com.revature.eMarket.services.ProductService;
import com.revature.eMarket.services.RouterService;
import com.revature.eMarket.utils.Session;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@AllArgsConstructor
public class CartScreen implements IScreen {
    private final RouterService router;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CartDAO cartDAO;
    private final ProductService prodService;
    private final Session session;
//    getProdService


    @Override
    public void start(Scanner scan) {
        String input = "";
        String itemQuantity = "";
        String itemChosen = "";
        Cart cart = cartService.findCartByCardId(session.getCart_id());
//        CartItems cartItems = cartItemService.findAllCartItemsByCartId(session.getCart_id());
        List<CartItem> cartItem = cartItemService.findAllCartItemsByCartId(cart.getId());

        exit:
        {
            // cart screen emptied
            if (cartItem.isEmpty()) {
                cartIsEmpty(scan);
                break exit;
            }
            // cart screen
            while (true) {
                clearScreen();
                System.out.println("Welcome to the Cart Screen!" + session.getUsername() + "!");
                System.out.println("Press [Enter] to continue...");

                // View Shopping cart
                System.out.println("View shopping cart");
                //ViewCartItems(cartItem);

                // navigate through the options
                System.out.println("\nProduct name: "); // product name added
                System.out.println("Product price: "); // price of the product
                System.out.println("[r] Remove an item" + "[m] Modify an item");
                System.out.println("[c] Checkout");
                System.out.println("[b] Back to the main menu" + "[x] Exit");

                // chose option
                System.out.println("\nChoose option to navigate: ");
                switch (input.toLowerCase()) {
                    case "b":
                        router.navigate("/home", scan);
                        break;
//                    case "m":
//                        System.out.println("\nEnter item to modify");
//                        itemQuantity = scan.nextLine();
                    case "r":
                        while (true) {
                            // get cart item choice
                            itemChosen = getCartItemChosen(cartItem, scan);
                            if (itemChosen.equals("x")) {
                                break;
                            }

                            CartItem cartItems = cartItem.get(Integer.parseInt(itemChosen) - 1);

                            // update cart
                            cart.setTotal_cost(cart.getTotal_cost().subtract(cartItems.getPrice()));
                            cartService.updateCart(cart);

                            // delete cart item
                            cartItemService.deleteCartItem(cartItems.getId());
                            cartItem.remove(cartItems);

                            // empty cart screen
                            if (cartItem.isEmpty()) {
                                cartIsEmpty(scan);
                                // leave cart screen
                                break exit;
                            }

                            // successful removal
                            clearScreen();
                            System.out.println("Removal successful");
                            System.out.print("\nPress enter to continue...");
                            scan.nextLine();
                            break;
                        }
                        break;
                    case "m":
                        while (true) {
                            // get cart item chosen
                            itemChosen = getCartItemChosen(cartItem, scan);
                            if (itemChosen.equals("x")) {
                                break;
                            }

                            CartItem cartItems = cartItem.get(Integer.parseInt(itemChosen) - 1);

                            // get new quantity
                            itemQuantity = getQuantity(cartItems.getQuantity(), cartItems.getStock(), scan);
                            if (itemQuantity.equals("x")) {
                                continue;
                            }

                            if (Integer.parseInt(itemQuantity) == 0) {
                                // update cart
                                //cart.setTotalCost(BigDecimal.valueOf(0));
                                cart.setTotal_cost(cart.getTotal_cost().subtract(cartItems.getPrice()));
                                cartService.updateCart(cart);

                                // delete cart item
                                cartItemService.deleteCartItem(cartItems.getId());
                                cartItem.remove(cartItems);

                                // success removal
                                clearScreen();
                                System.out.println("Removal successful");
                                System.out.print("\nPress enter to continue...");
                                scan.nextLine();

                                // show empty cart screen
                                if (cartItem.isEmpty()) {
                                    cartIsEmpty(scan);
                                    // leave cart screen
                                    break;
                                }
                                break;
                            }
                            // update cart and cart item
                            updateCartAndCartItem(cart, cartItems, Integer.parseInt(itemQuantity));

                            clearScreen();
                            System.out.println("Update successful");
                            System.out.print("\nPress enter to continue...");
                            scan.nextLine();
                            break;
                            }
                        default:
                            break;
                    }
                    break;
                }
            }
        }



    /***************************** Helper Methods *****************************/

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private void cartIsEmpty(Scanner scan) {
        // if cart is not found, navigate back to menu
        exit:
        {
            while (true) {
                clearScreen();
                System.out.println("Welcome to the Cart Screen!" + session.getUsername() + "!");
                System.out.println("Press [Enter] to continue...");
                System.out.println("\nThere is nothing in your cart....");
                System.out.println("\n[1] Continue shopping");
                System.out.println("Press [x] to return");

                System.out.println("\nChoose an option: ");
                switch (scan.nextLine()) {
                    case "1":
                        //navigate to product screen
                        router.navigate("/home", scan);
                        break exit;
                    case "x":
                        break exit;
                    default:
                        break;
                }

            }
        }
    }

    public void viewCartItems(List<CartItem> cartItem) {
        // looping through the cart items
        for(CartItem cartItems : cartItem){
            System.out.println("\n" +
                    cartItems.getName() +
                    " - Price: $" +
                    cartItems.getPrice() +
                    "Quantity: " +
                    cartItems.getQuantity());
        }
    }
    private void viewCartItemsChosen(List<CartItem> cartItems) {
        int counter = 1;
        for(CartItem cartItem : cartItems){
            System.out.println("\n[" + counter +"]" +
                    cartItem.getName() +
                    " - Price: $" +
                    cartItem.getPrice() +
                    " Quantity: " + cartItem.getQuantity());
            counter += 1;
        }
    }
    private String getCartItemChosen(List<CartItem> cartItems, Scanner scan) {
        String input = "";
        while (true) {
            clearScreen();
            System.out.println("Choosing cart item...");

            // show cart item options
            viewCartItemsChosen(cartItems);

            System.out.print("\nChoose an option (x to cancel): ");

            input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                return "x";
            } else if (!isValidNumber(input) || Integer.parseInt(input) > cartItems.size() ||
                    Integer.parseInt(input) < 1) {
                clearScreen();
                System.out.println("Input is invalid: must be a number between 1 and " + cartItems.size());
                System.out.print("\nEnter to continue...");
                scan.nextLine();
                continue;
            }

            return input;
        }
    }


    private String getQuantity(int amount, int stock, Scanner scan) {
        String input = "";
        while (true) {
            clearScreen();
            System.out.println("Changing quantity...");
            System.out.println("\n- Current stock   : " + stock);
            System.out.println("- Current quantity: " + amount);
            System.out.print("\nEnter new quantity (x to cancel): ");

            input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                return "x";
            }
            // validate
            if (!isValidNumber(input) || Integer.parseInt(input) > stock || Integer.parseInt(input) < 0) {
                clearScreen();
                System.out.println("Input is invalid: must be a number between 0 and " + stock);
                System.out.print("\nEnter to continue...");
                scan.nextLine();
                continue;
            }
            break;
        }
        return input;
    }

    private void updateCartAndCartItem(Cart cart, CartItem cartItem, int i) {
        /*BigDecimal newPrice = cartItem.getPrice()
                .divide(BigDecimal.valueOf(cartItem.getQuantity()))
                .multiply(BigDecimal.valueOf(quantity));

        // update cart
        cart.setTotal_cost(cart.getTotal_cost().add(newPrice.subtract(cartItem.getPrice())));
        cartService.updateCart(cart);

        // update cart item
        cartItem.setPrice(newPrice);
        cartItem.setQuantity(quantity);
        cartItemService.updateCartItem(cartItem);*/
    }

    private boolean isValidNumber(String possibleNum) {
        if (possibleNum.length() == 0 || !Pattern.matches("[0-9]+", possibleNum)) {
            return false;
        }
        return true;
    }

}

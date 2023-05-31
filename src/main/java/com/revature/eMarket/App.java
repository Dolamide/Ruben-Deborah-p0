package com.revature.eMarket;

import com.revature.eMarket.services.RouterService;
import com.revature.eMarket.utils.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
  private static final Logger logger = LogManager.getLogger(App.class);
  /**
   * The entry point of the eMarket Application.
   *
   * @param args command line arguments
   * @throws ClassNotFoundException if the specified class cannot be found
   * @throws IOException            if an I/O error occurs
   * @throws SQLException           if a database error occurs
   */
  public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {
    //System.out.println(ConnectionFactory.getInstance().getConnection()); // Use this to check db connection
    logger.info("------------------- START APPLICATION ------------------");

    Scanner scan = new Scanner(System.in);
    RouterService router = new RouterService();
    // navigate to the "/home" route using the router and scanner

    router.navigate("/landing", scan);

    logger.info("------------------- END APPLICATION --------------------");

    scan.close();
  }
}

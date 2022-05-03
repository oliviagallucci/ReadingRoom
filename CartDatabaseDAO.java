package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.Book;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.HelperSQL;

import org.springframework.stereotype.Component;

// file patha: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/persistence/CartDatabaseDAO.java

/**
 * Implements the functionality for JSON file-based peristance for Books
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 * 
 * @author Team 2- M. Elijah W.
 */
@Component
public class CartDatabaseDAO implements CartDAO {
    private Connection connector = null;
    // INSERT INTO shoppingcart VALUES(1000001, "2-089-77869-5");
    // private String URL = "jdbc:mysql://smt5541pi.student.rit.edu:3306/book?user=estore&password=sP3YulgAVf8en6H4";
    private String URL = "jdbc:mysql://smt5541pi.student.rit.edu:3306/book?"
    + "user=estore&password=sP3YulgAVf8en6H4";

    private boolean makeConnection() {
        try {
            this.connector = HelperSQL.connect(URL);
            return true; // connection succeeded
        }
        catch (Exception e) {
            System.err.println("SQL CONNECTION FAILED: " + e);
            return false; // connection failed
        }
    }

    @Override
    public boolean updateDatabase(Cart local) {
        if (this.connector == null) {makeConnection();}
        String query = String.format("DELETE FROM shoppingcart WHERE UserID = %d;", HelperSQL.getUserID(local.getShopper(), this.connector));
        if (local.isEmpty()) { // if local cart is empty, we will simply wipe the database of any shoppingcart items attached to user...
            return HelperSQL.updateDatabase(query, this.connector);
        }
        else {
            HelperSQL.updateDatabase(query, this.connector);
            for (Book item : local.getItems()) { // 
                addItemDatabase(HelperSQL.getUserID(local.getShopper(), this.connector), item.getISBN());
            }
            return true;
        }
    }

    @Override
    public Cart updateLocal(Account acc) {
        if (this.connector == null) {makeConnection();}
        int UserID = HelperSQL.getUserID(acc, this.connector);
        String [] arrISBN;
        // request all shopping cart items from given account (userid) into list, then build
        // an array of books out out of it to put into newCart
        arrISBN = HelperSQL.getIDFromCart(UserID, this.connector);
        ArrayList<Book> items = new ArrayList<>();
        for (String ISBN : arrISBN) {
            items.add(HelperSQL.getBook(ISBN, this.connector));
        }
        Cart newCart = new Cart(acc, items);
        return newCart;
    }

    @Override
    public boolean addItemDatabase(int UserID, String ISBN) {
        if (this.connector == null) {makeConnection();}
        String query = String.format("INSERT INTO shoppingcart VALUES(%d, '%s');", UserID, ISBN);
        return HelperSQL.updateDatabase(query, this.connector);
    }

    // DELETE FROM shoppingcart WHERE User_ID=1000001 AND ISBN='2-089-77869-5'; 
    @Override
    public boolean removeItemDatabase(int UserID, String ISBN) {
        if (this.connector == null) {makeConnection();}
        String query = String.format("DELETE FROM shoppingcart WHERE UserID = %d AND ISBN = '%s';", UserID, ISBN);
        return HelperSQL.updateDatabase(query, this.connector);
    }

    // rank is hardcoded, need to fix that...
    @Override
    public boolean checkout(Cart local) {
        try {
            for (Book item : local.getItems()) { 
                HelperSQL.buyBook(0, 1, item.getISBN(), local.getShopper().getUsername(), this.connector);
            }
            local.dump();
            updateDatabase(local);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}

package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/model/Cart.java

/**
 * Represents a Shopping Cart entity
 * 
 * @author Team 2- M. Elijah W.
 */
public class Cart {
    @JsonProperty("User") Account shopper;
    @JsonProperty("CartItems") ArrayList<Book> items;


    public Cart(@JsonProperty("User") Account shopper,
                @JsonProperty("CartItems") ArrayList<Book> items) {
        this.shopper = shopper;
        this.items = items;
    }


    public boolean isEmpty() {
        if (items.isEmpty()) {
            return true;
        }
        return false;
    }

    public Account getShopper() {
        return this.shopper;
    }

    public List<Book> getItems() {
        return this.items;

    }



    public int addToCart(Book book) {
        boolean check = false;
        for (Book item : items) {
            if (item.getISBN() == book.getISBN()) {
                check = true;
            }
        }
        if (!check) {
            this.items.add(book);
            return 1; // WORKED CORRECTLY
        }
        else {
            return 0; // FAILED TO ADD BOOK
        }
    }

    public int remove(String ISBN) {
        for (Book item : items) {
            if (item.getISBN() == ISBN) {
                this.items.remove(item);
                return 1; // WORKED CORRECTLY
            }
        }
        return 0; // FAILED TO REMOVE BOOK
    }

    public int dump() {
        if (!this.items.isEmpty()) {
            this.items.clear();
            return 1; // WORKED CORRECTLY
        }
        return 0; // SHOPPING CART ALREADY EMPTY
    }
}

package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/model/InventoryEntry.java

/**
 * Represents a singular book entry in the inventory table in the database
 * 
 * @author Team2 : Ali Stambayev
 */
public class InventoryEntry {
    // NOT OPTIMIZED - IMPROVEMENT - can be bettered - make fully identical to the
    // entry in the database,
    // add the InventoryID element,

    // Package private for tests
    static final String STRING_FORMAT = "Entry [ISBN=%s, Condition=%d, Count=%d]";
    // Properties from the inventory table in the database
    @JsonProperty("ISBN")
    private String ISBN; // The ISBN of the Book
    @JsonProperty("Condition")
    private int Condition; // The Condition of the singular book
    @JsonProperty("Count")
    private int Count; // The number of singular books that get added

    /**
     * Retrieves the ISBN of the inventory entry
     * 
     * @return The ISBN of the inventory entry
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Retrieves the level of wear of the book in the inventory entry
     * 
     * @return The condition of the book in the inventory entry
     */
    public int getCondition() {
        return Condition;
    }

    /**
     * Retrieves the number of times the book should be added to the inventory
     * 
     * @return The number array for conditions
     */
    public int getCount() {
        return Count;
    }

    /**
     * Returning a String representation of the InventoryEntry object.
     * {@inheritDoc}
     * 
     * @return the String representation of InventoryEntry
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, ISBN, Condition, Count);
    }
}

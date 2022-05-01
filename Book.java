package com.estore.api.estoreapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

// File path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/model/Book.java

/**
 * Represents a Book/product entity
 * 
 * @author Team2 : Ali Stambayev
 */
public class Book {

    // Package private for tests
    static final String STRING_FORMAT = "Book [ISBN=%s, Title=%s, Description=%s, Category=%d, Edition=%s, PublisherID=%d, Condition=%s, Count=%s]";
    // Properties from the book table in the database
    @JsonProperty("ISBN")
    private String ISBN;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("Category")
    private int Category;
    @JsonProperty("Edition")
    private String Edition;
    @JsonProperty("PublisherID")
    private int PublisherID;

    // Properties from the bookcondition table in the database
    @JsonProperty("Condition")
    private String Condition;
    @JsonProperty("Count")
    private int[] Count;

    /**
     * Make a Book Object
     * 
     * @param ISBN
     * @param Title
     * @param Description
     * @param Category    - the suited genre
     * @param Edition
     * @param PublisherID
     * @param Condition   - the level of wear
     * @param Count       - the quantity of the books of specific condition :
     *                    [#ofBads, #ofPoors, #ofAverages, #ofGoods, #ofExcellents,
     *                    #ofNews]
     */
    public Book(@JsonProperty("ISBN") String ISBN,
            @JsonProperty("Title") String Title,
            @JsonProperty("Description") String Description,
            @JsonProperty("Category") int Category,
            @JsonProperty("Edition") String Edition,
            @JsonProperty("PublisherID") int PublisherID,
            @JsonProperty("Condition") String Condition) {
        this.ISBN = ISBN;
        this.Title = Title;
        this.Description = Description;
        this.Category = Category;
        this.Edition = Edition;
        this.PublisherID = PublisherID;
        this.Condition = Condition;
        int[] countInit = { 0, 0, 0, 0, 0, 0 }; // initially, all condition books are 0
        this.Count = countInit;
        conditionSetter(Condition, countInit);
    }

    /**
     * Retrieves the ISBN of the book
     * 
     * @return The ISBN of the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Retrieves the name of the book
     * 
     * @return The name of the book
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Retrieves the description of the book
     * 
     * @return The description of the book
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Retrieves the Category - genre of the book
     * 
     * @return The Category of the book
     */
    public int getCategory() {
        return Category;
    }

    /**
     * Retrieves the edition of the book
     * 
     * @return The edition of the book
     */
    public String getEdition() {
        return Edition;
    }

    /**
     * Retrieves the edition of the book
     * 
     * @return The edition of the book
     */
    public int getPublisherID() {
        return PublisherID;
    }

    /**
     * Retrieves the level of wear of the book
     * 
     * @return The condition of the book
     */
    public String getCondition() {
        return Condition;
    }

    /**
     * Retrieves the quantity of the book available
     * 
     * @return The quantity of the book
     */
    public int[] getCount() {
        return Count;
    }

    /**
     * Set the book quantity to the correct value
     * 
     * @param count the quantity of the book
     */
    public void conditionSetter(String condition, int[] count) {
        int[] countInit = { 0, 0, 0, 0, 0, 0 };
        this.Count = count;
        if (condition.equals("Bad")) {
            this.Count[0] = count[0] + 1;
        } else if (condition.equals("Poor")) {
            this.Count[1] = count[1] + 1;
        } else if (condition.equals("Average")) {
            this.Count[2] = count[2] + 1;
        } else if (condition.equals("Good")) {
            this.Count[3] = count[3] + 1;
        } else if (condition.equals("Excellent")) {
            this.Count[4] = count[4] + 1;
        } else if (condition.equals("New")) {
            this.Count[5] = count[5] + 1;
        } else {
            this.Count = countInit;
        }
    }

    /**
     * Returning a String representation of the Book object.
     * {@inheritDoc}
     * 
     * @return the String representation of Book
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, ISBN, Title, Description, Category, Edition, PublisherID, Condition,
                Arrays.toString(Count));
    }
}

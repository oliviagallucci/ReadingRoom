package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Book;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/persistence/EstoreDatabaseDAO.java

/**
 * Implements the functionality for MySQL Database-based peristance for Books
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 * 
 * @author Ali Stambayev
 */
@Component
public class EstoreDatabaseDAO implements EstoreDAO {
    private Connection connect; // The local DriverManager for the database connection

    /**
     * Creates a Book Databases Access Object
     * 
     * @throws SQLException when the database cannot be accessed or read from
     */
    public EstoreDatabaseDAO() throws IOException {
        try {
            this.connect = DriverManager
                    .getConnection("jdbc:mysql://smt5541pi.student.rit.edu:3306/book?"
                            + "user=estore&password=sP3YulgAVf8en6H4");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper function that works with the database
     * and gets a book array based on the query - FOR GETTERS
     * 
     * @param query - the command that is sent to the database
     * @return book array of the database response
     */
    private Book[] getFromDatabase(String query) {
        // send a command to get the products from the database
        try {
            Statement st = this.connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            // The resultSet contains:
            // ISBN, Title, Description, Category, Edition, PublisherID, Condition
            ArrayList<Book> bookResultArrayList = new ArrayList<Book>();

            while (rs.next()) {
                if (rs.getString(7)==null){
                    break;
                }
                // TODO: ADD A QUERY RESULT CHECK, if the result is error or not
                // Take the row data to create a Book object
                Book book = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                                     rs.getString(5), rs.getInt(6), rs.getString(7));
                // Add the book to the local ArrayList of Books
                bookResultArrayList.add(book);
            }
            // Turn the books ArrayList to an Array
            ArrayList<Book> books = new ArrayList<>();
            Map<String,int[]> bookNums = new HashMap<>();
            for (Book book: bookResultArrayList){
                int[] count = bookNums.get(book.getISBN());
                if (count == null){
                    bookNums.put(book.getISBN(),book.getCount());
                    books.add(book);
                }else{
                    book.conditionSetter(book.getCondition(), count);
                    bookNums.put(book.getISBN(),book.getCount());
                }
            }
            Book[] bookArray = new Book[books.size()];
            bookArray = books.toArray(bookArray);
            return bookArray;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper function for ISNERT,DELETE,UPDATE MySQL commands
     * @param query - the query that gets executed
     * @return - boolean status of the operation:
     *              true if successful;
     *              false if failed.
     */
    private boolean updateDatabase(String query){
        try {
            Statement st = this.connect.createStatement();
            int rs = st.executeUpdate(query);
            System.out.println("updateResult");
            System.out.println(rs);
            // if book not updated - 0, no affected rows.
            if (rs != 0) {
                System.out.println("pass return nothing check");
                // the row count for 1 element is 1 affected row = 1
                if (rs == 1) {
                    System.out.println("pass row count check");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /// -----------------------CUSTOMER METHODS-----------------
    /**
     ** {@inheritDoc}
     */
    @Override
    public Book[] getBooks() {
        // send a command to get the products from the database
        // THE COMMAND
        String query = 
        "SELECT isbn, title, book.description, category, edition, publisherid,"+
        "bookcondition.description, price FROM book JOIN                                                                                                                                                                                                                                                                                                                                                                                                                  inventory USING(ISBN) "+
        "JOIN bookcondition USING(Ranks) order BY isbn, bookcondition.ranks asc";
        return getFromDatabase(query);
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Book[] findBooks(String containsText) {
        System.out.println("findBooks("+containsText+")");
        // THE COMMAND
        String query = "SELECT isbn, title, book.description, category, edition, publisherid, bookcondition.description, price FROM book JOIN inventory USING(ISBN) JOIN bookcondition USING(Ranks) where title like '%"+containsText+"%' order BY isbn, bookcondition.ranks asc;";
        Book[] books = getFromDatabase(query);
        System.out.println(books[0]);
        return books;
    }

    /**
     ** {@inheritDoc}
     */
    @Override 
    public Book getBook(String ISBN) {
        // THE COMMAND
        String query = "SELECT isbn, title, book.description, category, edition, publisherid, bookcondition.description, price FROM book JOIN inventory USING(ISBN) JOIN bookcondition USING(Ranks) where isbn like '%"+ISBN+"%' order BY isbn, bookcondition.ranks asc;";
        Book[] book = getFromDatabase(query);
        return book[0];
    }

    /**
     *  NOT IMPLEMENTED - NEEDS IMPROVEMENT
     ** {@inheritDoc}
     * @throws SQLException
     */
    @Override
    public boolean buyBook(int Purchased,int Rank,String ISBN, String Username) throws SQLException {
        // to get the Inventory ID
        // Example command:
        // select InventoryID from inventory WHERE ISBN="1-111-11111-4" AND Ranks=3 AND Purchased=0;
        String query1 = String.format("select InventoryID from inventory WHERE ISBN='%s' AND Ranks=%d AND Purchased=%d;"
        , ISBN, Rank, Purchased);
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery(query1);
        String InventoryID = rs.next()? rs.getString(1): null;
        if (InventoryID == null){return false;}

        // getting userID
        // "select UserID from user WHERE username='Ali';
        String query2 = String.format("select UserID from user WHERE username='%s';", Username);
        Statement st2 = this.connect.createStatement();
        ResultSet rs2 = st.executeQuery(query2);
        String UserID = rs.next()? rs.getString(1): null;
        if (UserID == null){return false;}

        //# insert a row in the purchase log
        //INSERT INTO purchaselog VALUES(1000008, 1000002);
        String query3 = String.format("INSERT INTO shoppingcart VALUES(%d, '%s');", UserID, ISBN);
        Statement st3 = this.connect.createStatement();
        ResultSet rs3 = st.executeQuery(query3);


        // # change the inventory from available to purchased 
        // UPDATE inventory SET Purchased=1 WHERE InventoryID=1000002;
        String query = String.format("UPDATE inventory SET Purchased=%d, Ranks=%d WHERE InventoryID='%s';", Purchased,Rank,InventoryID);
        return updateDatabase(query);
    }

    // --------------------------OWNER METHODS---------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createBook(Book book) {
        // send the book object stuff into the database
        // THE COMMAND, swap the data values with the book.*getters
        // insert into book values("isbn", "title", "description", 1, "edition", 2);
        String query = String.format("INSERT INTO book VALUES ('%s', '%s', '%s', %d, '%s', %d);", book.getISBN(),
                book.getTitle(), book.getDescription(), book.getCategory(), book.getEdition(), book.getPublisherID());
        System.out.println(query);
        boolean status1 = updateDatabase(query);
        // send the updated book stuff into the database
        // THE COMMAND
        // insert into inventory (ISBN, Ranks) VALUES("3-444-95595-4", 4);
        String query1 = String.format("INSERT INTO inventory (ISBN, Ranks) VALUES('%s', %d);", book.getISBN(), book.getCategory());
        System.out.println(query1);
        boolean status = updateDatabase(query1);
        // if ERROR/conflict returned - unsuccessful
        // If correct - successful.-maybe pop up a message on UI side
        return status && status1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateBookData(Book book) {
        // send the updated book stuff into the database
        // THE COMMAND
        // UPDATE book SET isbn='%s', title='%s', description='%s', category=%d, edition='%s', publisherID=%d where ISBN='%s';
        String query = String.format(
                "UPDATE book SET isbn='%s', title='%s', description='%s', category=%d, edition='%s', publisherID=%d where ISBN='%s';",
                book.getISBN(), book.getTitle(), book.getDescription(), book.getCategory(), book.getEdition(),
                book.getPublisherID(), book.getISBN());
        System.out.println(query);
        return updateDatabase(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addBookInventoryCount(String ISBN, int Rank, int Count) {
        // send the updated book stuff into the database
        // THE COMMAND
        // insert into inventory (ISBN, Ranks) VALUES("3-444-95595-4", 4)
        String query = String.format("INSERT INTO inventory (ISBN, Ranks) VALUES('%s', %d);", ISBN, Rank);
        System.out.println(query);
        boolean status = true;
        for (int i = 0; i < Count; i++) {
            status = status && updateDatabase(query);
        }
        // if ERROR/conflict returned - unsuccessful
        // If correct - successful.-maybe pop up a message on UI side
        return status;
    }

    /**
     * {@inheritDoc}
     * NOT FUNCTIONAL: IMPROVEMENT NEEDED
     */
    @Override
    public boolean updateBookInventoryStatus(int Rank ) {
        // send the updated book stuff into the database
        // THE COMMAND
        //UPDATE inventory SET Purchased=1, Ranks='%d' WHERE InventoryID=1000000;
        String query = String.format(
                "UPDATE inventory SET Purchased=1, Ranks='%d' WHERE InventoryID=1000000;", Rank);
        System.out.println(query);
        return updateDatabase(query);
    }

     /**
     * {@inheritDoc}
     * NOT TESTED
     */
    @Override
    public boolean deleteBookCount(int InventoryID) {
        // send the updated book stuff into the database
        // THE COMMAND
        // DELETE FROM inventory WHERE InventoryID=1000000;
        String query = String.format(
            "DELETE FROM inventory WHERE InventoryID=%d;", InventoryID);
        System.out.println(query);
        return updateDatabase(query);
    }
}

package com.estore.api.estoreapi.persistence;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.Book;

import java.sql.*;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/persistence/HelperSQL.java 

/**
 * 
 * @author M. Elijah Wangeman 
 */
public class HelperSQL {

    public static Connection connect(String url) {
        try {
            return DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            return null; // if failed
        }
    }

    public static int getUserID(Account acc, Connection connect) {
        try {
            String query = String.format("select UserID from user WHERE username='%s';", acc.getUsername());
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            String UserID = rs.next()? rs.getString(1): null;
            if (UserID == null){return -1;}
            return Integer.parseInt(UserID);
        } catch (SQLException e) {
            return -2;
        }
    }

    /**
     * Helper function that works with the database
     * and gets a book array based on the query
     * 
     * @param query - the command that is sent to the database
     * @return book array of the database response
     */
    public static Book[] getBookArrFromDatabase(String query, Connection connect) {
        // send a command to get the products from the database
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            // The resultSet contains:
            // ISBN, Title, Description, Category, Edition, PublisherID, Condition, Count
            ArrayList<Book> bookResultArrayList = new ArrayList<Book>();
            while (rs.next()) {
                // TODO: ADD A QUERY RESULT CHECK, if the thing is error or not
                // Take the row data to create a Book object
                Book book = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                                     rs.getString(5), rs.getInt(6), rs.getString(7));   
                // Add the book to the local ArratList of Books
                bookResultArrayList.add(book);
            }
            // Turn the books ArrayList to an Array
            Book[] books = new Book[bookResultArrayList.size()];
            books = bookResultArrayList.toArray(books);
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getIDFromCart(int UserID, Connection connect) {
        try {
            ArrayList<String> result = new ArrayList<String>();
            String query = String.format("SELECT ISBN FROM shoppingcart WHERE UserID = %d;", UserID);
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getString("ISBN"));
            }

            return result.toArray(new String[result.size()]);
        } catch (SQLException e) {
            return null;
        }
    }

    public static Book getBook(String ISBN, Connection connect) {
        // THE COMMAND
        String query = String.format("SELECT * FROM book;", ISBN);
        Book[] books = getBookArrFromDatabase(query, connect);
        return books[0];
    }

    public static boolean updateDatabase(String query, Connection connect){
        try {
            Statement st = connect.createStatement();
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

    public static boolean buyBook(int Purchased,int Rank,String ISBN, String Username, Connection connect) throws SQLException {
        // to get the Inventory ID
        // select InventoryID from inventory WHERE ISBN="1-111-11111-4" AND Ranks=3 AND Purchased=0;
        String query1 = String.format("select InventoryID from inventory WHERE ISBN='%s' AND Ranks=%d AND Purchased=%d;"
        , ISBN, Rank, Purchased);
        Statement st = connect.createStatement();
        ResultSet rs = st.executeQuery(query1);
        String InventoryID = rs.next()? rs.getString(1): null;
        if (InventoryID == null){return false;}

        // getting userID
        // "select UserID from user WHERE username='Ali';
        String query2 = String.format("select UserID from user WHERE username='%s';", Username);
        Statement st2 = connect.createStatement();
        ResultSet rs2 = st.executeQuery(query1);
        String UserID = rs.next()? rs.getString(1): null;
        if (UserID == null){return false;}

        //# insert a row in the purchase log
        //INSERT INTO purchaselog VALUES(1000008, 1000002);
        String query3 = String.format("INSERT INTO purchaselog VALUES(%d, %d);", UserID, InventoryID);
        Statement st3 = connect.createStatement();
        ResultSet rs3 = st.executeQuery(query1);

        // # change the inventory from available to purchased 
        // UPDATE inventory SET Purchased=1 WHERE InventoryID=1000002;
        String query = String.format("UPDATE inventory SET Purchased=%d, Ranks=%d WHERE InventoryID='%s';", Purchased,Rank,InventoryID);
        return updateDatabase(query, connect);
    }
}

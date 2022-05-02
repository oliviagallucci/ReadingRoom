package com.estore.api.estoreapi.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.estore.api.estoreapi.model.Account;

import org.springframework.stereotype.Component;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/persistence/LoginDatabaseDAO.java 

/**
 * Implements the functionality for Database-based peristance for Login + register features
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 * 
 * @author Ali Stambayev
 */
@Component
public class LoginDatabaseDAO implements LoginDAO {
    private Connection connect;  //The local DriverManager for the database connection
    private String queryResult;  //The result of a select query from the database

    /**
     * Creates an instance of LoginDatabaseDAO 
     * 
     * Used for specificying the functionality of overriden functions
     * checkCredentials() and register() that gets dependency injected 
     * into the RESTController element
     */
    public LoginDatabaseDAO(){
        try {
            this.connect = DriverManager
                        .getConnection("jdbc:mysql://smt5541pi.student.rit.edu:3306/book?"
                                + "user=estore&password=sP3YulgAVf8en6H4");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Attempt to insert a new account into the database and get the response
     * @param acc - Account object from the user that gets added to the database
     * @return - the status of the success of database insertion
     */
    @Override
    public boolean register(Account acc) {
        //send the username account stuff into the database
        //YHE COMMAND, swap the data values with the acc.*getters
        //INSERT INTO user (LastName, FirstName, PhoneNumber, Username, Password) VALUES ("Gallucci", "Olivia", "(123) 456-7890", "oliviagallucci", "password");
        String query = String.format("INSERT INTO user (LastName, FirstName, PhoneNumber, Username, Password)" +
                        " VALUES ('%s', '%s', '%s', '%s', '%s')"
        , acc.getLastname(), acc.getFirstname(),acc.getPhonenumber(),acc.getUsername(),acc.getPassword()); 
        System.out.println(query);
        try {
            Statement st = this.connect.createStatement();
            int rs = st.executeUpdate(query);
            System.out.println("updateResult");
            System.out.println(rs); 
            //if account not created - 0, no affected rows. 
            if (rs != 0){   
                System.out.println("pass return nothing check");
                // the row count for 1 element is 1 affected row = 1
                if(rs == 1){
                System.out.println("pass row count check");
                return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //if ERROR/conflict returned - unsuccessful
        //If correct - successful.-maybe pop up a message on UI side
        return false;
    }


    /**
     * Call the database in here and get the response of log in or not
     * @param acc - Account object from the user where the password is 
     *              checked according to the database
     * @return - status of credential checking
     */
    @Override
    public boolean checkCredentials(Account acc) {

        // send a command to get the user credentials from the database 
        //THE COMMAND
        String query = String.format("select Password from user WHERE UserName='%s'",acc.getUsername()); 
        System.out.println(query);
        try {
            Statement st = this.connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                System.out.println("queryResult");
                // parse the returned result to get the pure password.
                this.queryResult = rs.getString(1);  
                System.out.println(this.queryResult); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //if username not found - returns null OR empty string. 
        //no account with that username - return false
        if (queryResult != null || !queryResult.equals("") ){   
            System.out.println("pass empty check");
        // check the returned password and the acc.getpassword, if same return true, if not fallse
            if(this.queryResult.equals(acc.getPassword())){
                System.out.println("pass password check");
                return true;
            }
        }
        System.out.println("Big last false");
        return false;
    }
}

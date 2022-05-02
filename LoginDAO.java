package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Account;

// path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/persistence/LoginDAO.java 

/**
 * Defines the interface for Login feature/Account object persistence
 * 
 * @author Team2 - Ali Stambayev
 */
public interface LoginDAO {

    /**
     * Take the account information from the UI and 
     * update the database. 
     * @return true if registering happened
     *          false if it didn't
     */
    boolean register(Account acc);

    /**
     * Make a call to the database to check whether
     * the acc logs in correctly
     * @param acc - account that logs in
     * @return true if password and username are correct
     *         false if otherwise.
     */
    boolean checkCredentials(Account acc);
}

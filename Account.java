package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// file path: team-project-2215-swen-261-04-2-the-reading-room/estore-api/src/main/java/com/estore/api/estoreapi/model/Account.java

/**
 * Represents a Book/product entity
 * 
 * @author M. Elijah Wangeman
 * @author Ali Stambayev
 * @since 2022-03-18
 */
public class Account {
    @JsonProperty("LastName")
    private String lastname;
    @JsonProperty("FirstName")
    private String firstname;
    @JsonProperty("PhoneNumber")
    private String phonenumber;
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    @JsonProperty("Role")
    private int role; // user: 1, guest: 2, admin: 0

    /**
     * Creates account entity according to provided authentication
     * 
     * @param lastname:    account user's lastname
     * @param firstname:   account user's firstname
     * @param phonenumber: account user's phone number
     * @param username:    account username
     * @param password:    account password provided as json object
     * @return Account: returns itself as object
     */
    public Account(@JsonProperty("LastName") String lastname,
            @JsonProperty("FirstName") String firstname,
            @JsonProperty("PhoneNumber") String phonenumber,
            @JsonProperty("Username") String username,
            @JsonProperty("Password") String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
        this.username = username;
        this.password = password;
        this.role = (this.username.equals("admin")) ? 0 : 1;
    }

    /**
     * Retrieves the lastname of account object
     * 
     * @return lastname - string
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * Retrieves the firstname of account object
     * 
     * @return firstname - string
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Retrieves the phonenumber of account object
     * 
     * @return phonenumber - string
     */
    public String getPhonenumber() {
        return this.phonenumber;
    }

    /**
     * Retrieves the username of account object
     * 
     * @return username - string
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Retrieves the password of account object
     * 
     * @return password - string
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Retrieves the role (as int) of account object
     * 
     * @return role ID
     */
    public int getRole() {
        return this.role;
    }

    /**
     * Retrieves the role (as int) of account object
     * 
     * @return role ID
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * Returning a String representation of the Account object
     * in the format of "FirstName LastName PhoneNUmber Username Password"
     * 
     * @return the String representation of Account
     */
    @Override
    public String toString() {
        return this.firstname + " " + this.lastname + " " + this.phonenumber + " " + this.username + " "
                + this.password;
    }
}

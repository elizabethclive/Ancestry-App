package DAO;

import java.sql.Connection;

import Model.AuthToken;

public class AuthTokenDAO {
    private Connection connection;

    /**
     * Takes a token from AuthToken and puts it into the database
     * @param token to be created.
     */
    public void createToken(AuthToken token){};

    /**
     * Finds the token in the database with the token id and returns it
     * @param tokenID to identify the token being read.
     * @return token with the ID tokenID
     */
    public AuthToken readToken(String tokenID) {
        return null;
    };

    /**
     * Finds the token in the database with the token id and deletes it
     * @param tokenID to identify the token being deleted.
     */
    public void deleteToken(String tokenID) {};
}

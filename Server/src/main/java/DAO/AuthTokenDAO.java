package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.AuthToken;

public class AuthTokenDAO {
    private Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }


    /**
     * Takes a token from AuthToken and puts it into the database
     * @param authToken to be created.
     */
    public void createToken(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (authToken, userName, personID) VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getToken());
            stmt.setString(2, authToken.getUsername());
            stmt.setString(3, authToken.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    };

    /**
     * Finds the token in the database with the token id and returns it
     * @param tokenID to identify the token being read.
     * @return token with the ID tokenID
     */
    public AuthToken readToken(String tokenID) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authToken"), rs.getString("userName"), rs.getString("personID"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    };

    /**
     * Finds the token in the database with the token id and deletes it
     * @param tokenID to identify the token being deleted.
     */
    public void deleteToken(String tokenID) {
        try {
            String sql = "DELETE FROM AuthToken WHERE authToken = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, tokenID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while deleting from the token database");
            }
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }
}

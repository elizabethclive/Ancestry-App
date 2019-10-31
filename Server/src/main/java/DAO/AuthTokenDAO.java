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
     * @param token to be created.
     */
    public void createToken(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (token, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUsername());

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
        AuthToken token;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new AuthToken(rs.getString("token"), rs.getString("username"), rs.getString("personID"));
                return token;
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
            String sql = "DELETE FROM AuthToken WHERE token = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, tokenID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while deleting from the token database");
            }
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    };

    /**
     * Clears all events in the database
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM AuthToken;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing authToken table");
        }
    }
}

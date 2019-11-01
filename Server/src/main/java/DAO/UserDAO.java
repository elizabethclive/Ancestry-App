package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.User;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Takes a user object and puts it into the database
     * @param user to be put into the database
     */
    public void createUser(User user) throws DataAccessException{
        String sql = "INSERT INTO User (userName, password, email, firstName, " +
                "lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    };

    /**
     * Finds the user in the database with the user id and returns it
     * @param userName to identify the user being read.
     * @return user with the ID userID
     */
    public User readUser(String userName) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
            } else {
                System.out.println("Error?????????"); //HELP
            }
        } catch (SQLException e) {
            System.out.println("error encountered while finding user");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user ");
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
     * Finds the user in the database with the user id and deletes it
     * @param userName to identify the user being deleted.
     */
    public void deleteUser(String userName) {
        try {
            String sql = "DELETE FROM User WHERE userName = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while deleting from the database");
            }

        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }
}
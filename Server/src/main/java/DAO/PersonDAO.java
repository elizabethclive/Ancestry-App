package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Person;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }


    /**
     * Takes a person object and puts it into the database
     * @param person to be put into the database
     */
    public void createPerson(Person person) throws DataAccessException{
        String sql = "INSERT INTO Person (personID, username, firstName, lastName, gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            System.out.println("here1");
            stmt.setString(1, person.getId());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROROROROROROROROR");
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
    };

    /**
     * Finds the person in the database with the person id and returns it
     * @param personID to identify the person being read.
     * @return person with the ID personID
     */
    public Person readPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("id"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
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
     * Finds the person in the database with the person id and deletes it
     * @param personID to identify the person being deleted.
     */
    public void deletePerson(String personID) {
        try {
            String sql = "DELETE FROM Person WHERE id = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, personID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while deleting from the database");
            }

        } catch (DataAccessException e) {
            System.out.println(e);
        }
    };


    /**
     * Clears all people in the database
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Person;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing person table");
        }
    }
}

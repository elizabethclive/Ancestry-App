package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
        String sql = "INSERT INTO Person (firstName, lastName, gender, personID, fatherID, motherID, spouseID, associatedUsername) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getGender());
            stmt.setString(4, person.getId());
            stmt.setString(5, person.getFatherID());
            stmt.setString(6, person.getMotherID());
            stmt.setString(7, person.getSpouseID());
            stmt.setString(8, person.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"), rs.getString("associatedUsername"));
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
            String sql = "DELETE FROM Person WHERE personID = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, personID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while deleting from the database");
            }

        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }


    public void deleteAssociatedPeople(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    public ArrayList<Person> getAssociatedPersons(String associatedUsername) throws DataAccessException {
        Person person;
        ArrayList<Person> persons = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"), rs.getString("associatedUsername"));
                persons.add(person);
            }
            return persons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

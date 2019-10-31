package DAO;

import java.sql.Connection;

import Model.Person;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person person;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        this.person = new Person("asdf", "eclive", "Elizabeth",
                "Clive", null, null, null, "asdf");
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @org.junit.jupiter.api.Test
    void createPersonPass() {
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            compareTest = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(person.getId(), compareTest.getId());
    }

    @org.junit.jupiter.api.Test
    void createPersonFail() {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            pDao.createPerson(person);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
            didItWork = false;
        }
        assertFalse(didItWork);

        Person compareTest = person;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNull(compareTest);
    }

    @org.junit.jupiter.api.Test
    void readPersonPass() {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            compareTest = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNotNull(compareTest);
        assertEquals(compareTest.getId(), person.getId());
    }

    @org.junit.jupiter.api.Test
    void readPersonFail() {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            compareTest = pDao.readPerson("5");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNull(compareTest);
    }

    @org.junit.jupiter.api.Test
    void deletePersonPass() {
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            compareTest = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(person.getId(), compareTest.getId());

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.deletePerson(person.getId());
            compareTest = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNull(compareTest);
    }

    @org.junit.jupiter.api.Test
    void deletePersonFail() {
        boolean didItWork = true;
        final Connection conn1 = null;

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.deletePerson(person.getId());
//            compareTest = pDao.readPerson(person.getId()());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
            didItWork = false;
        }
        assertTrue(didItWork);
    }
}
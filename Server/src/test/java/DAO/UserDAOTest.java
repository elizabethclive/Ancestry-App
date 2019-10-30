package DAO;

import java.sql.Connection;

import Model.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User user;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        user = new User("eclive", "password", "eclive@gmail.com",
                "Elizabeth", "Clive", "F", 1);
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
    void createUserPass() {
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareTest = uDao.readUser(user.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(user.getUsername(), compareTest.getUsername());
    }

    @org.junit.jupiter.api.Test
    void createUserFail() {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            uDao.createUser(user);
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

        User compareTest = user;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareTest = uDao.readUser(user.getUsername());
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
    void readUserPass() {
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareTest = uDao.readUser(user.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNotNull(compareTest);
        assertEquals(compareTest.getUsername(), user.getUsername());
    }

    @org.junit.jupiter.api.Test
    void readUserFail() {
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareTest = uDao.readUser("BAD USERNAME");
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
    void deleteUserPass() {
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareTest = uDao.readUser(user.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(user.getUsername(), compareTest.getUsername());

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.deleteUser(user.getUsername());
            compareTest = uDao.readUser(user.getUsername());
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
    void deleteUserFail() {
        boolean didItWork = true;
        final Connection conn1 = null;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.deleteUser("1");
//            compareTest = uDao.readUser(user.getUsername());
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
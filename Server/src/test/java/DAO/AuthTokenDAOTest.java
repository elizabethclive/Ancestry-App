package DAO;

import java.sql.Connection;

import Model.AuthToken;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDAOTest {

    private Database db;
    private AuthToken event;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        event = new AuthToken("Biking_123A", "Gale", "hello");
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
    void createTokenPass() {
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.createToken(event);
            compareTest = eDao.readToken(event.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(event.getToken(), compareTest.getToken());
    }

    @org.junit.jupiter.api.Test
    void createTokenFail() {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.createToken(event);
            eDao.createToken(event);
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

        AuthToken compareTest = event;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            compareTest = eDao.readToken(event.getToken());
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
    void readTokenPass() {
        AuthToken compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.createToken(event);
            compareTest = eDao.readToken(event.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNotNull(compareTest);
        assertEquals(compareTest.getToken(), event.getToken());
    }

    @org.junit.jupiter.api.Test
    void readTokenFail() {
        AuthToken compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.createToken(event);
            compareTest = eDao.readToken("BAD USERNAME");
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
    void deleteTokenPass() {
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.createToken(event);
            compareTest = eDao.readToken(event.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(event.getToken(), compareTest.getToken());

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.deleteToken(event.getToken());
            compareTest = eDao.readToken(event.getToken());
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
    void deleteTokenFail() {
        boolean didItWork = true;
        final Connection conn1 = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.deleteToken("1");
//            compareTest = eDao.readToken()(event.getToken()());
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
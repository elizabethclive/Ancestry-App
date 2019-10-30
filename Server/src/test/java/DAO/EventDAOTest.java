package DAO;

import java.sql.Connection;

import Model.Event;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event event;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        event = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
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
    void createEventPass() {
        Event compareTest = null;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            compareTest = eDao.readEvent(event.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(event.getId(), compareTest.getId());
    }

    @org.junit.jupiter.api.Test
    void createEventFail() {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            eDao.createEvent(event);
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

        Event compareTest = event;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            compareTest = eDao.readEvent(event.getId());
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
    void readEventPass() {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            compareTest = eDao.readEvent(event.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNotNull(compareTest);
        assertEquals(compareTest.getId(), event.getId());
    }

    @org.junit.jupiter.api.Test
    void readEventFail() {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            compareTest = eDao.readEvent("BAD USERNAME");
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
    void deleteEventPass() {
        Event compareTest = null;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            compareTest = eDao.readEvent(event.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareTest);
        assertEquals(event.getId(), compareTest.getId());

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.deleteEvent(event.getId());
            compareTest = eDao.readEvent(event.getId());
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
    void deleteEventFail() {
        boolean didItWork = true;
        final Connection conn1 = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.deleteEvent("1");
//            compareTest = eDao.readEvent(event.getId()());
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
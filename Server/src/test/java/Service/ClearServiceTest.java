package Service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Handler.ClearHandler;
import Model.Event;
import Model.Person;
import Model.User;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private Database db;
    private User user;
    private Event event;
    private Person person;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        user = new User("eclive", "password", "eclive@gmail.com",
                "Elizabeth", "Clive", "F", "1");
        event = new Event("id", "username", "personID", 10.3f,
                           234.45f, "country", "city", "eventType", 1962);
        person = new Person("firstname", "lastname", "gender", "personID124678",
                             "fatherID", "motherID", "spouseID", "username");
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
    void clearServicePass() {
        ClearService clearService = new ClearService();
        User compareUser = null;
        Event compareEvent = null;
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareUser = uDao.readUser(user.getUsername());

            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            compareEvent = eDao.readEvent(event.getId());

            PersonDAO pDao = new PersonDAO(conn);
            pDao.createPerson(person);
            comparePerson = pDao.readPerson(person.getId());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareUser);
        assertNotNull(compareEvent);
        assertNotNull(comparePerson);
        try {
            clearService.clear();
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            compareUser = uDao.readUser(user.getUsername());
            compareEvent = eDao.readEvent(event.getId());
            comparePerson = pDao.readPerson(person.getId());

            db.closeConnection(true);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }

        assertNull(compareUser);
        assertNull(compareEvent);
        assertNull(comparePerson);
    }

    @org.junit.jupiter.api.Test
    void clearServiceFail() {
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

//    @org.junit.jupiter.api.Test
//    void readUserPass() {
//        User compareTest = null;
//
//        try {
//            Connection conn = db.openConnection();
//            UserDAO uDao = new UserDAO(conn);
//            uDao.createUser(user);
//            compareTest = uDao.readUser(user.getUsername());
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//        }
//
//        assertNotNull(compareTest);
//        assertEquals(compareTest.getUsername(), user.getUsername());
//    }
//
//    @org.junit.jupiter.api.Test
//    void readUserFail() {
//        User compareTest = null;
//
//        try {
//            Connection conn = db.openConnection();
//            UserDAO uDao = new UserDAO(conn);
//            uDao.createUser(user);
//            compareTest = uDao.readUser("BAD USERNAME");
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//        }
//
//        assertNull(compareTest);
//    }
//
//    @org.junit.jupiter.api.Test
//    void deleteUserPass() {
//        User compareTest = null;
//        try {
//            Connection conn = db.openConnection();
//            UserDAO uDao = new UserDAO(conn);
//            uDao.createUser(user);
//            compareTest = uDao.readUser(user.getUsername());
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//        }
//        assertNotNull(compareTest);
//        assertEquals(user.getUsername(), compareTest.getUsername());
//
//        try {
//            Connection conn = db.openConnection();
//            UserDAO uDao = new UserDAO(conn);
//            uDao.deleteUser(user.getUsername());
//            compareTest = uDao.readUser(user.getUsername());
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//        }
//
//        assertNull(compareTest);
//    }
//
//    @org.junit.jupiter.api.Test
//    void deleteUserFail() {
//        boolean didItWork = true;
//        final Connection conn1 = null;
//
//        try {
//            Connection conn = db.openConnection();
//            UserDAO uDao = new UserDAO(conn);
//            uDao.deleteUser("1");
////            compareTest = uDao.readUser(user.getUsername());
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//            didItWork = false;
//        }
//        assertTrue(didItWork);
//    }
}
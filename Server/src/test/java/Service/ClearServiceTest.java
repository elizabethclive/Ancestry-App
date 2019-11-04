package Service;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private Database db;
    private User user;
    private Event event;
    private Event event2;
    private Person person;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
        user = new User("eclive", "password", "eclive@gmail.com",
                "Elizabeth", "Clive", "F", "1");
        event = new Event("id", "username", "personID", 10.3f,
                           234.45f, "country", "city", "eventType", 1962);
        event2 = new Event("id2", "username2", "personID2", 10.32f,
                234.452f, "country2", "city2", "eventType2", 19622);
        person = new Person("firstname", "lastname", "gender", "personID",
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
    void clearServicePass2() {
        ClearService clearService = new ClearService();
        User compareUser = null;
        Event compareEvent = null;
        Event compareEvent2 = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            compareUser = uDao.readUser(user.getUsername());

            EventDAO eDao = new EventDAO(conn);
            eDao.createEvent(event);
            eDao.createEvent(event2);
            compareEvent = eDao.readEvent(event.getId());
            compareEvent2 = eDao.readEvent(event2.getId());

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
        assertNotNull(compareEvent2);

        try {
            clearService.clear();
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            compareUser = uDao.readUser(user.getUsername());
            compareEvent = eDao.readEvent(event.getId());
            compareEvent2 = eDao.readEvent(event2.getId());

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
        assertNull(compareEvent2);
    }
}
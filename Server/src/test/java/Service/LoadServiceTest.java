package Service;

import java.sql.Connection;
import java.util.List;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    private Database db;
    private User user = new User("eclive", "password", "eclive@gmail.com",
            "Elizabeth", "Clive", "F", "1");
    private Event event  = new Event("id", "username", "personID", 10.3f,
            234.45f, "country", "city", "eventType", 1962);
    private Event event2 = new Event("id2", "username2", "personID2", 10.32f,
            234.452f, "country2", "city2", "eventType2", 19622);
    private Person person = new Person("personID", "firstname", "lastname", "gender",
            "fatherID", "motherID", "spouseID", "username");
    private User[] users = {user};
    private Event[] events = {event, event2};
    private Person[] persons = {person};
    private Person[] noPersons = {};

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        db = new Database();
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
    void loadServicePass() {
        try {
            LoadRequest loadRequest = new LoadRequest(users, persons, events);
            LoadService loadService = new LoadService();
            loadService.load(loadRequest);
        } catch (DataAccessException e) {
            System.out.println("error");
        }

        User compareUser = null;
        Event compareEvent = null;
        Event compareEvent2 = null;
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareUser = uDao.readUser(user.getUsername());

            EventDAO eDao = new EventDAO(conn);
            compareEvent = eDao.readEvent(event.getId());
            compareEvent2 = eDao.readEvent(event2.getId());

            PersonDAO pDao = new PersonDAO(conn);
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
        assertNotNull(compareEvent2);
        assertNotNull(comparePerson);
    }

    @org.junit.jupiter.api.Test
    void loadServiceFail() {
        boolean didItWork = true;

//        try {
//            Connection conn = db.openConnection();
//            EventDAO eDao = new EventDAO(conn);
//            eDao.createEvent(event);
//            eDao.createEvent(event);
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (Exception exception) {
//                System.out.println("error");
//            }
//            didItWork = false;
//        }
//        assertFalse(didItWork);
        try {
            LoadRequest loadRequest = new LoadRequest(users, noPersons, events);
            LoadService loadService = new LoadService();
            loadService.load(loadRequest);
        } catch (DataAccessException e) {
            System.out.println("error");
            didItWork = false;
        }
        assertFalse(didItWork);

        User compareUser = null;
        Event compareEvent = null;
        Event compareEvent2 = null;
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareUser = uDao.readUser(user.getUsername());

            EventDAO eDao = new EventDAO(conn);
            compareEvent = eDao.readEvent(event.getId());
            compareEvent2 = eDao.readEvent(event2.getId());

            PersonDAO pDao = new PersonDAO(conn);
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
        assertNull(compareEvent2);
        assertNull(comparePerson);
    }

}
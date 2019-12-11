package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.PersonsRequest;
import Request.LoadRequest;
import Result.PersonsResult;

import static org.junit.jupiter.api.Assertions.*;

class PersonsServiceTest {

    private Database db;
    private User user = new User("username", "password", "eclive@gmail.com",
            "Elizabeth", "Clive", "F", "1");
    private Event event  = new Event("id", "username", "personID", 10.3f,
            234.45f, "country", "city", "eventType", 1962);
    private Event event2 = new Event("id2", "username2", "personID2", 10.32f,
            234.452f, "country2", "city2", "eventType2", 19622);
    private Person person = new Person("firstname", "lastname", "gender", "personID",
            "fatherID", "motherID", "spouseID", "username");
    private AuthToken authToken = new AuthToken("authToken", "username", "personID");
    private User[] users = {user};
    private Event[] events = {event, event2};
    private Person[] noPersons = {};
    private Person[] persons = {person};

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
    void personsServicePass() throws Exception {
        PersonsResult personsResult = null;
        try {
            LoadRequest loadRequest = new LoadRequest(users, persons, events);
            LoadService loadService = new LoadService();
            loadService.load(loadRequest);
        } catch (DataAccessException e) {
            System.out.println("error");
        }

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.createToken(authToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println("error");
        }

        try {
            PersonsRequest personsRequest = new PersonsRequest(authToken.getToken());
            PersonsService personsService = new PersonsService();
            personsResult = personsService.persons(personsRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(personsResult);
        assertTrue(personsResult.isSuccess());
    }

    @org.junit.jupiter.api.Test
    void personsServiceFail() throws Exception {
        PersonsResult personsResult = null;
        try {
            LoadRequest loadRequest = new LoadRequest(users, noPersons, events);
            LoadService loadService = new LoadService();
            loadService.load(loadRequest);
        } catch (DataAccessException e) {
            System.out.println("error");
        }

        try {
            PersonsRequest personsRequest = new PersonsRequest("badAuthToken");
            PersonsService personsService = new PersonsService();
            personsResult = personsService.persons(personsRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(personsResult);
        assertFalse(personsResult.isSuccess());
    }
}
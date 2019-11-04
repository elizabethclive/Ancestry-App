package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.EventsRequest;
import Request.FillRequest;
import Result.EventsResult;
import Result.FillResult;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {

    private Database db;
    private User user = new User("username", "password", "eclive@gmail.com",
            "Elizabeth", "Clive", "F", "1");
    private AuthToken authToken = new AuthToken("authToken", "username", "personID");

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
    void fillServicePass() throws Exception {
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.createUser(user);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println("error");
        }

        try {
            FillRequest fillRequest = new FillRequest("username", 3);
            FillService fillService = new FillService();
            fillService.fill(fillRequest);
        } catch (Exception e) {
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

        EventsResult eventsResult = null;
        try {
            EventsRequest eventsRequest = new EventsRequest(authToken.getToken());
            EventsService eventsService = new EventsService();
            eventsResult = eventsService.events(eventsRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(eventsResult);
        assertTrue(eventsResult.isSuccess());
        assertFalse(eventsResult.getResult().contains("error"));
    }

    @org.junit.jupiter.api.Test
    void fillServiceFail() {
        FillResult fillResult = null;

        try {
            FillRequest fillRequest = new FillRequest("BADUSERNAME", 2);
            FillService fillService = new FillService();
            fillResult = fillService.fill(fillRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(fillResult);
        assertFalse(fillResult.isSuccess());
    }
}
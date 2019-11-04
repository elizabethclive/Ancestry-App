package Service;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    private Database db;

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
    void registerServicePass() {
        RegisterResult registerResult = null;
        try {
            RegisterRequest registerRequest = new RegisterRequest("username", "password",
                    "email", "firstname", "lastname", "gender", "personid");
            RegisterService registerService = new RegisterService();
            registerResult = registerService.register(registerRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(registerResult);
        assertTrue(registerResult.isSuccess());

        User compareUser = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareUser = uDao.readUser("username");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (Exception exception) {
                System.out.println("error");
            }
        }
        assertNotNull(compareUser);
    }

    @org.junit.jupiter.api.Test
    void registerServiceFail() {
        RegisterResult registerResultPass = null;
        RegisterResult registerResultFail = null;
        try {
            RegisterRequest registerRequest = new RegisterRequest("username", "password",
                    "email", "firstname", "lastname", "gender", "personid");
            RegisterService registerService = new RegisterService();
            registerResultPass = registerService.register(registerRequest);
            registerResultFail = registerService.register(registerRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(registerResultPass);
        assertNotNull(registerResultFail);
        assertTrue(registerResultPass.isSuccess());
        assertFalse(registerResultFail.isSuccess());
    }
}
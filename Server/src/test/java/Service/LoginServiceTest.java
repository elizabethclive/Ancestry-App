package Service;

import DAO.Database;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
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
    void loginServicePass() {
        RegisterResult registerResult = null;
        LoginResult loginResult = null;
        try {
            RegisterRequest registerRequest = new RegisterRequest("username", "password",
                    "email", "firstname", "lastname", "gender", "personid");
            RegisterService registerService = new RegisterService();
            registerResult = registerService.register(registerRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertTrue(registerResult.isSuccess());

        try {
            LoginRequest loginRequest = new LoginRequest("username", "password");
            LoginService loginService = new LoginService();
            loginResult = loginService.login(loginRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
    }

    @org.junit.jupiter.api.Test
    void loginServiceFail() {
        RegisterResult registerResult = null;
        LoginResult loginResult = null;
        try {
            RegisterRequest registerRequest = new RegisterRequest("username", "password",
                    "email", "firstname", "lastname", "gender", "personid");
            RegisterService registerService = new RegisterService();
            registerResult = registerService.register(registerRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertTrue(registerResult.isSuccess());

        try {
            LoginRequest loginRequest = new LoginRequest("username", "BADPASSWORD");
            LoginService loginService = new LoginService();
            loginResult = loginService.login(loginRequest);
        } catch (Exception e) {
            System.out.println("error");
        }

        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
    }
}
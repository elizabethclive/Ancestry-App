package Service;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

public class LoginService {
    /**
     * Logs in a user.
     * @param request LoginRequest
     * @return LoginResult to the user
     */
    public LoginResult login(LoginRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.getConnection();
            db.clearTables();

            String username = request.getUsername();
            String password = request.getPassword();

            UserDAO uDao = new UserDAO(conn);

            User user = uDao.readUser(username);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    String authTokenString = RandomString.getRandomString();
                    AuthToken token = new AuthToken(authTokenString, username, user.getPersonID());
                    String serializedToken = JsonHandler.serialize(token);
                    db.closeConnection(true);
                    return new LoginResult(true, serializedToken);
                } else {
                    db.closeConnection(false);
                    return new LoginResult(false, "Password was incorrect");
                }
            } else {
                return new LoginResult(false,"User not found.");
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new LoginResult(false, e.getMessage());
        }
    }
}

package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
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
            Connection conn = db.openConnection();
//            db.clearTables();

            String userName = request.getUsername();
            String password = request.getPassword();

            UserDAO uDao = new UserDAO(conn);

            User user = uDao.readUser(userName);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    String authTokenString = RandomString.getRandomString();
                    AuthToken authToken = new AuthToken(authTokenString, userName, user.getPersonID());
                    AuthTokenDAO aDao = new AuthTokenDAO(conn);
                    aDao.createToken(authToken);
                    String serializedToken = JsonHandler.serialize(authToken);
                    db.closeConnection(true);
                    return new LoginResult(true, serializedToken);
                } else {
                    db.closeConnection(false);
                    return new LoginResult(false, "Error: Password is incorrect");
                }
            } else {
                db.closeConnection(false);
                return new LoginResult(false,"Error: User not found.");
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new LoginResult(false, e.getMessage());
        }
    }
}

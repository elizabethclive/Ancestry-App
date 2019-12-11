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
        System.out.println("*******************************IN LOGIN SERVICE");

        try {
            Connection conn = db.openConnection();
            String userName = request.getUsername();
            String password = request.getPassword();

            UserDAO uDao = new UserDAO(conn);
            System.out.println("in read user");
            User user = uDao.readUser(userName);
            if (user != null) {
                System.out.println("user is not null. user password: " + user.getPassword() + " password: " + password);
                if (user.getPassword().equals(password)) {
                    System.out.println("passwords are equal");
                    String authTokenString = RandomString.getRandomString();
                    AuthToken authToken = new AuthToken(authTokenString, userName, user.getPersonID());
                    AuthTokenDAO aDao = new AuthTokenDAO(conn);
                    aDao.createToken(authToken);
                    String serializedToken = JsonHandler.serialize(authToken);
                    db.closeConnection(true);
                    System.out.println("about to return a true loginresult with serialized token: " + serializedToken);
                    return new LoginResult(true, serializedToken);
                } else {
                    System.out.println("password is incorrect");
                    db.closeConnection(false);
                    return new LoginResult(false, "Error: Password is incorrect");
                }
            } else {
                System.out.println("user not found");
                db.closeConnection(false);
                return new LoginResult(false,"Error: User not found.");
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new LoginResult(false, e.getMessage());
        }
    }
}

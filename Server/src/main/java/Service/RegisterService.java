package Service;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.User;
import Request.RegisterRequest;
import Result.RegisterResult;

public class RegisterService {
    /**
     * Logs in a user.
     * @param request RegisterRequest
     * @return RegisterResult to the user
     */
    public RegisterResult register(RegisterRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.getConnection();
            db.clearTables(); //FIXME

            String username = request.getUsername();
            String personID = request.getPersonID();
            if (personID == null) {
                personID = RandomString.getRandomString();
            }

            UserDAO uDao = new UserDAO(conn);
            User user = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender(), personID);
            uDao.createUser(user);

            // TODO: create 4 generations of ancestor data

            String authTokenString = RandomString.getRandomString();
            AuthToken token = new AuthToken(authTokenString, username, user.getPersonID());
            String serializedToken = JsonHandler.serialize(token);
            db.closeConnection(true);
            return new RegisterResult(true, serializedToken);

        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new RegisterResult(false, e.getMessage());
        }
    };
}

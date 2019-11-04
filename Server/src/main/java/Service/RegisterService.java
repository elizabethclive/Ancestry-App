package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.FillRequest;
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
            Connection conn = db.openConnection();
            String userName = request.getUsername();
            String personID = request.getPersonID();
            if (personID == null) {
                personID = RandomString.getRandomString();
            }
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.readUser(userName);
            if (user != null) {
                db.closeConnection(false);
                return new RegisterResult(false, "Error: This user already exists.");
            }
            user = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender(), personID);
            uDao.createUser(user);

            PersonDAO pDao = new PersonDAO(conn);
            Person person = new Person(request.getFirstName(), request.getLastName(), request.getGender(), personID, null, null, null, request.getUsername());

            String authTokenString = RandomString.getRandomString();
            AuthToken authToken = new AuthToken(authTokenString, userName, user.getPersonID());
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.createToken(authToken);
            String serializedToken = JsonHandler.serialize(authToken);
            db.closeConnection(true);
            return new RegisterResult(true, serializedToken);

        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new RegisterResult(false, e.getMessage());
        }
    }
}

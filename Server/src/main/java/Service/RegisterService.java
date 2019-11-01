package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
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
//            db.clearTables(); //FIXME
            String userName = request.getUsername();
            String personID = request.getPersonID();
            if (personID == null) {
                personID = RandomString.getRandomString();
            }
            System.out.println("before read user");
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.readUser(userName);
            if (user != null) {
                db.closeConnection(false);
                return new RegisterResult(false, "Error: This user already exists.");
            }
            System.out.println("before create user");
            user = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender(), personID);
            uDao.createUser(user);

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

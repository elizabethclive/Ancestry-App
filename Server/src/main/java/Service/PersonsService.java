package Service;

import java.sql.Connection;
import java.util.ArrayList;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.PersonsRequest;
import Result.PersonsResult;

public class PersonsService {
    /**
     * Logs in a user.
     * @param request PersonsRequest
     * @return PersonsResult to the user
     */
    public PersonsResult persons(PersonsRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.openConnection();

            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken authToken = aDao.readToken(request.getToken());

            if (authToken == null) {
                db.closeConnection(false);
                return new PersonsResult(false, "Error: Invalid authorization authToken:(");
            }

            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.readUser(authToken.getUsername());
            ArrayList<Person> persons = pDao.getAssociatedPersons(user.getUsername());

            if (persons == null) {
                db.closeConnection(false);
                return new PersonsResult(false, "Error: No persons found");
            } else {
                String serializedPersons = "{\n\"data\":" + JsonHandler.serialize(persons) + "\n}";
                db.closeConnection(true);
                return new PersonsResult(true, serializedPersons);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new PersonsResult(false, e.getMessage());
        }
    }
}

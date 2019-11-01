package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.Person;
import Request.PersonRequest;
import Result.PersonResult;

public class PersonService {
    /**
     * Gets a person specified by the user.
     * @param request PersonRequest
     * @return PersonResult to the user
     */
    public PersonResult person(PersonRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.openConnection();

            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken authToken = aDao.readToken(request.getToken());

            if (authToken == null) {
                db.closeConnection(false);
                return new PersonResult(false, "Error: Invalid authorization authToken!");
            }

            PersonDAO pDao = new PersonDAO(conn);
            Person person = pDao.readPerson(request.getPersonID());

            if (person == null) {
                db.closeConnection(false);
                return new PersonResult(false, "Error: There is no person");
            } else if (!person.getUsername().equals(authToken.getUsername())) {
                db.closeConnection(false);
                return new PersonResult(false, "Error: Person does not belong to the user");
            } else {
                String serializedPerson = JsonHandler.serialize(person);
                db.closeConnection(true);
                return new PersonResult(true, serializedPerson);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new PersonResult(false, e.getMessage());
        }
    }
}

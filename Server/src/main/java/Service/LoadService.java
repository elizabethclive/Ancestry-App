package Service;

import java.sql.Connection;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

public class LoadService {
    private int numUsers;
    private int numPersons;
    private int numEvents;
    /**
     * Loads a user.
     * @param request LoadRequest
     * @return LoadResult to the user
     */
    public LoadResult load(LoadRequest request) throws DataAccessException {
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            db.clearTables();

            User[] users = request.getUsers();
            Person[] persons = request.getPersons();
            Event[] events = request.getEvents();
            if (events == null || persons == null || users == null) {
                db.closeConnection(false);
                return new LoadResult(false, "Error: Invalid input.");
            }

            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            for (User user : users) {
                uDao.createUser(user);
                numUsers++;
            }
            for (Person person : persons) {
                pDao.createPerson(person);
                numPersons++;
            }
            for (Event event : events) {
                eDao.createEvent(event);
                numEvents++;
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new LoadResult(false, e.getMessage());
        }

        return new LoadResult(true,"Successfully added " + numUsers + " users, " + numPersons + " persons, and " + numEvents + " events to the database.");
    }
}

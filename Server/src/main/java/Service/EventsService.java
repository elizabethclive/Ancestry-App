package Service;

import java.sql.Connection;
import java.util.ArrayList;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.UserDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.Event;
import Model.User;
import Request.EventsRequest;
import Result.EventsResult;

public class EventsService {
    /**
     * Logs in a user.
     * @param request EventsRequest
     * @return EventsResult to the user
     */
    public EventsResult events(EventsRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken authToken = aDao.readToken(request.getToken());
            if (authToken == null) {
                db.closeConnection(false);
                return new EventsResult(false, "Error: Invalid authorization authToken");
            }

            EventDAO eDao = new EventDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.readUser(authToken.getUsername());
            ArrayList<Event> events = eDao.getAssociatedEvents(user.getUsername());
            if (events == null) {
                db.closeConnection(false);
                return new EventsResult(false, "Error: No events found");
            } else {
                String serializedEvents = "{\n\"data\":" + JsonHandler.serialize(events) + "\n}";;
                db.closeConnection(true);
                return new EventsResult(true, serializedEvents);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new EventsResult(false, e.getMessage());
        }
    }
}

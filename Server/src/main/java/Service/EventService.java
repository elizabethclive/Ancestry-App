package Service;

import java.sql.Connection;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Handler.JsonHandler;
import Model.AuthToken;
import Model.Event;
import Request.EventRequest;
import Result.EventResult;

public class EventService {
    /**
     * Logs in a user.
     * @param request EventRequest
     * @return EventResult to the user
     */
    public EventResult event(EventRequest request) throws Exception {
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken authToken = aDao.readToken(request.getToken());
            if (authToken == null) {
                db.closeConnection(false);
                return new EventResult(false, "Error: Invalid authorization authToken!");
            }

            EventDAO eDao = new EventDAO(conn);
            Event event = eDao.readEvent(request.getEventID());
            if (event == null) {
                db.closeConnection(false);
                return new EventResult(false, "Error: There is no event");
            } else if (!event.getUsername().equals(authToken.getUsername())) {
                db.closeConnection(false);
                return new EventResult(false, "Error: Event does not belong to the user");
            } else {
                String serializedEvent = JsonHandler.serialize(event);
                db.closeConnection(true);
                return new EventResult(true, serializedEvent);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new EventResult(false, e.getMessage());
        }
    }
}

package Service;

import DAO.DataAccessException;
import DAO.Database;
import Result.ClearResult;

public class ClearService {
    /**
     * Logs in a user.
     * @return ClearResult to the user
     */
    public ClearResult clear() throws DataAccessException { // added throws ioexception

        Database db = new Database();

        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new ClearResult(false, e.getMessage());
        }
        return new ClearResult(true, "Clear succeeded");
    }
}


package Handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import DAO.DataAccessException;
import Result.ClearResult;
import Service.ClearService;

/*
	The ClaimRouteHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/routes/claim" URL path.

	Notice that ClaimRouteHandler implements the HttpHandler interface,
	which is defined by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/routes/claim" URL path, it calls
	ClaimRouteHandler.handle() which actually processes the request.
*/
public class ClearHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                ClearService clearService = new ClearService();
                ClearResult result = clearService.clear();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();

                writeString(JsonHandler.serialize(result), respBody);
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            exchange.getResponseBody().close();
        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
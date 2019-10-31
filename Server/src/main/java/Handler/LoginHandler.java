package Handler;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import DAO.DataAccessException;
import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;

public class LoginHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoginRequest loginRequest = JsonHandler.deserialize(reqData, LoginRequest.class);
                reqBody.close();
                LoginService loginService = new LoginService();
                LoginResult loginResult = loginService.login(loginRequest);

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                writeString(JsonHandler.serialize(loginResult), respBody);
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            // exchange.getResponseBody().close();
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }


    }
}

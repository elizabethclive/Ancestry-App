package Handler;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;

public class LoginHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~`IN HERERERERE!!!!");
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoginRequest loginRequest = JsonHandler.deserialize(reqData, LoginRequest.class);
                reqBody.close();
                LoginService loginService = new LoginService();
                LoginResult loginResult = loginService.login(loginRequest);
                System.out.println("Just got login result: " + loginResult.isSuccess() + " message: " + loginResult.getResult());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                if (loginResult.isSuccess()) {
                    System.out.println("writing success string to respbody");
                    writeString(loginResult.getResult(), respBody);
                } else {
                    System.out.println("writing serialized result to respbody");
                    writeString(JsonHandler.serialize(loginResult), respBody);
                }
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}

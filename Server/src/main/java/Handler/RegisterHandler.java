package Handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Model.AuthToken;
import Request.FillRequest;
import Request.RegisterRequest;
import Result.RegisterResult;
import Service.FillService;
import Service.RegisterService;

public class RegisterHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                RegisterRequest registerRequest = JsonHandler.deserialize(reqData, RegisterRequest.class);
                reqBody.close();
                RegisterService registerService = new RegisterService();
                RegisterResult registerResult = registerService.register(registerRequest);
                if (registerResult.isSuccess()) {
                    FillRequest fillRequest = new FillRequest(registerRequest.getUsername(), 4);
                    FillService fillService = new FillService();
                    fillService.fill(fillRequest);

                    System.out.println("register result is a success");
                    AuthToken token = JsonHandler.deserialize(registerResult.getResult(), AuthToken.class);
                    String deserializedResult = token.getToken();
                    exchange.getResponseHeaders().set("Authorization", deserializedResult);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    System.out.println("Register result is not a success");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                OutputStream respBody = exchange.getResponseBody();
                if (registerResult.isSuccess()) {
                    writeString(registerResult.getResult(), respBody);
                } else {
                    writeString(JsonHandler.serialize(registerResult), respBody);
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

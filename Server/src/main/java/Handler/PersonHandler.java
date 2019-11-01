package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Request.PersonRequest;
import Request.PersonsRequest;
import Result.PersonResult;
import Result.PersonsResult;
import Service.PersonService;
import Service.PersonsService;

public class PersonHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String personID = null;
                    String[] urlParts = exchange.getRequestURI().toString().split("/");
                    if (urlParts.length > 2) personID = urlParts[2];
                    if (personID == null) {
                        PersonsRequest personsRequest = new PersonsRequest(authToken);
                        PersonsService personsService = new PersonsService();
                        PersonsResult personsResult = personsService.persons(personsRequest);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();
                        if (personsResult.isSuccess()) {
                            writeString(personsResult.getResult(), respBody);
                        } else {
                            writeString(JsonHandler.serialize(personsResult), respBody);
                        }
                        respBody.close();
                    } else {
                        PersonRequest personRequest = new PersonRequest(authToken, personID);
                        PersonService personService = new PersonService();
                        PersonResult personResult = personService.person(personRequest);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();
                        if (personResult.isSuccess()) {
                            writeString(personResult.getResult(), respBody);
                        } else {
                            writeString(JsonHandler.serialize(personResult), respBody);
                        }
                        respBody.close();
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
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

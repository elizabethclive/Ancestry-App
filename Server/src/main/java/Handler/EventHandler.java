package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Request.EventRequest;
import Request.EventsRequest;
import Result.EventResult;
import Result.EventsResult;
import Service.EventService;
import Service.EventsService;

public class EventHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String eventID = null;
                    String[] urlParts = exchange.getRequestURI().toString().split("/");
                    if (urlParts.length > 2) eventID = urlParts[2];

                    if (eventID == null) {
                        EventsRequest eventsRequest = new EventsRequest(authToken);
                        EventsService eventsService = new EventsService();
                        EventsResult eventsResult = eventsService.events(eventsRequest);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();
                        if (eventsResult.isSuccess()) {
                            writeString(eventsResult.getResult(), respBody);
                        } else {
                            writeString(JsonHandler.serialize(eventsResult), respBody);
                        }
                        respBody.close();
                    } else {
                        EventRequest eventRequest = new EventRequest(authToken, eventID);
                        EventService eventService = new EventService();
                        EventResult eventResult = eventService.event(eventRequest);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();
                        if (eventResult.isSuccess()) {
                            writeString(eventResult.getResult(), respBody);
                        } else {
                            writeString(JsonHandler.serialize(eventResult), respBody);
                        }
                        respBody.close();
                    }
                } else {
                    System.out.println("unauthorized");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
            } else {
                System.out.println("bad request");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
        } catch (Exception e) {
            System.out.println("internal error");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}

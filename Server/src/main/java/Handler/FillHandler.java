package Handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Request.FillRequest;
import Result.FillResult;
import Service.FillService;

public class FillHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                String url = exchange.getRequestURI().toString();
                FillRequest fillRequest = makeRequest(url);
                FillService fillService = new FillService();
                FillResult fillResult = fillService.fill(fillRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                writeString(JsonHandler.serialize(fillResult), respBody);
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().close();
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private FillRequest makeRequest(String url) {
        int generations = 4;
        String[] urlParts = url.split("/");
        String userName = urlParts[2];
        if (urlParts.length > 3) {
            generations = Integer.parseInt(urlParts[3]);
        }
        return new FillRequest(userName, generations);
    }
}

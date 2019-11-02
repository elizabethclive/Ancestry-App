package Handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import DAO.DataAccessException;
import Request.LoadRequest;
import Result.LoadResult;
import Service.LoadService;

public class LoadHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("-------------------------------AT START OF LOAD HANDLER!!!!!!!!!");
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();

                String reqData = readString(reqBody);

                LoadRequest loadRequest = JsonHandler.deserialize(reqData, LoadRequest.class);
                System.out.println("reqData" + reqData);
                System.out.println("json handler deserialize" + JsonHandler.deserialize(reqData, LoadRequest.class));
                reqBody.close();
                LoadService loadService = new LoadService();
                LoadResult loadResult = loadService.load(loadRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                writeString(JsonHandler.serialize(loadResult), respBody);
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}

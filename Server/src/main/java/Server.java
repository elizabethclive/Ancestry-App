import com.sun.net.httpserver.HttpServer;

import java.net.*;
import java.io.*;

import DAO.DataAccessException;
import DAO.Database;
import Handler.ClearHandler;
import Handler.EventHandler;
//import Handler.EventsHandler;
import Handler.FileHandler;
import Handler.FillHandler;
import Handler.LoadHandler;
import Handler.LoginHandler;
import Handler.PersonHandler;
//import Handler.PersonsHandler;
import Handler.RegisterHandler;

public class Server {
    public static void main(String[] args) throws Exception {
        Database db = new Database();

        try {
            db.openConnection();
            db.createTables();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
            System.out.println("Error creating the database");
        }

        int portNumber = Integer.parseInt(args[0]);
        new Server().startServer(portNumber);

    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);

        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }
    private void registerHandlers(HttpServer server) {
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
    }
}
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
    public void main(String[] args) throws IOException {
        Database db = new Database();

        try {
            db.createTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Error creating the database");
        }



        int portNumber = Integer.parseInt(args[0]);
        startServer(portNumber);

//        ServerSocket serverSocket = null;
//        Socket clientSocket = null;
//        try {
//            serverSocket = new ServerSocket(8000);
//            clientSocket = serverSocket.accept();
//        } catch (IOException e) {
//            System.err.println("Error trying to connect to socket");
//            System.exit(1);
//        }
//
//
//        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(
//                        clientSocket.getInputStream()));
//
//
//
//        ClearHandler.handle();
//
//
//
//        String inputLine, outputLine;
//        KnockKnockProtocol kkp = new KnockKnockProtocol();
//
//        outputLine = kkp.processInput(null);
//        out.println(outputLine);
//
//        while ((inputLine = in.readLine()) != null) {
//            outputLine = kkp.processInput(inputLine);
//            out.println(outputLine);
//            if (outputLine.equals("Bye."))
//                break;
//        }
//        out.close();
//        in.close();
//        clientSocket.close();
//        serverSocket.close();
    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);

        server.setExecutor(null); // not in the slides

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
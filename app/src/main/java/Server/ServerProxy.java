package Server;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Model.AuthToken;
import Util.EventsResult;
import Util.JsonHandler;
import Util.LoginRequest;
import Util.LoginResult;
import Util.PersonsResult;
import Util.RegisterRequest;
import Util.RegisterResult;

public class ServerProxy {

    public LoginResult login(String serverHost, String serverPort, LoginRequest request) throws Exception {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = connection.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                LoginResult loginResult;
                if (respData.contains("Error")) {
                    loginResult = gson.fromJson(respData, LoginResult.class);
                } else {
                    AuthToken authToken = gson.fromJson(respData, AuthToken.class);
                    loginResult = new LoginResult(true, JsonHandler.serialize(authToken));
                }
                return loginResult;
            } else {
                System.out.println("ERROR:" + connection.getResponseMessage());
                return new LoginResult(false, connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult(false, e.getMessage());
        }
    }

    public RegisterResult register(String serverHost, String serverPort, RegisterRequest request) throws Exception {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = connection.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                RegisterResult registerResult;
                if (respData.contains("Error")) {
                    registerResult = gson.fromJson(respData, RegisterResult.class);
                } else {
                    AuthToken authToken = gson.fromJson(respData, AuthToken.class);
                    registerResult = new RegisterResult(true, JsonHandler.serialize(authToken));
                }
                return registerResult;
            } else {
                System.out.println("ERROR:" + connection.getResponseMessage());
                return new RegisterResult(false, connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult(false, e.getMessage());
        }
    }

    public PersonsResult persons(String serverHost, String serverPort, String authToken) throws Exception {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            Gson gson = new Gson();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                System.out.println(respData);
                PersonsResult result = gson.fromJson(respData, PersonsResult.class);
                return result;
            } else {
                System.out.println("ERROR:" + connection.getResponseMessage());
                return new PersonsResult(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new PersonsResult(e.getMessage());
        }
    }

    public EventsResult events(String serverHost, String serverPort, String authToken) throws Exception {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            Gson gson = new Gson();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                EventsResult result = gson.fromJson(respData, EventsResult.class);
                return result;
            } else {
                System.out.println("ERROR:" + connection.getResponseMessage());
                return new EventsResult(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new EventsResult(e.getMessage());
        }
    }

    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}

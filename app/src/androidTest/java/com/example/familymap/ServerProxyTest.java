package com.example.familymap;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import Model.Event;
import Model.Person;
import Server.ServerProxy;
import Util.EventsResult;
import Util.LoginRequest;
import Util.LoginResult;
import Util.PersonsResult;
import Util.RegisterRequest;
import Util.RegisterResult;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ServerProxyTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.familymap", appContext.getPackageName());
    }

    private ServerProxy sp;
    private String host = "10.0.2.2";
    private String port = "8080";
    private Person person = new Person("test", "person", "f", "testperson", "", "", "", "sheila");
    private Event  event = new Event("event", "sheila", "Sheila_Parker", 12, 12, "USA", "boise", "birth", 1998);
    private String authToken = "8bf960b9-7f56-41b9-a52f-7844d2cb7049";
    private String badAuthToken = "asdf";

    @Before
    public void setUp() {
        sp = new ServerProxy();
    }

    @Test
    public void testLoginPass() throws Exception {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = sp.login(host, port, loginRequest);
        assertTrue(loginResult.isSuccess());
    }

    @Test
    public void testLoginFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest("badUsername", "badPassword");
        LoginResult loginResult = sp.login(host, port, loginRequest);
        assertFalse(loginResult.isSuccess());
    }

    @Test
    public void testRegisterPass() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("new", "password", "email@email.com", "User", "Smith", "f", "newUser");
        RegisterResult registerResult = sp.register(host, port, registerRequest);
        assertTrue(registerResult.isSuccess());
    }

    @Test
    public void testRegisterFail() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("sheila", "parker", "email@email.com", "User", "Smith", "f", "newUser");
        RegisterResult registerResult = sp.register(host, port, registerRequest);
        assertFalse(registerResult.isSuccess());
    }

    @Test
    public void testPersonsPass() throws Exception {
        PersonsResult personsResult = sp.persons(host, port, authToken);
        assertEquals(8, personsResult.getData().length);
        for (int i = 0; i < personsResult.getData().length; i++) {
            assertEquals(personsResult.getData()[i].getClass(), person.getClass());
        }
    }

    @Test
    public void testPersonsFail() throws Exception {
        PersonsResult personsResult = sp.persons(host, port, badAuthToken);
        assertNull(personsResult.getData());
    }

    @Test
    public void testEventsPass() throws Exception {
        EventsResult eventsResult = sp.events(host, port, authToken);
        assertEquals(16, eventsResult.getData().length);
        for (int i = 0; i < eventsResult.getData().length; i++) {
            assertEquals(eventsResult.getData()[i].getClass(), event.getClass());
        }
    }

    @Test
    public void testEventsFail() throws Exception {
        EventsResult eventsResult = sp.events(host, port, badAuthToken);
        assertNull(eventsResult.getData());
    }
}

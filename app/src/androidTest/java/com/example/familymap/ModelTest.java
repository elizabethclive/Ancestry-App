package com.example.familymap;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Model.Event;
import Model.Model;
import Model.Person;
import Server.ServerProxy;
import Util.EventsResult;
import Util.LoginRequest;
import Util.LoginResult;
import Util.PersonsResult;
import Util.RegisterRequest;
import Util.RegisterResult;
import Util.Settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ModelTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.familymap", appContext.getPackageName());
    }

    private ServerProxy sp;
    private String host = "10.0.2.2";
    private String port = "8080";
    private Person person = new Person("test", "person", "f", "person", "father", "mother", "spouse", "person");
    private Person father = new Person("test", "father", "m", "father", "grandpa", null, "mother", "person");
    private Person mother = new Person("test", "mother", "f", "mother", null, "momGrandpa", "father", "person");
    private Person spouse = new Person("test", "spouse", "m", "spouse", null, null, "person", "person");
    private Person dadsGrandpa = new Person("test", "grandpa", "m", "grandpa", null, null, null, "person");
    private Person momsGrandpa = new Person("test", "momGrandpa", "m", "momGrandpa", null, null, null, "person");
    private Person unrelated = new Person("unrelated", "unrelated", "m", "unrelated", null, null, null, "person");
    private Event  event = new Event("personbirth", "person", "person", 12, 12, "USA", "boise", "birth", 1995);
    private Event  momMarriage = new Event("momMarriage", "mother", "mother", 60, 60, "USA", "boise", "marriage", 1996);
    private Event  dadMarriage = new Event("dadMarriage", "father", "father", 60, 60, "USA", "boise", "marriage", 1996);
    private Event  personMarriage = new Event("personMarriage", "sheila", "Sheila_Parker", 12, 12, "USA", "boise", "marriage", 2018);
    private String authToken = "8bf960b9-7f56-41b9-a52f-7844d2cb7049";
    private String badAuthToken = "asdf";
    private Settings settings = new Settings();

    @Before
    public void setUp() throws Exception {
//        sp = new ServerProxy();
//        sp.persons(host, port, authToken);
        Person[] persons = {person, father, mother, spouse, dadsGrandpa, momsGrandpa, unrelated};
        Event[] events = {personMarriage, event, momMarriage, dadMarriage};
        Model.getInstance().setPersons(persons);
        Model.getInstance().setEvents(events);
        Model.getInstance().setPersonID(person.getId());
        Model.getInstance().setUserName(person.getUsername());
    }

    @Test
    public void testRelationshipsPass() throws Exception {
        HashMap<Person, String> relations = Model.getInstance().getImmediateRelations(person);
        assertEquals(3, relations.size());
        for (Map.Entry<Person, String> entry : relations.entrySet()) {
            assertNotEquals(entry.getKey().getId(), unrelated.getId());
            assertNotEquals(entry.getKey().getId(), dadsGrandpa.getId());
            assertNotEquals(entry.getKey().getId(), momsGrandpa.getId());
        }
    }

    @Test
    public void testRelationshipsAbnormal() throws Exception {
        HashMap<Person, String> relations = Model.getInstance().getImmediateRelations(unrelated);
        assertEquals(relations.size(), 0);
    }

    @Test
    public void testFilterPass() throws Exception {
        Model.getInstance().getSettings().setFemaleEvents(true);
        Event[] events = Model.getInstance().getEvents();
        for (Event event : events) {
            assertEquals(Model.getInstance().getPersonFromId(event.getPersonID()).getGender(), "f");
        }

        ArrayList<Person> fathersPeople = new ArrayList<>();
        fathersPeople = Model.getInstance().getAncestors(Model.getInstance().getPersonFromId(person.getFatherID()), fathersPeople);
        Model.getInstance().getSettings().setFemaleEvents(false);
        Model.getInstance().getSettings().setMothersSide(true);
        events = Model.getInstance().getEvents();
        for (Event event : events) {
            Person eventPerson = Model.getInstance().getPersonFromId(event.getPersonID());
            for (Person person : fathersPeople) {
                assertNotEquals(person.getId(), eventPerson.getId());
            }
        }
    }

    @Test
    public void testFilterAbnormal() throws Exception {
        Model.getInstance().getSettings().setFemaleEvents(true);
        Model.getInstance().getSettings().setFathersSide(true);
        Event[] events = Model.getInstance().getEvents();
        assertEquals(1, events.length);
    }

    @Test
    public void testEventSortingPass() throws Exception {
        ArrayList<Event> events = Model.getInstance().getPersonEvents(person);
        int previousYear = 0;

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventType().toLowerCase().equals("birth")) {
                assertEquals(i, 0);
            } else if (events.get(i).getEventType().toLowerCase().equals("death")) {
                assertEquals(i, events.size()-1);
            }
            assertTrue(events.get(i).getYear() > previousYear);
            previousYear = events.get(i).getYear();
        }
    }

    @Test
    public void testEventSortingAbnormal() throws Exception {
        ArrayList<Event> events = Model.getInstance().getPersonEvents(unrelated);
        assertEquals(events.size(), 0);
    }

    @Test
    public void testSearchPass() throws Exception {
        String input = "person";
        ArrayList<Event> events = Model.getInstance().getFilteredEvents(input);
        assertEquals(1, events.size());
        for (Event event : events) {
            Person currentPerson = Model.getInstance().getPersonFromId(event.getPersonID());
            assertTrue(currentPerson.getFirstName().toLowerCase().contains(input) ||
                    currentPerson.getLastName().toLowerCase().contains(input) ||
                    event.getCountry().toLowerCase().contains(input) ||
                    event.getCity().toLowerCase().contains(input) ||
                    event.getEventType().toLowerCase().contains(input) ||
                    Integer.toString(event.getYear()).contains(input));
        }

        input = "test";
        ArrayList<Person> persons = Model.getInstance().getFilteredPersons(input);
        assertEquals(6, persons.size());
        for (Person currentPerson : persons) {
            assertTrue(currentPerson.getFirstName().toLowerCase().contains(input) || currentPerson.getLastName().toLowerCase().contains(input));
        }
    }

    @Test
    public void testSearchAbnormal() throws Exception {
        String input = "asdf";
        ArrayList<Event> events = Model.getInstance().getFilteredEvents(input);
        assertEquals(0, events.size());
        ArrayList<Person> persons = Model.getInstance().getFilteredPersons(input);
        assertEquals(0, persons.size());
    }
}

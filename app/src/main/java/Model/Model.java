package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Util.Settings;

public class Model {
    private static Model instance;
    private String port;
    private String host;

    // Current user
    private String authToken;
    private String userName;
    private String personID;
    private Person[] persons;
    private Event[] events;
    private String firstName;
    private String lastName;

    // Login
    private Map<String, Person> personsMap = new HashMap<String, Person>();
    private Map<String, Event> eventsMap = new HashMap<String, Event>();

    // Other fragments, activities
    private Settings settings;
    private Person selectedPerson;
    private Event selectedEvent;
    private Boolean inEventActivity;
    private HashMap<String, Integer> colorMap = new HashMap<>();
    private static ArrayList<String> colors = new ArrayList<String>();
    private Integer colorIndex = 0;

    public Map<String, Person> getPersonsMap() {
        return personsMap;
    }

    public Map<String, Event> getEventsMap() {
        return eventsMap;
    }

    public Person getPersonFromMap(String id) {
        return personsMap.get(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
        for (Person person : persons) {
            personsMap.put(person.getId(), person);
        }
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
        for (Event event : events) {
            eventsMap.put(event.getId(), event);
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public Person getPersonFromId(String personID) {
        for (int i = 0; i < persons.length; i++) {
            if (persons[i].getId().equals(personID)) {
                return persons[i];
            }
        }
        return null;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public Event getEventFromId(String eventID) {
        for (int i = 0; i < events.length; i++) {
            if (events[i].getId().equals(eventID)) {
                return events[i];
            }
        }
        return null;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Boolean getInEventActivity() {
        return inEventActivity;
    }

    public void setInEventActivity(Boolean inEventActivity) {
        this.inEventActivity = inEventActivity;
    }

    public HashMap<String, Integer> getColorMap() {
        return colorMap;
    }

    public void setColorMap(HashMap<String, Integer> colorMap) {
        this.colorMap = colorMap;
    }

    public void addToColorMap(String eventType) {
        colorMap.put(eventType, colorIndex);
        if (colorIndex == colors.size() - 1) colorIndex = 0; else colorIndex++;
    }

    public String getFullName(Person currentPerson) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentPerson.getFirstName() + " " + currentPerson.getLastName());
        return sb.toString();
    }

    public ArrayList<Event> getPersonEvents(Person currentPerson) {
        ArrayList<Event> currentEvents = new ArrayList<>();
        for (int i = 0; i < events.length; i++) {
            if (events[i].getPersonID().equals(currentPerson.getId())) {
                currentEvents.add(events[i]);
            }
        }

        Collections.sort(currentEvents, (p1, p2) -> p1.getYear() - (p2.getYear()));

        for (int i = 0; i < currentEvents.size(); i++) {
            Event currentEvent = currentEvents.get(i);
            String currentEventType = currentEvent.getEventType().toLowerCase();
            if (currentEventType.equals("birth") && i != 0) {
                Event temp = currentEvents.get(0);
                currentEvents.set(0, currentEvent);
                currentEvents.set(i, temp);
            } else if (currentEventType.equals("death") && i != currentEvents.size()-1) {
                Event temp = currentEvents.get(currentEvents.size()-1);
                currentEvents.set(currentEvents.size()-1, currentEvent);
                currentEvents.set(i, temp);
//            } else if (i > 0 && !currentEvents.get(i-1).getEventType().toLowerCase().equals("birth") && !currentEventType.equals("death") && currentEvent.getYear() < currentEvents.get(i-1).getYear()) {
//                Event temp = currentEvents.get(i-1);
//                currentEvents.set(i-1, currentEvent);
//                currentEvents.set(i, temp);
            } else if (i > 0 && !currentEvents.get(i-1).getEventType().toLowerCase().equals("birth") && !currentEventType.equals("death") && currentEvent.getYear() == currentEvents.get(i-1).getYear()) {
                if (currentEvent.getEventType().compareToIgnoreCase(currentEvents.get(i-1).getEventType()) < 0) {
                    Event temp = currentEvents.get(i-1);
                    currentEvents.set(i-1, currentEvent);
                    currentEvents.set(i, temp);
                }
            }
        }

        return currentEvents;
    }

    public String getEventDetails(Event currentEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentEvent.getEventType().toUpperCase() + ": " + currentEvent.getCity() + ", " + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");
        return sb.toString();
    }

    public HashMap<Person, String> getImmediateRelations(Person initialPerson) {
        HashMap<Person, String> currentPersons = new HashMap<Person, String>();
        for (int i = 0; i < persons.length; i++) {
            if (persons[i].getMotherID() != null && persons[i].getMotherID().equals(initialPerson.getId()) || persons[i].getFatherID() != null && persons[i].getFatherID().equals(initialPerson.getId())) {
                currentPersons.put(persons[i], "Child");
            } else if (initialPerson.getMotherID() != null && initialPerson.getMotherID().equals(persons[i].getId())) {
                currentPersons.put(persons[i], "Mother");
            } else if (initialPerson.getFatherID() != null && initialPerson.getFatherID().equals(persons[i].getId())) {
                currentPersons.put(persons[i], "Father");
            } else if (initialPerson.getSpouseID() != null && initialPerson.getSpouseID().equals(persons[i].getId())) {
                currentPersons.put(persons[i], "Spouse");
            }
        }
        return currentPersons;
    }

    public static Model getInstance() {
        if(instance == null) {
            instance = new Model();
        }

        return instance;
    }

    private Model() {}
}

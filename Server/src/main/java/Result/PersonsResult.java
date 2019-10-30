package Result;

import java.util.ArrayList;

import Model.Person;

public class PersonsResult {
    private ArrayList<Person> persons;

    public PersonsResult(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}

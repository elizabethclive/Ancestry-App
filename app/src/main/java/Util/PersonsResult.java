package Util;

//public class PersonsResult extends Result {
//
//    public PersonsResult(boolean success, String message) {
//        super(success, message);
//    }
//}

import Model.Person;

public class PersonsResult {
    private Person[] data;
    String message;

    public PersonsResult(String message) {
        this.message = message;
    }

    public PersonsResult(Person[] data) {
        this.data = data;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
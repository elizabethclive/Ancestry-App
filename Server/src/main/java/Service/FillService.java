package Service;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Random;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Result.FillResult;
import Util.Location;
import Util.LocationsList;
import Util.NamesList;

public class FillService {
    private String[] femaleNames;
    private String[] maleNames;
    private String[] surNames;
    private Location[] locations;
    private int numPersons = 0;
    private int numEvents = 0;


    /**
     * Fills stuff.
     * @param request FillRequest
     * @return FillResult to the user
     */
    public FillResult fill(FillRequest request) throws Exception {
        if (request.getGenerations() < 0) {
            return new FillResult(false, "Error: Invalid number of generations"); // ??????? spec says 0 gen means just create user
        }

        Database db = new Database();
        createLists();
        try {
            Connection conn = db.openConnection();

            UserDAO uDao = new UserDAO(conn);
            User user = uDao.readUser(request.getUsername());
            if (user == null) {
                db.closeConnection(false);
                return new FillResult(false, "Error: Incorrect username");
            }

            PersonDAO pDao = new PersonDAO(conn);
            pDao.deleteAssociatedPeople(request.getUsername());

            EventDAO eDao = new EventDAO(conn);

            Person person = new Person(RandomString.getRandomString(), user.getFirstName(), user.getLastName(), user.getGender(),
                    RandomString.getRandomString(), RandomString.getRandomString(), null, request.getUsername());
            pDao.createPerson(person);
            numPersons++;
            addParents(pDao, person, request.getGenerations(), 1980, eDao);
            Location birthLocation = getRandomLocation();
            Location baptismLocation = getRandomLocation();
            Event birth = new Event(RandomString.getRandomString(), person.getUsername(), person.getId(), birthLocation.getLatitude(),
                    birthLocation.getLongitude(), birthLocation.getCountry(), birthLocation.getCity(), "birth", 2000);
            Event christening = new Event(RandomString.getRandomString(), person.getUsername(), person.getId(), birthLocation.getLatitude(),
                    birthLocation.getLongitude(), birthLocation.getCountry(), birthLocation.getCity(), "christening", 2000);
            Event baptism = new Event(RandomString.getRandomString(), person.getUsername(), person.getId(), baptismLocation.getLatitude(),
                    baptismLocation.getLongitude(), baptismLocation.getCountry(), baptismLocation.getCity(), "baptism", 2008);
            eDao.createEvent(birth);
            eDao.createEvent(christening);
            eDao.createEvent(baptism);
            numEvents += 3;




            db.closeConnection(true);

            return new FillResult(true, "Successfully added " + numPersons + " persons and " + numEvents + " events to the database.");
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new FillResult(false, e.getMessage());
        }



    };

    private void createLists() throws Exception {
        String path = "C:/Users/lizzy/Downloads/familymapserver/familymapserver/json/";
        Gson gson = new Gson();

        File file = new File(path + "fnames.json");
        FileReader reader = new FileReader(file);
        femaleNames = gson.fromJson(reader, NamesList.class).getData();

        file = new File(path + "mnames.json");
        reader = new FileReader(file);
        maleNames = gson.fromJson(reader, NamesList.class).getData();

        file = new File(path + "snames.json");
        reader = new FileReader(file);
        surNames = gson.fromJson(reader, NamesList.class).getData();

        file = new File(path + "locations.json");
        reader = new FileReader(file);
        locations = gson.fromJson(reader, LocationsList.class).getData();
    }

    private void addParents(PersonDAO pDao, Person person, int generations, int birthYear, EventDAO eDao) {
        if (generations == 0) {
            return;
        }
        try {
            Person mother = new Person(person.getMotherID(), getRandomElement(femaleNames), getRandomElement(surNames), "F",
                    RandomString.getRandomString(), RandomString.getRandomString(), person.getFatherID(), person.getUsername());
            pDao.createPerson(mother);
            Person father = new Person(person.getFatherID(), getRandomElement(femaleNames), getRandomElement(surNames), "M",
                    RandomString.getRandomString(), RandomString.getRandomString(), person.getMotherID(), person.getUsername());
            pDao.createPerson(father);
            numPersons += 2;
            addParents(pDao, mother, generations - 1, birthYear-20, eDao);
            addParents(pDao, father, generations - 1, birthYear-20, eDao);
            addEvents(mother, father, birthYear, eDao);
        } catch (DataAccessException e) {
            return; // HELP
        }
    }

    private void addEvents(Person mother, Person father, int birthYear, EventDAO eDao) {
        Location birthLocation = getRandomLocation();
        Location marriageLocation = getRandomLocation();
        Location deathLocation = getRandomLocation();
        int marriageYear = birthYear + 19;
        int deathYear = birthYear + 80;
        if (deathYear > 2019) deathYear = 2018;
        Event motherBirth = new Event(RandomString.getRandomString(), mother.getUsername(), mother.getId(), birthLocation.getLatitude(),
                birthLocation.getLongitude(), birthLocation.getCountry(), birthLocation.getCity(), "birth", birthYear);
        Event fatherBirth = new Event(RandomString.getRandomString(), father.getUsername(), father.getId(), birthLocation.getLatitude(),
                birthLocation.getLongitude(), birthLocation.getCountry(), birthLocation.getCity(), "birth", birthYear);
        Event motherMarriage = new Event(RandomString.getRandomString(), mother.getUsername(), mother.getId(), marriageLocation.getLatitude(),
                marriageLocation.getLongitude(), marriageLocation.getCountry(), marriageLocation.getCity(), "marriage", marriageYear);
        Event fatherMarriage = new Event(RandomString.getRandomString(), father.getUsername(), father.getId(), marriageLocation.getLatitude(),
                marriageLocation.getLongitude(), marriageLocation.getCountry(), marriageLocation.getCity(), "marriage", marriageYear);
        Event motherDeath = new Event(RandomString.getRandomString(), mother.getUsername(), mother.getId(), deathLocation.getLatitude(),
                deathLocation.getLongitude(), deathLocation.getCountry(), deathLocation.getCity(), "death", deathYear);
        Event fatherDeath = new Event(RandomString.getRandomString(), father.getUsername(), father.getId(), deathLocation.getLatitude(),
                deathLocation.getLongitude(), deathLocation.getCountry(), deathLocation.getCity(), "death", deathYear);
        try {
            eDao.createEvent(motherBirth);
            eDao.createEvent(fatherBirth);
            eDao.createEvent(motherMarriage);
            eDao.createEvent(fatherMarriage);
            eDao.createEvent(motherDeath);
            eDao.createEvent(fatherDeath);
        } catch (DataAccessException e) {
            return; //HELP
        }
        numEvents += 6;
    }

    public String getRandomElement(String[] list) {
        Random random = new Random();
        int index = random.nextInt(list.length);
        return list[index];
    }

    public Location getRandomLocation() {
        Random random = new Random();
        int index = random.nextInt(locations.length);
        return locations[index];
    }
}

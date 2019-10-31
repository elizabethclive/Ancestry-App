package Service;

import java.util.UUID;

public class RandomString {
    public static String getRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}

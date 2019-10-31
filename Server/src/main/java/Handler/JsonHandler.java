package Handler;

import com.google.gson.Gson;

public class JsonHandler {

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }

    public static <T> String serialize(T value) {
        return (new Gson()).toJson(value);
    }
}


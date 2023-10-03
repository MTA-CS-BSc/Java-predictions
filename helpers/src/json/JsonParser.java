package json;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonParser {
    private static String primitiveToJson(String key, Object data) throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put(key, data);
        return SingletonObjectMapper.objectMapper.writeValueAsString(json);
    }

    public static String toJson(String key, String data) throws JsonProcessingException {
        return primitiveToJson(key, data);
    }

    public static String toJson(String key, Number data) throws JsonProcessingException {
        return primitiveToJson(key, data);
    }

    public static String getJsonFromReader(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            requestBody.append(line);

        return requestBody.toString();
    }
}

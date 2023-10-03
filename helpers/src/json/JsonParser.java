package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonParser {
    public static String toJson(Map<String, Object> map) throws JsonProcessingException {
        return SingletonObjectMapper.objectMapper.writeValueAsString(map);
    }

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

    private static String getJsonStringFromReader(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            requestBody.append(line);

        return requestBody.toString();
    }

    public static Map<String, Object> getRequestBodyMap(BufferedReader reader) throws IOException {
        return getMapFromJsonString(getJsonStringFromReader(reader));
    }

    public static Map<String, Object> getMapFromJsonString(String json) throws JsonProcessingException {
        return SingletonObjectMapper.objectMapper.convertValue(getJsonNodeFromString(json), new TypeReference<Map<String, Object>>(){});
    }

    private static JsonNode getJsonNodeFromString(String json) throws JsonProcessingException {
        return SingletonObjectMapper.objectMapper.readTree(json);
    }
}

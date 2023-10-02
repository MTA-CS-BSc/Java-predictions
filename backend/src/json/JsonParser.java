package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import modules.SingletonObjectMapper;

import java.util.HashMap;
import java.util.Map;

public abstract class JsonParser {
    public static String toJson(Map<String, Object> map) throws JsonProcessingException {
        return SingletonObjectMapper.objectMapper.writeValueAsString(map);
    }

    public static String toJson(String key, Object data) throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put(key, data);
        return SingletonObjectMapper.objectMapper.writeValueAsString(json);
    }
}

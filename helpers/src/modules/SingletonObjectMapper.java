package modules;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SingletonObjectMapper {
    public static final ObjectMapper objectMapper = new ObjectMapper();
}

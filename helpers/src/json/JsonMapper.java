package json;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonMapper {
    public static final ObjectMapper objectMapper = new ObjectMapper();
}

package config;

import api.EngineAPI;

public abstract class Configuration {
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final EngineAPI api = new EngineAPI();
}

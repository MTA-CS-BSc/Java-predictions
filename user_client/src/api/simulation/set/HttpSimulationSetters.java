package api.simulation.set;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpSimulationSetters {
    private static final String SET_ENTITY_POPULATION_URL = Configuration.SERVER_URL + Routes.SET_ENTITY_INIT_POPULATION;
    private static final String SET_ENV_PROP_VALUE_URL = Configuration.SERVER_URL + Routes.SET_ENV_PROP_VALUE;

    public static Response setEntityInitialPopulation(String simulationUuid, String entityName, Integer population) throws IOException {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(Keys.UUID_KEY, simulationUuid);
        bodyMap.put(Keys.ENTITY_NAME_KEY, entityName);
        bodyMap.put(Keys.VALUE_KEY, population);

        Request request = new Request.Builder()
                .url(SET_ENTITY_POPULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(bodyMap), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response setEnvironmentProperty(String simulationUuid, String propertyName, Integer value) throws IOException {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(Keys.UUID_KEY, simulationUuid);
        bodyMap.put(Keys.PROPERTY_NAME_KEY, propertyName);
        bodyMap.put(Keys.VALUE_KEY, value);

        Request request = new Request.Builder()
                .url(SET_ENV_PROP_VALUE_URL)
                .put(RequestBody.create(JsonParser.toJson(bodyMap), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

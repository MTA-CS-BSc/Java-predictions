package api.simulation;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public abstract class HttpSimulation {
    private static final String CREATE_SIMULATION_URL = Configuration.SERVER_URL + Routes.CREATE_SIMULATION;

    public static Response createSimulation(String requestUuid) throws IOException  {
        Request request = new Request.Builder()
                .url(CREATE_SIMULATION_URL)
                .post(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, requestUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response enqueueSimulation(String simulationUuid) throws IOException  {
        Request request = new Request.Builder()
                .url(CREATE_SIMULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, simulationUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

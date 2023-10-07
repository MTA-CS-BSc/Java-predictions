package api.simulation;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import types.ByStep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class HttpSimulation {
    private static final String CREATE_SIMULATION_URL = Configuration.SERVER_URL + Routes.CREATE_SIMULATION;
    private static final String STOP_SIMULATION_URL = Configuration.SERVER_URL + Routes.STOP_SIMULATION;
    private static final String PAUSE_SIMULATION_URL = Configuration.SERVER_URL + Routes.PAUSE_SIMULATION;
    private static final String RESUME_SIMULATION_URL = Configuration.SERVER_URL + Routes.RESUME_SIMULATION;
    private static final String ENQUEUE_SIMULATION_URL = Configuration.SERVER_URL + Routes.ENQUEUE_SIMULATION;
    private static final String GET_CREATING_SIMULATION = Configuration.SERVER_URL + Routes.CREATING_SIMULATION;
    private static final String SET_BY_STEP_URL = Configuration.SERVER_URL + Routes.SET_BY_STEP;

    public static Response getCreatingSimulation(String simulationUuid) throws IOException {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(GET_CREATING_SIMULATION)).newBuilder();
        httpBuilder.addQueryParameter(Keys.UUID_KEY, simulationUuid);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response createSimulation(String requestUuid) throws IOException  {
        Request request = new Request.Builder()
                .url(CREATE_SIMULATION_URL)
                .post(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, requestUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response enqueueSimulation(String simulationUuid) throws IOException  {
        Request request = new Request.Builder()
                .url(ENQUEUE_SIMULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, simulationUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response stopSimulation(String simulationUuid) throws IOException {
        Request request = new Request.Builder()
                .url(STOP_SIMULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, simulationUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response resumeSimulation(String simulationUuid) throws IOException {
        Request request = new Request.Builder()
                .url(RESUME_SIMULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, simulationUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response pauseSimulation(String simulationUuid) throws IOException {
        Request request = new Request.Builder()
                .url(PAUSE_SIMULATION_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, simulationUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response setByStep(String simulationUuid, ByStep byStep) throws IOException {
        Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put(Keys.UUID_KEY, simulationUuid);
        bodyMap.put(Keys.VALUE_KEY, byStep);

        Request request = new Request.Builder()
                .url(SET_BY_STEP_URL)
                .put(RequestBody.create(JsonParser.toJson(bodyMap), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

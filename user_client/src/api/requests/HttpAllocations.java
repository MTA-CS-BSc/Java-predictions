package api.requests;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import other.TerminationDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class HttpAllocations {
    public static final String USER_ALLOCATIONS_URL = Configuration.SERVER_URL + Routes.ALLOCATIONS_HISTORY_FOR_USER;
    public static final String ALLOCATIONS_URL = Configuration.SERVER_URL + Routes.ALLOCATIONS_HISTORY;

    public static Response createAllocationRequest(String createdUser,
                                                   String initialWorldName,
                                                   int executionsAmount,
                                                   TerminationDTO termination) throws IOException {
        Map<String, Object> body = new HashMap<>();

        body.put(Keys.CREATED_USER_KEY, createdUser);
        body.put(Keys.INITIAL_WORLD_NAME_KEY, initialWorldName);
        body.put(Keys.REQUESTED_EXECUTIONS_KEY, executionsAmount);
        body.put(Keys.TERMINATION_KEY, JsonParser.toJson(termination));

        Request request = new Request.Builder()
                .url(ALLOCATIONS_URL)
                .post(RequestBody.create(JsonParser.toJson(body), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }


    public static Response getAllocationsRequests(String username) throws IOException {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(USER_ALLOCATIONS_URL)).newBuilder();
        httpBuilder.addQueryParameter(Keys.USERNAME_KEY, username);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

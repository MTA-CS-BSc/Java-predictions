package api.history.allocations;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import other.TerminationDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpAllocations {
    public static final String ALLOCATIONS_URL = Configuration.SERVER_URL + Routes.ALLOCATIONS_HISTORY;

    public static Response getAllocationsRequests() throws IOException {
        Request request = new Request.Builder()
                .url(ALLOCATIONS_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response createAllocationRequest(String createdUser,
                                                   String fromInitialWorld,
                                                   int executionsAmount,
                                                   TerminationDTO termination) throws IOException {
        Map<String, Object> body = new HashMap<>();

        body.put(Keys.CREATED_USER_KEY, createdUser);
        body.put(Keys.INITIAL_WORLD_NAME_KEY, fromInitialWorld);
        body.put(Keys.REQUESTED_EXECUTIONS_KEY, executionsAmount);
        body.put(Keys.TERMINATION_KEY, termination);

        Request request = new Request.Builder()
                .url(ALLOCATIONS_URL)
                .post(RequestBody.create(JsonParser.toJson(body), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

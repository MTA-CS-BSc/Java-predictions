package api.allocations;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public abstract class HttpAllocations {
    public static final String ALLOCATIONS_URL = Configuration.SERVER_URL + Routes.ALLOCATIONS_HISTORY;
    public static final String APPROVE_REQUEST_URL = Configuration.SERVER_URL + Routes.APPROVE_REQUEST;
    public static final String DECLINE_REQUEST_URL = Configuration.SERVER_URL + Routes.DECLINE_REQUEST;

    public static Response getAllocationsRequests() throws IOException {
        Request request = new Request.Builder()
                .url(ALLOCATIONS_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response approveRequest(String requestUuid) throws IOException {
        Request request = new Request.Builder()
                .url(APPROVE_REQUEST_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, requestUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response declineRequest(String requestUuid) throws IOException {
        Request request = new Request.Builder()
                .url(DECLINE_REQUEST_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.UUID_KEY, requestUuid), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

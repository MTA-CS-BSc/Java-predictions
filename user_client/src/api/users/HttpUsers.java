package api.users;

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

public abstract class HttpUsers {
    private static final String CREATE_USER_URL = Configuration.SERVER_URL + Routes.CREATE_USER;
    private static final String SET_IS_CONNECTED_URL = Configuration.SERVER_URL + Routes.SET_IS_CONNECTED;

    public static Response createUser(String username, boolean isConnected) throws IOException {
        HashMap<String, Object> bodyMap = new HashMap<>();

        bodyMap.put(Keys.USERNAME_KEY, username);
        bodyMap.put(Keys.IS_CONNECTED_KEY, isConnected);

        Request request = new Request.Builder()
                .url(CREATE_USER_URL)
                .post(RequestBody.create(JsonParser.toJson(bodyMap), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response setIsConnected(String username, boolean isConnected) throws IOException {
        HashMap<String, Object> bodyMap = new HashMap<>();

        bodyMap.put(Keys.USERNAME_KEY, username);
        bodyMap.put(Keys.IS_CONNECTED_KEY, isConnected);

        Request request = new Request.Builder()
                .url(SET_IS_CONNECTED_URL)
                .put(RequestBody.create(JsonParser.toJson(bodyMap), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

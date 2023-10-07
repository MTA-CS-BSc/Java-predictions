package api.history;

import api.Configuration;
import api.Routes;
import json.Keys;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public abstract class HttpUserPastSimulations {
    public static final String USER_PAST_SIMULATIONS_URL = Configuration.SERVER_URL + Routes.SIMULATIONS_HISTORY_FOR_USER;

    public static Response getUserPastSimulations(String username) throws IOException {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(USER_PAST_SIMULATIONS_URL)).newBuilder();
        httpBuilder.addQueryParameter(Keys.USERNAME_KEY, username);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

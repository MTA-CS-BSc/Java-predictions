package api.results.stats.entities;

import api.Configuration;
import api.Routes;
import json.Keys;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public abstract class HttpEntitiesStats {
    private static final String ENTITIES_PER_TICK_URL = Configuration.SERVER_URL + Routes.ENTITIES_AMOUNTS_PER_TICK;

    public static Response getEntitiesAmountsPerTick(String simulationUuid) throws IOException {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(ENTITIES_PER_TICK_URL)).newBuilder();
        httpBuilder.addQueryParameter(Keys.UUID_KEY, simulationUuid);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

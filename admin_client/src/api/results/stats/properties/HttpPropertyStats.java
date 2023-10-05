package api.results.stats.properties;

import api.Configuration;
import api.Routes;
import json.Keys;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public abstract class HttpPropertyStats {
    private static final String CONSISTENCY_URL = Configuration.SERVER_URL + Routes.PROPERTY_CONSISTENCY;
    private static final String AVERAGE_URL = Configuration.SERVER_URL + Routes.PROPERTY_AVG;
    private static final String ENTITIES_COUNT_URL = Configuration.SERVER_URL + Routes.ENTITIES_COUNT;

    private static HttpUrl getUrlWithParams(String url, String uuid, String entityName, String propertyName) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

        httpBuilder.addQueryParameter(Keys.UUID_KEY, uuid);
        httpBuilder.addQueryParameter(Keys.ENTITY_NAME_KEY, entityName);
        httpBuilder.addQueryParameter(Keys.PROPERTY_NAME_KEY, propertyName);

        return httpBuilder.build();
    }

    public static Response getConsistency(String uuid, String entityName, String propertyName) throws IOException {
        Request request = new Request.Builder()
                .url(getUrlWithParams(CONSISTENCY_URL, uuid, entityName, propertyName))
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response getAverage(String uuid, String entityName, String propertyName) throws IOException {
        Request request = new Request.Builder()
                .url(getUrlWithParams(AVERAGE_URL, uuid, entityName, propertyName))
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws IOException {
        Request request = new Request.Builder()
                .url(getUrlWithParams(ENTITIES_COUNT_URL, uuid, entityName, propertyName))
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

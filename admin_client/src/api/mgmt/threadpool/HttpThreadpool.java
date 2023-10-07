package api.mgmt.threadpool;

import api.Configuration;
import api.Routes;
import consts.API;
import json.JsonParser;
import json.Keys;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public abstract class HttpThreadpool {
    private static final String THREADPOOL_URL = Configuration.SERVER_URL + Routes.THREADPOOL;
    private static final String THREADPOOL_QUEUE_URL = Configuration.SERVER_URL + Routes.THREADPOOL_QUEUE;

    public static Response getThreadsAmount() throws IOException {
        Request request = new Request.Builder()
                .url(THREADPOOL_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response setThreadsAmount(int amount) throws IOException {
        Request request = new Request.Builder()
                .url(THREADPOOL_URL)
                .put(RequestBody.create(JsonParser.toJson(Keys.THREADS_AMOUNT_KEY, amount), API.JSON_MEDIA_TYPE))
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }

    public static Response getThreadpoolQueueData() throws IOException {
        Request request = new Request.Builder()
                .url(THREADPOOL_QUEUE_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

package api;

import okhttp3.OkHttpClient;

public abstract class Configuration {
    public final static String SERVER_URL = "http://localhost:8080/backend";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
}

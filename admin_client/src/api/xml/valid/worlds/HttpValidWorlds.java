package api.xml.valid.worlds;

import api.Configuration;
import api.Routes;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class HttpValidWorlds {
    private static final String VALID_WORLDS_URL = Configuration.SERVER_URL + Routes.XML_VALID_WORLDS;

    public static Response getValidWorlds() throws IOException {
        Request request = new Request.Builder()
                .url(VALID_WORLDS_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

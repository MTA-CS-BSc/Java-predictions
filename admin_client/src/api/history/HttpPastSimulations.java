package api.history;

import api.Configuration;
import api.Routes;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class HttpPastSimulations {
    public static final String PAST_SIMULATIONS_URL = Configuration.SERVER_URL + Routes.SIMULATIONS_HISTORY;

    public static Response getPastSimulations() throws IOException {
        Request request = new Request.Builder()
                .url(PAST_SIMULATIONS_URL)
                .get()
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

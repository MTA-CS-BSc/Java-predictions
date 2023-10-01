package admin.client.api.xml.loader;

import admin.client.api.Configuration;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

public abstract class HttpXmlLoader {
    private static final String XML_LOAD_URL = Configuration.SERVER_URL + "/xml/load";

    public static Response uploadXml(File file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileField",file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(XML_LOAD_URL)
                .post(requestBody)
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

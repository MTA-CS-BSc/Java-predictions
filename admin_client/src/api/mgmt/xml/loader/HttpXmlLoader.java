package api.mgmt.xml.loader;

import api.Configuration;
import api.ApiConstants;
import api.Routes;
import json.Keys;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

public abstract class HttpXmlLoader {
    private static final String XML_LOAD_URL = Configuration.SERVER_URL + Routes.XML_LOAD;

    public static Response uploadXml(File file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Keys.XML_UPLOAD_KEY, file.getName(),
                        RequestBody.create(file, MediaType.parse(ApiConstants.XML_CONTENT_TYPE)))
                .build();

        Request request = new Request.Builder()
                .url(XML_LOAD_URL)
                .post(requestBody)
                .build();

        return Configuration.HTTP_CLIENT.newCall(request).execute();
    }
}

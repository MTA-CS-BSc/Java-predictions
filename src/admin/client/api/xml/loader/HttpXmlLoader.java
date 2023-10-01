package admin.client.api.xml.loader;

import admin.client.api.Configuration;
import dtos.ResponseDTO;
import helpers.modules.SingletonObjectMapper;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class HttpXmlLoader {
    private static final String XML_LOAD_URL = Configuration.SERVER_URL + "/xml/load";

    public static ResponseDTO uploadXml(File file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileField",file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(XML_LOAD_URL)
                .post(requestBody)
                .build();

        Response httpResponse = Configuration.HTTP_CLIENT.newCall(request).execute();

        if (!httpResponse.isSuccessful() || Objects.isNull(httpResponse.body()) || httpResponse.body().string().isEmpty())
            return new ResponseDTO(500, false, "Unknown");

        return SingletonObjectMapper.objectMapper.readValue(httpResponse.body().string(), ResponseDTO.class);
    }
}

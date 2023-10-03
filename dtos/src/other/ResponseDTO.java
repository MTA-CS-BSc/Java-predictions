package other;

import com.google.gson.Gson;
import json.SingletonObjectMapper;

public class ResponseDTO {
    protected int status;
    protected ErrorDescriptionDTO errorDescription;
    protected String data; // JSON object

    public ResponseDTO(int status) {
        this.status = status;
        errorDescription = null;
        data = null;
    }

    public ResponseDTO(int status, Object data) {
        this.status = status;
        try {
            this.data = SingletonObjectMapper.objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            this.data = null;
        }
        errorDescription = null;
    }

    public ResponseDTO(int status, Object data, String cause) {
        this.status = status;
        this.data = new Gson().toJson(data);
        this.errorDescription = new ErrorDescriptionDTO(cause);
    }

    public int getStatus() {
        return status;
    }

    public ErrorDescriptionDTO getErrorDescription() {
        return errorDescription;
    }

    public String getData() {
        return data;
    }
}

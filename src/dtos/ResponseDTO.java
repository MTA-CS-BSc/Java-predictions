package dtos;

import com.google.gson.Gson;

public class ResponseDTO {
    protected int status;
    protected ErrorDescriptionDTO errorDescription;
    protected String data;
    public ResponseDTO(int _status) {
        status = _status;
        errorDescription = null;
        data = null;
    }

    public ResponseDTO(int _status, Object _data) {
        status = _status;
        data = new Gson().toJson(_data);
        errorDescription = null;
    }

    public ResponseDTO(int _status, Object _data, String _cause) {
        status = _status;
        data = new Gson().toJson(_data);
        errorDescription = new ErrorDescriptionDTO(_cause);
    }

    public int getStatus() { return status; }
    public ErrorDescriptionDTO getErrorDescription() { return errorDescription; }
    public String getData() { return data; }
}

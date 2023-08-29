package dtos;

public class ResponseDTO {
    protected String message;
    protected int status;
    protected ErrorDescriptionDTO errorDescription;
    public ResponseDTO(String _message, int _status) {
        message = _message;
        status = _status;
        errorDescription = null;
    }

    public ResponseDTO(String _message, int _status, String _errorCause, String _exceptionType) {
        message = _message;
        status = _status;
        errorDescription = new ErrorDescriptionDTO(_errorCause, _exceptionType);
    }

    public String getMessage() { return message; }
    public int getStatus() { return status; }

    public ErrorDescriptionDTO getErrorDescription() { return errorDescription; }
}

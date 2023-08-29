package dtos;

public class ErrorDescriptionDTO {
    protected String cause;
    protected String exceptionType;

    public ErrorDescriptionDTO(String _cause, String _exceptionType) {
        cause = _cause;
        exceptionType = _exceptionType;
    }
}

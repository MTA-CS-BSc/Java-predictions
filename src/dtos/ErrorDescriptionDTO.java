package dtos;

public class ErrorDescriptionDTO {
    protected String cause;
    public ErrorDescriptionDTO(String _cause) {
        cause = _cause;
    }
    public String getCause() { return cause; }
}

package dtos;

public class ErrorDescriptionDTO {
    protected String cause;

    public ErrorDescriptionDTO(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}

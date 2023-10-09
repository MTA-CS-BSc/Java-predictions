package other;

public class UserDTO {
    protected String username;
    protected boolean isConnected;

    public UserDTO(String username, boolean isConnected) {
        this.username = username;
        this.isConnected = isConnected;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }
}

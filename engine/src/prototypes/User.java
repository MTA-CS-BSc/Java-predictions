package prototypes;

public class User {
    protected String username;
    protected boolean isConnected;

    public User(String username, boolean isConnected) {
        this.username = username;
        this.isConnected = isConnected;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean value) {
        isConnected = value;
    }
}

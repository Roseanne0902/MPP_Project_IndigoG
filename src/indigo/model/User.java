package indigo.model;


public class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String roleId;

    public User() {
    }

    public User(String userId, String username, String password,
                String email, String phoneNumber, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleId = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

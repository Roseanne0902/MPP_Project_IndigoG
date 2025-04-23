package indigo.model;

public class UserRole {
    private String roleId;
    private UserRoleEnum roleName;
    private String description;

    public UserRole(String roleId, UserRoleEnum roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRoleId() {
        return roleId;
    }

    public UserRoleEnum getRoleName() {
        return roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(UserRoleEnum roleName) {
        this.roleName = roleName;
    }
}

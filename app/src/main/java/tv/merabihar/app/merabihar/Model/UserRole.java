package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class UserRole implements Serializable {

    @SerializedName("UserRoleId")
    public int UserRoleId;

    @SerializedName("UserRoleName")
    public String UserRoleName;

    @SerializedName("UserRoleUniqueId")
    public String userRoleUniqueId;

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.UserRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return UserRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.UserRoleName = userRoleName;
    }

    public void setUserRoleUniqueId(String userRoleUniqueId) {
        this.userRoleUniqueId = userRoleUniqueId;
    }

    public String getUserRoleUniqueId() {
        return userRoleUniqueId;
    }
}
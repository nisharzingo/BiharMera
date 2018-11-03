package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public class FollowsWithMapping implements Serializable {

    @SerializedName("followMapping")
    public ProfileFollowMapping followMapping;

    @SerializedName("Followers")
    public UserProfile Followers;

    @SerializedName("Follows")
    public UserProfile Follows;

    public ProfileFollowMapping getFollowMapping() {
        return followMapping;
    }

    public void setFollowMapping(ProfileFollowMapping followMapping) {
        this.followMapping = followMapping;
    }

    public UserProfile getFollowers() {
        return Followers;
    }

    public void setFollowers(UserProfile followers) {
        Followers = followers;
    }

    public UserProfile getFollows() {
        return Follows;
    }

    public void setFollows(UserProfile follows) {
        Follows = follows;
    }
}

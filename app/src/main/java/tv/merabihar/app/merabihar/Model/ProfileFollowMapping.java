package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class ProfileFollowMapping implements Serializable {

    @SerializedName("FollowId")
    private int FollowId	;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("FollowerId")
    private int FollowerId;

    public int getFollowId() {
        return FollowId;
    }

    public void setFollowId(int followId) {
        FollowId = followId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public int getFollowerId() {
        return FollowerId;
    }

    public void setFollowerId(int followerId) {
        FollowerId = followerId;
    }
}

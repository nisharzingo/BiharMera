package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 19-11-2018.
 */

public class FollowersWithProfileData implements Serializable {

    @SerializedName("profile")
    private UserProfile profile;

    @SerializedName("Followers")
    private String Followers;

    @SerializedName("NonFollowers")
    private String NonFollowers;

    @SerializedName("Following")
    private String Following;

    @SerializedName("NonFollowing")
    private String NonFollowing;

    @SerializedName("NoOfPost")
    private String NoOfPost;

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public String getNonFollowers() {
        return NonFollowers;
    }

    public void setNonFollowers(String nonFollowers) {
        NonFollowers = nonFollowers;
    }

    public String getFollowing() {
        return Following;
    }

    public void setFollowing(String following) {
        Following = following;
    }

    public String getNonFollowing() {
        return NonFollowing;
    }

    public void setNonFollowing(String nonFollowing) {
        NonFollowing = nonFollowing;
    }

    public String getNoOfPost() {
        return NoOfPost;
    }

    public void setNoOfPost(String noOfPost) {
        NoOfPost = noOfPost;
    }
}

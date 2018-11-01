package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class FollowerNonFollowers implements Serializable {

    @SerializedName("profile")
    UserProfile profile;

    @SerializedName("Followers")
    ArrayList<UserProfile> Followers;

    @SerializedName("NonFollowers")
    ArrayList<UserProfile> NonFollowers;

    @SerializedName("Following")
    ArrayList<UserProfile> Following;

    @SerializedName("NonFollowing")
    ArrayList<UserProfile> NonFollowing;

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public ArrayList<UserProfile> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<UserProfile> followers) {
        Followers = followers;
    }

    public ArrayList<UserProfile> getNonFollowers() {
        return NonFollowers;
    }

    public void setNonFollowers(ArrayList<UserProfile> nonFollowers) {
        NonFollowers = nonFollowers;
    }

    public ArrayList<UserProfile> getFollowing() {
        return Following;
    }

    public void setFollowing(ArrayList<UserProfile> following) {
        Following = following;
    }

    public ArrayList<UserProfile> getNonFollowing() {
        return NonFollowing;
    }

    public void setNonFollowing(ArrayList<UserProfile> nonFollowing) {
        NonFollowing = nonFollowing;
    }
}

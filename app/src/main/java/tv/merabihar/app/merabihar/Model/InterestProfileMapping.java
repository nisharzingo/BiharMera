package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class InterestProfileMapping implements Serializable {

    @SerializedName("MappingId")
    private int MappingId	;

    @SerializedName("profiles")
    private UserProfile profiles;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("zingoInterest")
    private Interest zingoInterest	;

    @SerializedName("ZingoInterestId")
    private int ZingoInterestId;

    public int getMappingId() {
        return MappingId;
    }

    public void setMappingId(int mappingId) {
        MappingId = mappingId;
    }

    public UserProfile getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfile profiles) {
        this.profiles = profiles;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public Interest getZingoInterest() {
        return zingoInterest;
    }

    public void setZingoInterest(Interest zingoInterest) {
        this.zingoInterest = zingoInterest;
    }

    public int getZingoInterestId() {
        return ZingoInterestId;
    }

    public void setZingoInterestId(int zingoInterestId) {
        ZingoInterestId = zingoInterestId;
    }
}

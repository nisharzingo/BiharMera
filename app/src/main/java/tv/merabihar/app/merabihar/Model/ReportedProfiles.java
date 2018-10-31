package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ReportedProfiles implements Serializable {

    @SerializedName("ReportedId")
    public int ReportedId;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("profiles")
    public UserProfile profiles;

    @SerializedName("ReportedProfileId")
    public int ReportedProfileId;

    @SerializedName("ReportedDate")
    public String ReportedDate;

    @SerializedName("Comments")
    public String Comments;

    public int getReportedId() {
        return ReportedId;
    }

    public void setReportedId(int reportedId) {
        ReportedId = reportedId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public UserProfile getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfile profiles) {
        this.profiles = profiles;
    }

    public int getReportedProfileId() {
        return ReportedProfileId;
    }

    public void setReportedProfileId(int reportedProfileId) {
        ReportedProfileId = reportedProfileId;
    }

    public String getReportedDate() {
        return ReportedDate;
    }

    public void setReportedDate(String reportedDate) {
        ReportedDate = reportedDate;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}

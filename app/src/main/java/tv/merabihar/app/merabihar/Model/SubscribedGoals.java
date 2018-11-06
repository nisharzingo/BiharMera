package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 06-11-2018.
 */

public class SubscribedGoals implements Serializable {

    @SerializedName("SubscribedGoalId")
    public int SubscribedGoalId;

    @SerializedName("goals")
    public Goals goals;

    @SerializedName("GoalId")
    public int GoalId;

    @SerializedName("StartDate")
    public String StartDate;

    @SerializedName("EndDate")
    public String EndDate;

    @SerializedName("Status")
    public String Status;

    @SerializedName("RewardsEarned")
    public String RewardsEarned;

    @SerializedName("ExtraDescription")
    public String ExtraDescription;

    @SerializedName("ActiveDate")
    public String ActiveDate;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("profile")
    public UserProfile profile;

    public int getSubscribedGoalId() {
        return SubscribedGoalId;
    }

    public void setSubscribedGoalId(int subscribedGoalId) {
        SubscribedGoalId = subscribedGoalId;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public int getGoalId() {
        return GoalId;
    }

    public void setGoalId(int goalId) {
        GoalId = goalId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRewardsEarned() {
        return RewardsEarned;
    }

    public void setRewardsEarned(String rewardsEarned) {
        RewardsEarned = rewardsEarned;
    }

    public String getExtraDescription() {
        return ExtraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        ExtraDescription = extraDescription;
    }

    public String getActiveDate() {
        return ActiveDate;
    }

    public void setActiveDate(String activeDate) {
        ActiveDate = activeDate;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}

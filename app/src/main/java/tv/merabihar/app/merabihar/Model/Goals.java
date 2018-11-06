package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 06-11-2018.
 */

public class Goals implements Serializable {

    @SerializedName("GoalId")
    public int GoalId;

    @SerializedName("GoalName")
    public String GoalName;

    @SerializedName("Description")
    public String Description;

    @SerializedName("GoalImage")
    public String GoalImage;

    @SerializedName("Status")
    public String Status;

    public int getGoalId() {
        return GoalId;
    }

    public void setGoalId(int goalId) {
        GoalId = goalId;
    }

    public String getGoalName() {
        return GoalName;
    }

    public void setGoalName(String goalName) {
        GoalName = goalName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getGoalImage() {
        return GoalImage;
    }

    public void setGoalImage(String goalImage) {
        GoalImage = goalImage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

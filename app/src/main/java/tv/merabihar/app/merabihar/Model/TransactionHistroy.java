package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 15-11-2018.
 */

public class TransactionHistroy implements Serializable {

    @SerializedName("TransactionHistoryId")
    public int TransactionHistoryId;

    @SerializedName("profile")
    public UserProfile profile;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("Title")
    public String Title;

    @SerializedName("Description")
    public String Description;

    @SerializedName("TransactionHistoryDate")
    public String TransactionHistoryDate;

    @SerializedName("value")
    public int value;

    public int getTransactionHistoryId() {
        return TransactionHistoryId;
    }

    public void setTransactionHistoryId(int transactionHistoryId) {
        TransactionHistoryId = transactionHistoryId;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTransactionHistoryDate() {
        return TransactionHistoryDate;
    }

    public void setTransactionHistoryDate(String transactionHistoryDate) {
        TransactionHistoryDate = transactionHistoryDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

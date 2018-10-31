package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class Comments implements Serializable {

    @SerializedName("CommentId")
    public int CommentId;

    @SerializedName("CommentsDesc")
    public String CommentsDesc;

    @SerializedName("content")
    public Contents content;

    @SerializedName("ContentId")
    public int ContentId;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("CreatedBy")
    public String CreatedBy;

    @SerializedName("UpdatedBy")
    public String UpdatedBy;

    @SerializedName("CommentHistory")
    public String CommentHistory;

    @SerializedName("UpdateDate")
    public String UpdateDate;

    @SerializedName("StarRatings")
    public double StarRatings;

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public String getCommentsDesc() {
        return CommentsDesc;
    }

    public void setCommentsDesc(String commentsDesc) {
        CommentsDesc = commentsDesc;
    }

    public Contents getContent() {
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public int getContentId() {
        return ContentId;
    }

    public void setContentId(int contentId) {
        ContentId = contentId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getCommentHistory() {
        return CommentHistory;
    }

    public void setCommentHistory(String commentHistory) {
        CommentHistory = commentHistory;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public double getStarRatings() {
        return StarRatings;
    }

    public void setStarRatings(double starRatings) {
        StarRatings = starRatings;
    }
}

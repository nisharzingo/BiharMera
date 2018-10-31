package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class Contents implements Serializable {

    @SerializedName("ContentId")
    public int ContentId;

    @SerializedName("Title")
    public String Title;

    @SerializedName("Description")
    public String Description;

    @SerializedName("ContentType")
    public String ContentType;

    @SerializedName("ContentURL")
    public String ContentURL;

    @SerializedName("Thumbnail")
    public String Thumbnail;

    @SerializedName("CreatedBy")
    public String CreatedBy;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("UpdatedBy")
    public String UpdatedBy;

    @SerializedName("UpdatedDate")
    public String UpdatedDate;

    @SerializedName("Views")
    public String Views;

    @SerializedName("WatchTime")
    public String WatchTime;

    @SerializedName("CreditName")
    public String CreditName;

    @SerializedName("OriginalURL")
    public String OriginalURL;

    @SerializedName("profile")
    public UserProfile profile;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("subCategories")
    public SubCategories subCategories;

    @SerializedName("SubCategoriesId")
    public int SubCategoriesId;

    @SerializedName("likes")
    public ArrayList<Likes> likes;

    @SerializedName("commentsList")
    public ArrayList<Comments> commentsList;

    @SerializedName("contentImage")
    public ArrayList<ContentImages> contentImage;

    public int getContentId() {
        return ContentId;
    }

    public void setContentId(int contentId) {
        ContentId = contentId;
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

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContentURL() {
        return ContentURL;
    }

    public void setContentURL(String contentURL) {
        ContentURL = contentURL;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        Views = views;
    }

    public String getWatchTime() {
        return WatchTime;
    }

    public void setWatchTime(String watchTime) {
        WatchTime = watchTime;
    }

    public String getCreditName() {
        return CreditName;
    }

    public void setCreditName(String creditName) {
        CreditName = creditName;
    }

    public String getOriginalURL() {
        return OriginalURL;
    }

    public void setOriginalURL(String originalURL) {
        OriginalURL = originalURL;
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

    public SubCategories getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(SubCategories subCategories) {
        this.subCategories = subCategories;
    }

    public int getSubCategoriesId() {
        return SubCategoriesId;
    }

    public void setSubCategoriesId(int subCategoriesId) {
        SubCategoriesId = subCategoriesId;
    }

    public ArrayList<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

    public ArrayList<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    public ArrayList<ContentImages> getContentImage() {
        return contentImage;
    }

    public void setContentImage(ArrayList<ContentImages> contentImage) {
        this.contentImage = contentImage;
    }
}

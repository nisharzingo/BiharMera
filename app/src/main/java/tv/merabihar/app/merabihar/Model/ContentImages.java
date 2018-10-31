package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ContentImages implements Serializable {

    @SerializedName("ContentImagesId")
    public int ContentImagesId;

    @SerializedName("content")
    public Contents content;

    @SerializedName("ContentId")
    public int ContentId;

    @SerializedName("Images")
    public String Images;

    @SerializedName("Caption")
    public String Caption;

    @SerializedName("Credits")
    public String Credits;

    @SerializedName("Description")
    public String Description;

    public int getContentImagesId() {
        return ContentImagesId;
    }

    public void setContentImagesId(int contentImagesId) {
        ContentImagesId = contentImagesId;
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

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getCredits() {
        return Credits;
    }

    public void setCredits(String credits) {
        Credits = credits;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

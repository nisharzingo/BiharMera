package tv.merabihar.app.merabihar.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Email implements Serializable {

    @SerializedName("EmailAddress")
    public String recieverEmail;

    @SerializedName("Body")
    public String emailBody;

    @SerializedName("Subject")
    public String emailSubject;

    @SerializedName("UserName")
    public String userName;

    @SerializedName("Password")
    public String password;

    @SerializedName("FromName")
    public String fromName;

    @SerializedName("FromEmail")
    public String fromEmail;

    public String getRecieverEmail() {
        return recieverEmail;
    }

    public void setRecieverEmail(String adminEmail) {
        this.recieverEmail = adminEmail;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
}

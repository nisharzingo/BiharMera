package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;


public class UserProfile  implements Serializable {

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("Prefix")
    private String Prefix;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("Password")
    private String Password;

    @SerializedName("Gender")
    private String Gender;

    @SerializedName("Email")
    private String Email;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("UserRoleId")
    private int UserRoleId;

    @SerializedName("UserRoles")
    private UserRole UserRoles;

    @SerializedName("ProfilePhoto")
    private String ProfilePhoto;

    @SerializedName("AuthType")
    private String AuthType;

    @SerializedName("AuthId")
    private String AuthId;

    @SerializedName("SignUpDate")
    private String SignUpDate;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Views")
    private String Views;

    @SerializedName("WatchTime")
    private String WatchTime;

    @SerializedName("fitnessTracker")
    private String fitnessTracker;

    @SerializedName("contents")
    private ArrayList<Contents> contents;

    @SerializedName("comment")
    private ArrayList<Comments> comment;

    @SerializedName("ReferralCodeUsed")
    private String ReferralCodeUsed;

    @SerializedName("ReferralAmount")
    private double ReferralAmount;

    @SerializedName("ReferralCodeToUseForOtherProfile")
    private String ReferralCodeToUseForOtherProfile;

    @SerializedName("ReferralAmountForOtherProfile")
    private double ReferralAmountForOtherProfile;

    @SerializedName("WalletBalance")
    private int WalletBalance;

    @SerializedName("UsedAmount")
    private int UsedAmount;

    @SerializedName("VideoReferralCode")
    private String VideoReferralCode;

    @SerializedName("MemberType")
    private String MemberType;

    @SerializedName("ReferralCodeOfParents")
    private String ReferralCodeOfParents;

    @SerializedName("ReferralCodeOfSuperParents")
    private String ReferralCodeOfSuperParents;

    @SerializedName("reportedProfiles")
    private ArrayList<ReportedProfiles> reportedProfiles;

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        UserRoleId = userRoleId;
    }

    public UserRole getUserRoles() {
        return UserRoles;
    }

    public void setUserRoles(UserRole userRoles) {
        UserRoles = userRoles;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getAuthType() {
        return AuthType;
    }

    public void setAuthType(String authType) {
        AuthType = authType;
    }

    public String getAuthId() {
        return AuthId;
    }

    public void setAuthId(String authId) {
        AuthId = authId;
    }

    public String getSignUpDate() {
        return SignUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        SignUpDate = signUpDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getFitnessTracker() {
        return fitnessTracker;
    }

    public void setFitnessTracker(String fitnessTracker) {
        this.fitnessTracker = fitnessTracker;
    }

    public ArrayList<Contents> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Contents> contents) {
        this.contents = contents;
    }

    public ArrayList<Comments> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comments> comment) {
        this.comment = comment;
    }

    public String getReferralCodeUsed() {
        return ReferralCodeUsed;
    }

    public void setReferralCodeUsed(String referralCodeUsed) {
        ReferralCodeUsed = referralCodeUsed;
    }

    public double getReferralAmount() {
        return ReferralAmount;
    }

    public void setReferralAmount(double referralAmount) {
        ReferralAmount = referralAmount;
    }

    public String getReferralCodeToUseForOtherProfile() {
        return ReferralCodeToUseForOtherProfile;
    }

    public void setReferralCodeToUseForOtherProfile(String referralCodeToUseForOtherProfile) {
        ReferralCodeToUseForOtherProfile = referralCodeToUseForOtherProfile;
    }

    public double getReferralAmountForOtherProfile() {
        return ReferralAmountForOtherProfile;
    }

    public void setReferralAmountForOtherProfile(double referralAmountForOtherProfile) {
        ReferralAmountForOtherProfile = referralAmountForOtherProfile;
    }

    public int getWalletBalance() {
        return WalletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        WalletBalance = walletBalance;
    }

    public int getUsedAmount() {
        return UsedAmount;
    }

    public void setUsedAmount(int usedAmount) {
        UsedAmount = usedAmount;
    }

    public String getVideoReferralCode() {
        return VideoReferralCode;
    }

    public void setVideoReferralCode(String videoReferralCode) {
        VideoReferralCode = videoReferralCode;
    }

    public String getMemberType() {
        return MemberType;
    }

    public void setMemberType(String memberType) {
        MemberType = memberType;
    }

    public String getReferralCodeOfParents() {
        return ReferralCodeOfParents;
    }

    public void setReferralCodeOfParents(String referralCodeOfParents) {
        ReferralCodeOfParents = referralCodeOfParents;
    }

    public String getReferralCodeOfSuperParents() {
        return ReferralCodeOfSuperParents;
    }

    public void setReferralCodeOfSuperParents(String referralCodeOfSuperParents) {
        ReferralCodeOfSuperParents = referralCodeOfSuperParents;
    }

    public ArrayList<ReportedProfiles> getReportedProfiles() {
        return reportedProfiles;
    }

    public void setReportedProfiles(ArrayList<ReportedProfiles> reportedProfiles) {
        this.reportedProfiles = reportedProfiles;
    }

    public static Comparator compareProfile = new Comparator() {
        @Override
        public int compare(Object o, Object t1) {
            UserProfile profile = (UserProfile) o;
            UserProfile profile1 = (UserProfile) t1;
            return profile.getFullName().compareTo(profile1.getFullName());
        }
    };

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof UserProfile)) {
            return false;
        }
        UserProfile otherMember = (UserProfile)anObject;
        return otherMember.getProfileId()==(getProfileId());
    }
}

package tv.merabihar.app.merabihar.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class PreferenceHandler {

    private SharedPreferences sh;

    private PreferenceHandler() {

    }

    private PreferenceHandler(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private static PreferenceHandler instance = null;

    /**
     *
     * @param mContext
     * @return {@link PreferenceHandler}
     */
    public synchronized static PreferenceHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new PreferenceHandler(mContext);
        }
        return instance;
    }

    public void setUserId(int id)
    {
        sh.edit().putInt(Constants.USER_ID,id).apply();
    }

    public int getUserId()
    {
        return sh.getInt(Constants.USER_ID,0);
    }

    public void setVideoId(int id)
    {
        sh.edit().putInt(Constants.VIDEO_ID,id).apply();
    }

    public int getVideoId()
    {
        return sh.getInt(Constants.VIDEO_ID,0);
    }

    public void setPositionId(int id)
    {
        sh.edit().putInt(Constants.POSITION_ID,id).apply();
    }

    public int getPositionId()
    {
        return sh.getInt(Constants.POSITION_ID,0);
    }

    public void setListSize(int id)
    {
        sh.edit().putInt("List",id).apply();
    }

    public int getListSize()
    {
        return sh.getInt("List",0);
    }

    public void setTravelerId(int id)
    {
        sh.edit().putInt(Constants.Travelerr_ID,id).apply();
    }

    public int getTravelerID()
    {
        return sh.getInt(Constants.Travelerr_ID,0);
    }

    public void setToken(String token)
    {
        sh.edit().putString(Constants.TOKEN,token).apply();
    }

    public String getToken()
    {
        return sh.getString(Constants.TOKEN,"");
    }


    public void setUserName(String username)
    {
        sh.edit().putString(Constants.USER_NAME,username).apply();
    }

    public String getUserName()
    {
        return sh.getString(Constants.USER_NAME,"");
    }

    public void setReferalcode(String username)
    {
        sh.edit().putString(Constants.REFERAL_CODE,username).apply();
    }

    public String getReferalcode()
    {
        return sh.getString(Constants.REFERAL_CODE,"");
    }

    public void setPhoneNumber(String phonenumber)
    {
        sh.edit().putString(Constants.USER_PHONENUMER,phonenumber).apply();
    }

    public String getPhoneNumber()
    {
        return sh.getString(Constants.USER_PHONENUMER,"");
    }

    public void clear(){
        sh.edit().clear().apply();

    }

    public void setUserFullName(String approved)
    {
        sh.edit().putString(Constants.USER_FULL_NAME,approved).apply();
    }

    public String getUserFullName()
    {
        return sh.getString(Constants.USER_FULL_NAME,"");
    }

    public void setUserRoleUniqueID(String approved)
    {
        sh.edit().putString(Constants.USER_ROLE_UNIQUE_ID,approved).apply();
    }

    public String getUserRoleUniqueID()
    {
        return sh.getString(Constants.USER_ROLE_UNIQUE_ID,"");
    }

}

package tv.merabihar.app.merabihar.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

/**
 * Created by ZingoHotels Tech on 20-11-2018.
 */

public class DataBaseHelper  extends SQLiteOpenHelper {

    private static final int DATABASEVERSION = 2;
    private static final String DATABASENAME  = "MeraBiharDB.db";
    private static final String CONTENT_TABLE = "ContentTables";
    private static final String PROFILE_TABLE = "ProfileTables";
    private static final String CATEGORY_TABLE = "CategoryTables";
    private Context context;

    public DataBaseHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);

        this.context = context;
        System.out.println("DataBaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("inside onCreate db");



        String contentDetails = "CREATE TABLE "+CONTENT_TABLE+"(ID INTEGER PRIMARY KEY,ContentId INTEGER,Title TEXT," +
                "Description TEXT,ContentType TEXT,ContentURL TEXT,Thumbnail TEXT,CreatedBy TEXT," +
                "CreatedDate TEXT,UpdatedBy TEXT,UpdatedDate TEXT,Views TEXT,"+
                "WatchTime TEXT,CreditName TEXT,OriginalURL TEXT,ProfileId INTEGER,"+
                "SubCategoriesId INTEGER,"+
                " FOREIGN KEY(ProfileId) REFERENCES "+PROFILE_TABLE+"(ProfileId))";

        String categoryDetails = "CREATE TABLE "+CATEGORY_TABLE+"(ID INTEGER PRIMARY KEY,CategoriesId INTEGER,CategoriesName TEXT," +
                "CityId INTEGER,"+
                "Description TEXT,CategoriesImage TEXT,Reviews TEXT,StarRating REAL,OrderNo INTEGER)";


        String profileDetails = "CREATE TABLE "+PROFILE_TABLE+"(ID INTEGER,ProfileId INTEGER PRIMARY KEY,Prefix TEXT," +
                "FullName TEXT,"+
                "Password TEXT,Gender TEXT,Email TEXT,PhoneNumber TEXT,UserRoleId INTEGER,"+
                "ProfilePhoto TEXT,AuthType TEXT,AuthId TEXT,SignUpDate TEXT,Status TEXT,Views TEXT," +
                "WatchTime TEXT,ReferralCodeUsed TEXT,ReferralAmount REAL,ReferralCodeToUseForOtherProfile TEXT," +
                "ReferralAmountForOtherProfile REAL,WalletBalance INTEGER,UsedAmount INTEGER," +
                "VideoReferralCode TEXT,MemberType TEXT,ReferralCodeOfParents TEXT,ReferralCodeOfSuperParents TEXT)";

        db.execSQL(contentDetails);
        db.execSQL(profileDetails);
        db.execSQL(categoryDetails);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("inside onUpgrade db");
        db.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        onCreate(db);
    }


    public void addContents(Contents contents) {
        System.out.println("addContents");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ContentId", contents.getContentId());
        values.put("Title", contents.getTitle());
        values.put("Description", contents.getDescription());
        values.put("ContentType", contents.getContentType());
        values.put("ContentURL", contents.getContentURL());
        values.put("Thumbnail", contents.getThumbnail());
        values.put("CreatedBy", contents.getCreatedBy());
        values.put("CreatedDate", contents.getCreatedDate());
        values.put("UpdatedBy", contents.getUpdatedBy());
        values.put("UpdatedDate", contents.getUpdatedDate());
        values.put("Views", contents.getViews());
        values.put("WatchTime", contents.getWatchTime());
        values.put("CreditName", contents.getCreditName());
        values.put("OriginalURL", contents.getOriginalURL());
        values.put("ProfileId", contents.getProfileId());
        values.put("SubCategoriesId", contents.getSubCategoriesId());

        // Inserting Row
        db.insert(CONTENT_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Contents> getContents() {
        System.out.println("getContents");
        try{
            String selectQuery = "SELECT  * FROM " + CONTENT_TABLE;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Contents> data = null;

            if(cursor != null && cursor.moveToFirst())
            {
                data = new ArrayList<>();

                while (cursor.isAfterLast() == false) {

                    Contents contents = new Contents();
                    String id = ""+cursor.getInt(0);


                    contents.setContentId(cursor.getInt(1));
                    contents.setTitle(cursor.getString(2));
                    contents.setDescription(cursor.getString(3));
                    contents.setContentType(cursor.getString(4));
                    contents.setContentURL(cursor.getString(5));
                    contents.setThumbnail(cursor.getString(6));
                    contents.setCreatedBy(cursor.getString(7));
                    contents.setCreatedDate(cursor.getString(8));
                    contents.setUpdatedBy(cursor.getString(9));
                    contents.setUpdatedDate(cursor.getString(10));
                    contents.setViews(cursor.getString(11));
                    contents.setWatchTime(cursor.getString(12));
                    contents.setCreditName(cursor.getString(13));
                    contents.setOriginalURL(cursor.getString(14));
                    contents.setProfileId(cursor.getInt(15));
                    contents.setSubCategoriesId(cursor.getInt(16));

                    data.add(contents);
                    cursor.moveToNext();
                }
                cursor.close();
            }

            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    public Contents getContentById(int contentID) {

        try {
            String selectQuery = "SELECT  * FROM " + CONTENT_TABLE;

            SQLiteDatabase db = this.getReadableDatabase();
            //Cursor cursor = db.rawQuery(selectQuery, null);
            Cursor cursor = db.query(CONTENT_TABLE, null, "ContentId = "+contentID, null, null, null, null);


            Contents contents = null;

            if (cursor != null && cursor.moveToFirst())
            {


                contents = new Contents();
                String id = ""+cursor.getInt(cursor.getColumnIndex("ID"));


                contents.setContentId(cursor.getInt(1));
                contents.setTitle(cursor.getString(2));
                contents.setDescription(cursor.getString(3));
                contents.setContentType(cursor.getString(4));
                contents.setContentURL(cursor.getString(5));
                contents.setThumbnail(cursor.getString(6));
                contents.setCreatedBy(cursor.getString(7));
                contents.setCreatedDate(cursor.getString(8));
                contents.setUpdatedBy(cursor.getString(9));
                contents.setUpdatedDate(cursor.getString(10));
                contents.setViews(cursor.getString(11));
                contents.setWatchTime(cursor.getString(12));
                contents.setCreditName(cursor.getString(13));
                contents.setOriginalURL(cursor.getString(14));
                contents.setProfileId(cursor.getInt(15));
                contents.setSubCategoriesId(cursor.getInt(16));
            }
            cursor.close();
            return contents;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    public ArrayList<Contents> getContentByType(String type) {

        try{
            String selectQuery = "SELECT  * FROM " + CONTENT_TABLE+" WHERE ContentType ="+type;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(CONTENT_TABLE,null, "ContentType=?",
                    new String[] {type}, null, null, null);




            ArrayList<Contents> data = new ArrayList<>();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {


                Contents contents = new Contents();
                String id = ""+cursor.getInt(cursor.getColumnIndex("ID"));

                contents.setContentId(cursor.getInt(1));
                contents.setTitle(cursor.getString(2));
                contents.setDescription(cursor.getString(3));
                contents.setContentType(cursor.getString(4));
                contents.setContentURL(cursor.getString(5));
                contents.setThumbnail(cursor.getString(6));
                contents.setCreatedBy(cursor.getString(7));
                contents.setCreatedDate(cursor.getString(8));
                contents.setUpdatedBy(cursor.getString(9));
                contents.setUpdatedDate(cursor.getString(10));
                contents.setViews(cursor.getString(11));
                contents.setWatchTime(cursor.getString(12));
                contents.setCreditName(cursor.getString(13));
                contents.setOriginalURL(cursor.getString(14));
                contents.setProfileId(cursor.getInt(15));
                contents.setSubCategoriesId(cursor.getInt(16));

                data.add(contents);
                cursor.moveToNext();
            }
            cursor.close();
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    public ArrayList<Contents> getContentByProfileId(int profileId) {

        try{
            String selectQuery = "SELECT  * FROM " + CONTENT_TABLE+" WHERE ProfileId ="+profileId;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(CONTENT_TABLE, null, "ProfileId = "+profileId, null, null, null, null);


            ArrayList<Contents> data = new ArrayList<>();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {


                Contents contents = new Contents();
                String id = ""+cursor.getInt(cursor.getColumnIndex("ID"));

                contents.setContentId(cursor.getInt(1));
                contents.setTitle(cursor.getString(2));
                contents.setDescription(cursor.getString(3));
                contents.setContentType(cursor.getString(4));
                contents.setContentURL(cursor.getString(5));
                contents.setThumbnail(cursor.getString(6));
                contents.setCreatedBy(cursor.getString(7));
                contents.setCreatedDate(cursor.getString(8));
                contents.setUpdatedBy(cursor.getString(9));
                contents.setUpdatedDate(cursor.getString(10));
                contents.setViews(cursor.getString(11));
                contents.setWatchTime(cursor.getString(12));
                contents.setCreditName(cursor.getString(13));
                contents.setOriginalURL(cursor.getString(14));
                contents.setProfileId(cursor.getInt(15));
                contents.setSubCategoriesId(cursor.getInt(16));

                data.add(contents);
                cursor.moveToNext();
            }
            cursor.close();
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    public void updateContents(Contents contents)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ContentId", contents.getContentId());
        values.put("Title", contents.getTitle());
        values.put("Description", contents.getDescription());
        values.put("ContentType", contents.getContentType());
        values.put("ContentURL", contents.getContentURL());
        values.put("Thumbnail", contents.getThumbnail());
        values.put("CreatedBy", contents.getCreatedBy());
        values.put("CreatedDate", contents.getCreatedDate());
        values.put("UpdatedBy", contents.getUpdatedBy());
        values.put("UpdatedDate", contents.getUpdatedDate());
        values.put("Views", contents.getViews());
        values.put("WatchTime", contents.getWatchTime());
        values.put("CreditName", contents.getCreditName());
        values.put("OriginalURL", contents.getOriginalURL());
        values.put("ProfileId", contents.getProfileId());
        values.put("SubCategoriesId", contents.getSubCategoriesId());

        db.update(CONTENT_TABLE, values,  "ContentId="+contents.getContentId(), null);
        db.close();
    }

    public void addCategories(Category category) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CategoriesId", category.getCategoriesId());
        values.put("CategoriesName", category.getCategoriesName());
        values.put("CityId", category.getCityId());
        values.put("Description", category.getDescription());
        values.put("CategoriesImage", category.getCategoriesImage());
        values.put("Reviews", category.getReviews());
        values.put("StarRating", category.getStarRating());
        values.put("OrderNo", category.getOrderNo());


        // Inserting Row
        db.insert(CATEGORY_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Category> getCategories() {
        System.out.println("getContents");
        try{
            String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Category> data = null;

            if(cursor != null && cursor.moveToFirst())
            {
                data = new ArrayList<>();

                while (cursor.isAfterLast() == false) {

                    Category category = new Category();
                    String id = ""+cursor.getInt(0);


                    category.setCategoriesId(cursor.getInt(1));
                    category.setCategoriesName(cursor.getString(2));
                    category.setCityId(cursor.getInt(3));
                    category.setDescription(cursor.getString(4));
                    category.setCategoriesImage(cursor.getString(5));
                    category.setReviews(cursor.getString(6));
                    category.setStarRating(cursor.getDouble(7));
                    category.setOrderNo(cursor.getInt(8));


                    data.add(category);
                    cursor.moveToNext();
                }
                cursor.close();
            }

            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    public void updateCategory(Category category)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("CategoriesId", category.getCategoriesId());
        values.put("CategoriesName", category.getCategoriesName());
        values.put("CityId", category.getCityId());
        values.put("Description", category.getDescription());
        values.put("CategoriesImage", category.getCategoriesImage());
        values.put("Reviews", category.getReviews());
        values.put("StarRating", category.getStarRating());
        values.put("OrderNo", category.getOrderNo());

        db.update(CONTENT_TABLE, values,  "CategoriesId="+category.getCategoriesId(), null);
        db.close();
    }

    public boolean exists(String table) {

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // db.query("SELECT * FROM " + table);

            String query = "SELECT * FROM "+table;
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            onCreate(db);
            return false;

        }
    }

}

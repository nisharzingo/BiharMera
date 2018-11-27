package tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.AutocompleteCustomArrayAdapter;
import tv.merabihar.app.merabihar.Adapter.SubCategoryListAdapter;
import tv.merabihar.app.merabihar.CustomViews.CustomAutoComplete;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestAndContents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.Model.InterestContentResponse;
import tv.merabihar.app.merabihar.Model.SubCategories;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabAccountActivity;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.Action;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestContentAPI;
import tv.merabihar.app.merabihar.WebAPI.SubCategoryAPI;
import tv.merabihar.app.merabihar.WebAPI.UploadApi;

public class PostContentScreen extends AppCompatActivity {

    LinearLayout mBlogImages,mUploadImages,mTaglay,mActivityLay;
    EditText mTitle,mShort,mLong,mTag;
    CustomGridView customGridView;
    CustomAutoComplete mTags;
    Button mSave;
    ImageView back;

    SubCategories activity;
    SubCategoryListAdapter adapter;

    private ArrayList<SubCategories> activities;
    private ArrayList<ContentImages> blogImagesArrayList;
    ArrayList<Interest> tlist;
    ArrayList<Integer> initerestId;
    int initerestIds = 0;



    private int activityId;
    static int REQUEST_GALLERY = 200;
    ArrayList<String> selectedImageList;
    ArrayList<String> interestList;
    int childcount = 0,count = 0,imageCount=0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_post_content_screen);

           /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("New Story");*/

            mTitle = (EditText) findViewById(R.id.blog_title);
            back = (ImageView) findViewById(R.id.back);
            mShort = (EditText) findViewById(R.id.short_desc_blog);
            mLong = (EditText) findViewById(R.id.long_desc_blog);
            mBlogImages = (LinearLayout) findViewById(R.id.blog_images);
            mUploadImages = (LinearLayout) findViewById(R.id.image_layout);
            mTaglay = (LinearLayout) findViewById(R.id.tag_layout);
            mActivityLay = (LinearLayout) findViewById(R.id.activity_list);
            mSave = (Button) findViewById(R.id.create_blogs);
            customGridView = (CustomGridView) findViewById(R.id.interest_grid_view);
            mTags = (CustomAutoComplete) findViewById(R.id.tagss_blog);


            if (Util.isNetworkAvailable(PostContentScreen.this)) {

                getActivities();
                getInterest();

            }else{

                SnackbarViewer.showSnackbar(findViewById(R.id.post_content_main),"No Internet connection");
            }


            initerestId = new ArrayList<>();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PostContentScreen.this.finish();
                }
            });

            mTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*

                    String tags = mTags.getText().toString();
                    if(tags!=null&&!tags.isEmpty()){
                        mTags.setText(tags+" "+"#"+tlist.get(position).getInterestName());
                    }else{
                        mTags.setText("#"+tlist.get(position).getInterestName());
                    }
*/

                    mTags.setText("#"+tlist.get(position).getInterestName());

                    // initerestId.add(tlist.get(position).getZingoInterestId());
                    initerestIds = tlist.get(position).getZingoInterestId();
                }
            });


           /* mUploadImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectImage();
                }
            });*/

            gotoGallery();

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  System.out.println("Selected Image List "+selectedImageList.size());

                    validate();


                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void selectImage()
    {
        try{
            //final String[] imageSelectionArray = {"Gallery","Take Photo","Cancel"};
            final String[] imageSelectionArray = {"Gallery","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(PostContentScreen.this);
            builder.setTitle("Select Photo");
            builder.setCancelable(false);
            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(imageSelectionArray[which].equals("Gallery"))
                    {
                        boolean result= Util.checkPermissionOfCamera(PostContentScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE
                                ,"This App Needs Storage Permission");
                        if(result)
                        {
                            gotoGallery();
                        }
                    }

                    else
                    {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void gotoGallery() {

        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);

        if(isAvailable(PostContentScreen.this,i)){
            startActivityForResult(i, 200);


        }else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
        }
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {

            if(requestCode == REQUEST_GALLERY)
            {
                onSelectImageFromGalleryResult(data,"Multiple");
            }else if (requestCode == SELECT_FILE){

                onSelectImageFromGalleryResult(data,"Camera");
            }

        }else{
            System.out.println("Result code "+resultCode);
        }
    }

    private void onSelectImageFromGalleryResult(Intent data,String type) {

        selectedImageList = new ArrayList<>();

        if(type!=null&&type.equalsIgnoreCase("Multiple")){
            try{
                String[] all_path = data.getStringArrayExtra("all_path");
                if(all_path.length!=0){

                    for(int i =0;i<all_path.length;i++){
                        selectedImageList.add(all_path[i]);

                    }
                }else{
                    gotoGallery();
                }
                //selectedImageList = all_path;
                mBlogImages.removeAllViews();
                for (String s:all_path)
                {
                    //System.out.println(s);
                    File imgFile = new  File(s);
                    if(imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //addView(null,Util.getResizedBitmap(myBitmap,400));
                        addView(null,Util.getResizedBitmap(myBitmap,700));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {

            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( PostContentScreen.this, selectedImageUri );
            Log.d("Picture Path", picturePath);
            String[] all_path = {picturePath};
            selectedImageList.add(all_path[0]);

            mBlogImages.removeAllViews();
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    addView(null,Util.getResizedBitmap(myBitmap,700));
                }
            }
        }




    }

    public void addView(String uri,Bitmap bitmap)
    {
        final LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.gallery_layout, null);
            ImageView blogs =(ImageView) v.findViewById(R.id.blog_images);



            if(uri != null)
            {

            }
            else if(bitmap != null)
            {
                blogs.setImageBitmap(bitmap);
            }


            mBlogImages.addView(v);
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                PostContentScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getActivities()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final SubCategoryAPI activityApi = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = activityApi.getSubCategoriesByCityId(Constants.CITY_ID);

                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {

                        if (response.code() == 200)
                        {


                            if(response.body()!=null&&response.body().size()!=0){

                                activities = response.body();

                                adapter = new SubCategoryListAdapter(PostContentScreen.this,activities);
                                customGridView.setAdapter(adapter);


                            }else{

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {


//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void  validate(){

        String title = mTitle.getText().toString();
        // String shortDesc = mShort.getText().toString();
        String longDesc = mLong.getText().toString();



       /* if(title==null||title.isEmpty()){
            Toast.makeText(PostContentScreen.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }*//*else if(shortDesc==null||shortDesc.isEmpty()){
            Toast.makeText(PostContentScreen.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }*//*if(longDesc==null||longDesc.isEmpty()){
            Toast.makeText(PostContentScreen.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }*//*else if(tags==null||tags.isEmpty()){
            Toast.makeText(PostContentScreen.this, "Tag should not be Empty", Toast.LENGTH_SHORT).show();
        }*/
        //else{

            try
            {

                if(selectedImageList != null && selectedImageList.size() !=0)
                {
                    childcount = selectedImageList.size();
                    blogImagesArrayList = new ArrayList<>();
                    System.out.println("Image    list = "+childcount);
                    for (int i=0;i<selectedImageList.size();i++)
                    {
                        LinearLayout linearLayout = (LinearLayout) mBlogImages.getChildAt(i);
                        File file = new File(selectedImageList.get(i));

                        if(file.length() <= 1*1024*1024)
                        {
                            FileOutputStream out = null;
                            String[] filearray = selectedImageList.get(i).split("/");
                            final String filename = getFilename(filearray[filearray.length-1]);

                            out = new FileOutputStream(filename);
                            Bitmap myBitmap = BitmapFactory.decodeFile(selectedImageList.get(i));

                            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                            uploadImage(filename);



                        }
                        else
                        {
                            compressImage(selectedImageList.get(i));
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        //}
    }

    public String getFilename(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        //String[] s = filePath.split(".");
        System.out.println("getFilePath = "+filePath);
        String uriSting;
        if(filePath.contains(".jpg"))
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath);
        }
        else
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath+".jpg" );
        }
        return uriSting;

    }

    private void uploadImage(final String filePath)
    {
        //String filePath = getRealPathFromURIPath(uri, ImageUploadActivity.this);

        final File file = new File(filePath);
        int size = 1*1024*1024;

        if(file != null)
        {
            if(file.length() > size)
            {
                System.out.println(file.length());
                compressImage(filePath);
            }
            else
            {
                final ProgressDialog dialog = new ProgressDialog(PostContentScreen.this);
                dialog.setCancelable(false);
                dialog.show();

                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                UploadApi uploadImage = Util.getClient().create(UploadApi.class);
                //System.out.println("Started uploading = "+System.currentTimeMillis());
                Call<String> fileUpload = uploadImage.uploadBlogImages(fileToUpload, filename);
                fileUpload.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }


                        String[] spliting = response.body().toString().split("BlogImages");
                        for (String s:spliting)
                        {
                            System.out.println("String = "+s);
                        }

                        ContentImages blogImages = new ContentImages();

                        blogImages.setImages(Constants.IMAGE_URL+response.body().toString());

                        uploadBlogs(blogImages,1);
                        if(filePath.contains("MyFolder/Images"))
                        {
                            file.delete();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("TAG ERROR", "Error " + t.getMessage());
                    }
                });
            }
        }
    }


    //public String compressImage(Uri imageUri) {
    public String compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = actualHeight/2;//2033.0f;
        float maxWidth = actualWidth/2;//1011.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String[] filearray = filePath.split("/");
        final String filename = getFilename(filearray[filearray.length-1]);
        try {
            out = new FileOutputStream(filename);


//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            uploadImage(filename);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void uploadBlogs(final ContentImages blogImages,final int i){

        imageCount = imageCount + i;
        blogImagesArrayList.add(blogImages);

        if(imageCount==selectedImageList.size()){

            if(blogImagesArrayList!=null&&blogImagesArrayList.size()!=0){

                SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

                Contents blogs = new Contents();
                blogs.setTitle("");

                if(mLong.getText().toString()!=null&&!mLong.getText().toString().isEmpty()){
                    blogs.setDescription(mLong.getText().toString());
                }else{
                    blogs.setDescription("");
                }

                blogs.setContentType("Image");
                blogs.setContentURL(blogImagesArrayList.get(0).getImages());
                blogs.setCreatedBy(PreferenceHandler.getInstance(PostContentScreen.this).getUserFullName());
                blogs.setCreatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
                blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
                blogs.setProfileId(PreferenceHandler.getInstance(PostContentScreen.this).getUserId());
                blogs.setContentImage(blogImagesArrayList);
                blogs.setSubCategoriesId(101);


                if(mTags.getText().toString()!=null||!mTags.getText().toString().isEmpty()){

                    if(interestList.contains(mTags.getText().toString())){

                        if(initerestIds!=0){

                            if (Util.isNetworkAvailable(PostContentScreen.this)) {

                                postBlogsWithInterest(blogs);

                            }else{

                                SnackbarViewer.showSnackbar(findViewById(R.id.post_content_main),"No Internet connection");
                            }

                        }else{
                            ArrayList<Interest> interests = new ArrayList<>();

                            Interest interest = new Interest();
                            interest.setInterestName(mTags.getText().toString().replace("#",""));
                            interest.setDescription(mTags.getText().toString().replace("#",""));
                            interests.add(interest);

                            InterestAndContents interestAndBlogs = new InterestAndContents();
                            interestAndBlogs.setContent(blogs);
                            interestAndBlogs.setInterestList(interests);
                            postBlogsAndNewInterest(interestAndBlogs);
                        }
                    }else{

                        ArrayList<Interest> interests = new ArrayList<>();

                        Interest interest = new Interest();
                        interest.setInterestName(mTags.getText().toString().replace("#",""));
                        interest.setDescription(mTags.getText().toString().replace("#",""));
                        interests.add(interest);

                        InterestAndContents interestAndBlogs = new InterestAndContents();
                        interestAndBlogs.setContent(blogs);
                        interestAndBlogs.setInterestList(interests);
                        postBlogsAndNewInterest(interestAndBlogs);
                    }

                }else{
                    initerestIds = 0;
                    postBlogs(blogs);
                }
                //blogs.setActivitiesId(90);


            }

        }

    }

    private void postBlogs(final Contents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = blogsAPI.postContent(blogs);
                response.enqueue(new Callback<Contents>() {
                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                           /* EmailModel emailModel = new EmailModel();
                            //emailModel.setEmailAddress("abhinav@zingohotels.com,sheetal12kamble@gmail.com");
                            emailModel.setEmailAddress("nishar@zingohotels.com");
                            emailModel.setBody("Hello,<br> Admin new Blog Added on BiharTourism App please review the blogs. <br>Title : "+response.body().getTitle()+"<br><strong>Headline :"+response.body().getShortDesc()+"</strong><br><img src=\""+response.body().getBlogImages().get(0).getImage()+"\"> <br>Added By : '+authData.FullName+'<br><br> Thank You <br>BiharTourism");
                            emailModel.setSubject("New Blog Added on Bihar Tourism");
                            emailModel.setUserName("merabihar.tv@gmail.com");
                            emailModel.setPassword("merabihar@123");
                            emailModel.setFromName("Bihar Tourism App");
                            emailModel.setFromEmail("merabihar.tv@gmail.com");
                            postEmail(emailModel);*/

                            Toast.makeText(PostContentScreen.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            success();
                        }
                        else
                        {
                            Toast.makeText(PostContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


  /*  private void postEmail(final EmailModel blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                SendEmailAPI blogsAPI = Util.getClient().create(SendEmailAPI.class);
                Call<EmailModel> response = blogsAPI.sendEmail(blogs);
                response.enqueue(new Callback<EmailModel>() {
                    @Override
                    public void onResponse(Call<EmailModel> call, Response<EmailModel> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(PostContentScreen.this,"Blog created Successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostContentScreen.this, TabMainActivity.class);
                            startActivity(intent);
                            PostContentScreen.this.finish();
                        }
                        else
                        {
                            Toast.makeText(PostContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EmailModel> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }*/

    private void postBlogsWithInterest(final Contents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = blogsAPI.postContent(blogs);
                response.enqueue(new Callback<Contents>() {
                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            InterestContentMapping interestBlogMapping = new InterestContentMapping();
                            interestBlogMapping.setContentId(response.body().getContentId());
                            interestBlogMapping.setZingoInterestId(initerestIds);
                            postBlogInterestMapping(interestBlogMapping);

                        }
                        else
                        {
                            Toast.makeText(PostContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void postBlogInterestMapping(final InterestContentMapping blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestContentAPI blogsAPI = Util.getClient().create(InterestContentAPI.class);
                Call<InterestContentMapping> response = blogsAPI.postInterestContent(blogs);
                response.enqueue(new Callback<InterestContentMapping>() {
                    @Override
                    public void onResponse(Call<InterestContentMapping> call, Response<InterestContentMapping> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(PostContentScreen.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            success();
                        }
                        else
                        {
                            Toast.makeText(PostContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InterestContentMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void postBlogsAndNewInterest(final InterestAndContents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<InterestContentResponse>> response = blogsAPI.postContentsWithMultipleNewInterest(blogs);
                response.enqueue(new Callback<ArrayList<InterestContentResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<InterestContentResponse>> call, Response<ArrayList<InterestContentResponse>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(PostContentScreen.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            success();
                        }
                        else
                        {
                            Toast.makeText(PostContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestContentResponse>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    public void getInterest()
    {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final InterestAPI categoryAPI = Util.getClient().create(InterestAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<ArrayList<Interest>> getCat = categoryAPI.getInterest();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Interest>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {


                        if(response.code() == 200)
                        {
                            tlist = response.body();

                            interestList  = new ArrayList<>();

                            if(tlist!=null&&tlist.size()!=0){
                                for(int i =0;i<tlist.size();i++){

                                    interestList.add("#"+tlist.get(i).getInterestName());
                                }
                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(PostContentScreen.this,R.layout.interest_row,tlist,"ImageScreen");
                                mTags.setThreshold(1);
                                mTags.setAdapter(autocompleteCustomArrayAdapter);
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {


//                        Toast.makeText(PostContentScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void success(){


        Intent intent = new Intent(PostContentScreen.this, TabMainActivity.class);
        startActivity(intent);
        PostContentScreen.this.finish();
    }


    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

}

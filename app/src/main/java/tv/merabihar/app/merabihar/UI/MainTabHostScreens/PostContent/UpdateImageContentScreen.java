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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.Action;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.ContentImageAPI;
import tv.merabihar.app.merabihar.WebAPI.UploadApi;

public class UpdateImageContentScreen extends AppCompatActivity {

    LinearLayout mBlogImages,mUploadImages;
    EditText mTitle,mShort,mLong,mTag;

    Button mSave;
    ImageView back;


    static int REQUEST_GALLERY = 200;
    ArrayList<String> selectedImageList;
    ArrayList<ContentImages> blogImagesArrayList;

    int childcount = 0,count = 0,imageCount=0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    Contents updatecontents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_update_image_content_screen);

            mTitle = (EditText) findViewById(R.id.blog_title);
            back = (ImageView) findViewById(R.id.back);
            mShort = (EditText) findViewById(R.id.short_desc_blog);
            mLong = (EditText) findViewById(R.id.long_desc_blog);
            mBlogImages = (LinearLayout) findViewById(R.id.blog_images);
            mUploadImages = (LinearLayout) findViewById(R.id.image_layout);
            mSave = (Button) findViewById(R.id.create_blogs);


            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){

                updatecontents = (Contents)bundle.getSerializable("UpdateContents");
            }

            if(updatecontents!=null){

                mTitle.setText(""+updatecontents.getTitle());
                mLong.setText(""+updatecontents.getDescription());

                if(updatecontents.getContentImage()!=null&&updatecontents.getContentImage().size()!=0){

                    for (ContentImages ci :updatecontents.getContentImage()) {
                        addView(ci.getImages(),null,ci.getContentImagesId());
                    }

                }
            }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UpdateImageContentScreen.this.finish();
                }
            });
            mUploadImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectImage();
                }
            });








            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  System.out.println("Selected Image List "+selectedImageList.size());

                    if(mBlogImages.getChildCount()!=0){

                        validate();

                    }else{

                        Toast.makeText(UpdateImageContentScreen.this, "Please upload any image", Toast.LENGTH_SHORT).show();
                    }



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

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImageContentScreen.this);
            builder.setTitle("Select Photo");
            builder.setCancelable(false);
            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(imageSelectionArray[which].equals("Gallery"))
                    {
                        boolean result= Util.checkPermissionOfCamera(UpdateImageContentScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE
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

        if(isAvailable(UpdateImageContentScreen.this,i)){
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
               // mBlogImages.removeAllViews();
                for (String s:all_path)
                {
                    //System.out.println(s);
                    File imgFile = new  File(s);
                    if(imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //addView(null,Util.getResizedBitmap(myBitmap,400));
                        addView(null,Util.getResizedBitmap(myBitmap,700),0);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {

            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( UpdateImageContentScreen.this, selectedImageUri );
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
                    addView(null,Util.getResizedBitmap(myBitmap,700),0);
                }
            }
        }




    }

    public void addView(String uri,Bitmap bitmap,final int id)
    {
        final LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.gallery_layout_design, null);
            ImageView blogs =(ImageView) v.findViewById(R.id.blog_images);
            ImageView close =(ImageView) v.findViewById(R.id.close);
            final TextView imageId =(TextView) v.findViewById(R.id.image_id);
            final TextView posId =(TextView) v.findViewById(R.id.posi_id);


            if(id!=0){
                imageId.setText(""+id);
            }else{
                imageId.setText("0");
            }
            if(uri != null)
            {

                if(uri.contains(" ")){
                    Picasso.with(UpdateImageContentScreen.this).load(uri.replaceAll(" ","%20")).placeholder(R.drawable.no_image).
                            error(R.drawable.no_image).into(blogs);
                }else{
                    Picasso.with(UpdateImageContentScreen.this).load(uri).placeholder(R.drawable.no_image).
                            error(R.drawable.no_image).into(blogs);

                }



            }
            else if(bitmap != null)
            {
                blogs.setImageBitmap(bitmap);
            }

            blogs.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(Integer.parseInt(imageId.getText().toString())!=0){
                        try{

                            final String[] imageSelectionArray = {"Delete","Cancel"};

                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImageContentScreen.this);

                            builder.setCancelable(false);
                            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(imageSelectionArray[which].equals("Delete"))
                                    {
                                        deleteAlertBox(Integer.parseInt(imageId.getText().toString()),Integer.parseInt(posId.getText().toString()));

                                    }else
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
                    }else{

                        mBlogImages.removeViewAt(Integer.parseInt(posId.getText().toString()));
                    }


                    return false;

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Integer.parseInt(imageId.getText().toString())!=0){
                        try{

                            final String[] imageSelectionArray = {"Delete","Cancel"};

                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImageContentScreen.this);

                            builder.setCancelable(false);
                            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(imageSelectionArray[which].equals("Delete"))
                                    {
                                        deleteAlertBox(Integer.parseInt(imageId.getText().toString()),Integer.parseInt(posId.getText().toString()));

                                    }else
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
                    }else{

                        mBlogImages.removeViewAt(Integer.parseInt(posId.getText().toString()));
                    }

                }
            });


            mBlogImages.addView(v);
            posId.setText(""+(mBlogImages.getChildCount()-1));

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
                UpdateImageContentScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void  validate(){

        String title = mTitle.getText().toString();
        // String shortDesc = mShort.getText().toString();
        String longDesc = mLong.getText().toString();

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
            }else{

                if(mBlogImages.getChildCount()!=0){
                    SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

                    Contents blogs = updatecontents;


                    if(mLong.getText().toString()!=null&&!mLong.getText().toString().isEmpty()){
                        blogs.setDescription(mLong.getText().toString());
                    }else{
                        blogs.setDescription("");
                    }



                    blogs.setUpdatedBy(PreferenceHandler.getInstance(UpdateImageContentScreen.this).getUserFullName());
                    blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));

                    updateContents(blogs);

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
                final ProgressDialog dialog = new ProgressDialog(UpdateImageContentScreen.this);
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

                        blogImages.setContentId(updatecontents.getContentId());
                        blogImages.setImages(Constants.IMAGE_URL+response.body().toString());
                        postImages(blogImages);


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

                Contents blogs = updatecontents;


                if(mLong.getText().toString()!=null&&!mLong.getText().toString().isEmpty()){
                    blogs.setDescription(mLong.getText().toString());
                }else{
                    blogs.setDescription("");
                }

                blogs.setContentType("Image");
                blogs.setContentURL(blogImagesArrayList.get(0).getImages());
                blogs.setUpdatedBy(PreferenceHandler.getInstance(UpdateImageContentScreen.this).getUserFullName());
                blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));

                updateContents(blogs);

                //blogs.setActivitiesId(90);


            }

        }

    }

    private void updateContents(final Contents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = blogsAPI.updateContent(blogs.getContentId(),blogs);
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



                            Toast.makeText(UpdateImageContentScreen.this,"Story updated Successfull",Toast.LENGTH_SHORT).show();
                            success();
                        }
                        else
                        {
                            Toast.makeText(UpdateImageContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
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


    private void postImages(final ContentImages blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentImageAPI blogsAPI = Util.getClient().create(ContentImageAPI.class);
                Call<ContentImages> response = blogsAPI.postContentImages(blogs);
                response.enqueue(new Callback<ContentImages>() {
                    @Override
                    public void onResponse(Call<ContentImages> call, Response<ContentImages> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {




                            uploadBlogs(blogs,1);
                        }
                        else
                        {
                            Toast.makeText(UpdateImageContentScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContentImages> call, Throwable t) {
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






    public void success(){


        Intent intent = new Intent(UpdateImageContentScreen.this, TabMainActivity.class);
        startActivity(intent);
        UpdateImageContentScreen.this.finish();
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

    public void deleteAlertBox(final int id,final int posiyion){

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Are you sure to Delete your image?")
                //set message
                .setMessage("After delete it will not be longer anywhere")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        deleteContents(id,posiyion);
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked

                    }
                })
                .show();
    }

    public void deleteContents(final int id,final int posiyion)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentImageAPI categoryAPI = Util.getClient().create(ContentImageAPI.class);
                Call<ContentImages> getCat = categoryAPI.deleteContentImagesById(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ContentImages>() {

                    @Override
                    public void onResponse(Call<ContentImages> call, Response<ContentImages> response) {



                        if(response.code() == 200||response.code()==201||response.code()==204)
                        {

                            mBlogImages.removeViewAt(posiyion);

                        }else{

                            Toast.makeText(UpdateImageContentScreen.this, "Unable to delete due to "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContentImages> call, Throwable t) {

                        Toast.makeText(UpdateImageContentScreen.this, "Unable to delete", Toast.LENGTH_SHORT).show();


//                        Toast.makeText(CommentsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}

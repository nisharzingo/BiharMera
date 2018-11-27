package tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.SubCategoryListAdapter;
import tv.merabihar.app.merabihar.CustomViews.CustomAutoComplete;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestAndContents;
import tv.merabihar.app.merabihar.Model.SubCategories;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

public class UpdateContentUrlScreen extends AppCompatActivity {


    EditText mTitle,mShort,mLong,mURL;

    Button mSave;
    ImageView back;



    Contents updatecontents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_update_content_url_screen);

            mTitle = (EditText) findViewById(R.id.blog_title);
            back = (ImageView) findViewById(R.id.back);
            mShort = (EditText) findViewById(R.id.short_desc_blog);
            mLong = (EditText) findViewById(R.id.long_desc_blog);
            mURL = (EditText) findViewById(R.id.youtube_url);

            mSave = (Button) findViewById(R.id.create_blogs);




            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                updatecontents = (Contents)bundle.getSerializable("UpdateContents");
            }

            if(updatecontents!=null){

                mTitle.setText(""+updatecontents.getTitle());
                mLong.setText(""+updatecontents.getDescription());
                mURL.setText("https://youtu.be/"+updatecontents.getContentURL());
            }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UpdateContentUrlScreen.this.finish();
                }
            });

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                   validate();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String extractYTId(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        String video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = youtubeUrl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (youtubeUrl.contains("youtu.be/")  ) {
                String spl = youtubeUrl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                }else {
                    video_id =spl;
                }

            }
        }

        return video_id;
    }

    public void validate(){

        String title = mTitle.getText().toString();
        String longDesc = mLong.getText().toString();

        String url = mURL.getText().toString();
        String validurl = extractYTId(url);




        if(url==null||url.isEmpty()){
            Toast.makeText(UpdateContentUrlScreen.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }else if(validurl==null||validurl.isEmpty()){
            Toast.makeText(UpdateContentUrlScreen.this, "It is not valid youtube url", Toast.LENGTH_SHORT).show();
        }else{

            try
            {

                SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

                Contents blogs =updatecontents;

                if(mLong.getText().toString()!=null&&!mLong.getText().toString().isEmpty()){
                    blogs.setDescription(mLong.getText().toString());
                }else{
                    blogs.setDescription("");
                }

                blogs.setContentType("Video");
                blogs.setContentURL(extractYTId(mURL.getText().toString()));
                blogs.setUpdatedBy(PreferenceHandler.getInstance(UpdateContentUrlScreen.this).getUserFullName());
                blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
                updateBlogs(blogs);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }





    private void updateBlogs(final Contents blogs) {

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



                            Toast.makeText(UpdateContentUrlScreen.this,"Story updated Successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateContentUrlScreen.this, TabMainActivity.class);
                            startActivity(intent);
                            UpdateContentUrlScreen.this.finish();
                        }
                        else
                        {
                            Toast.makeText(UpdateContentUrlScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
    }

}

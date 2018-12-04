package tv.merabihar.app.merabihar.YoutubeUpload;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;

import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostVideoYoutubeContent;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

public class ReviewActivity extends Activity {

    VideoView mVideoView;
    MediaController mc;
    private String mChosenAccountName;
    private Uri mFileUri;
    EditText mTitle,mDescriton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            getActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.activity_review);
            Button uploadButton = (Button) findViewById(R.id.upload_button);
            mTitle = (EditText) findViewById(R.id.title_video);
            mDescriton = (EditText) findViewById(R.id.long_desc_blog);
            Intent intent = getIntent();
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                uploadButton.setVisibility(View.GONE);
                setTitle(R.string.playing_the_video_in_upload_progress);
            }
            mFileUri = intent.getData();
            loadAccount();

            reviewVideo(mFileUri);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void reviewVideo(Uri mFileUri) {
        try {
            mVideoView = (VideoView) findViewById(R.id.videoView);
            mc = new MediaController(this);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(mFileUri);
            mc.show();
            mVideoView.start();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), e.toString());
        }
    }

    private void loadAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        mChosenAccountName = sp.getString(YoutubeVideoUploadScreen.ACCOUNT_KEY, null);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.review, menu);
        return true;
    }

    public void uploadVideo(View view) {
        /*if (mChosenAccountName == null) {
            return;
        }*/
        // if a video is picked or recorded.

        String title = mTitle.getText().toString();
        String desc = mDescriton.getText().toString();

        if(title==null||title.isEmpty()){

            Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();

        }else if(desc==null||desc.isEmpty()){

            Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();

        }else if (mFileUri == null) {

            Toast.makeText(this, "File is not found.Something went wrong", Toast.LENGTH_SHORT).show();

        }else{

            SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

            Contents blogs = new Contents();
            blogs.setTitle(title+" Uploaded on Mera Bihar App");
            if(mDescriton.getText().toString()!=null&&!mDescriton.getText().toString().isEmpty()){
                blogs.setDescription(mDescriton.getText().toString());
            }else{
                blogs.setDescription("");
            }

            blogs.setContentType("Video");

            blogs.setCreatedBy(PreferenceHandler.getInstance(ReviewActivity.this).getUserFullName());
            blogs.setCreatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
            blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
            blogs.setProfileId(PreferenceHandler.getInstance(ReviewActivity.this).getUserId());
            blogs.setSubCategoriesId(101);


            Intent uploadIntent = new Intent(this, UploadService.class);
            uploadIntent.setData(mFileUri);
            uploadIntent.putExtra(YoutubeVideoUploadScreen.ACCOUNT_KEY, mChosenAccountName);

            Bundle bundle = new Bundle();
            bundle.putSerializable("Contents",blogs);
            uploadIntent.putExtras(bundle);
            startService(uploadIntent);
            Toast.makeText(this, R.string.youtube_upload_started,
                    Toast.LENGTH_LONG).show();
            // Go back to MainActivity after upload
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

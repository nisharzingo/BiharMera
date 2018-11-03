package tv.merabihar.app.merabihar.UI.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CommentsListAdapter;
import tv.merabihar.app.merabihar.Model.Comments;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CommentAPI;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

public class CommentsScreen extends AppCompatActivity {

    RecyclerView mCommentsList;
    ProgressBar mProgressBar;
    EditText comment;
    ImageButton mSend;

    Contents contents;
    int position;
    ArrayList<Comments> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_comments_screen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Comments");

            mCommentsList = (RecyclerView) findViewById(R.id.comments_list);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar_comment);
            comment = (EditText) findViewById(R.id.comment_eT);
            mSend = (ImageButton) findViewById(R.id.send_comment);


            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                contents = (Contents)bundle.getSerializable("Contents");
                position = bundle.getInt("Position");
            }


            if(contents!=null){

                getContents(contents.getContentId());
            }else{

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String commentDesc = comment.getText().toString();

                    if(commentDesc==null&&!commentDesc.isEmpty()){

                        Toast.makeText(CommentsScreen.this, "Please write your comment", Toast.LENGTH_SHORT).show();
                    }else{

                        Comments commentObj = new Comments();
                        commentObj.setCommentsDesc(commentDesc);
                        commentObj.setProfileId(PreferenceHandler.getInstance(CommentsScreen.this).getUserId());
                        commentObj.setContentId(contents.getContentId());
                        commentObj.setCreatedBy(PreferenceHandler.getInstance(CommentsScreen.this).getUserFullName());
                        commentObj.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                        commentObj.setUpdateDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                        postComment(commentObj);
                    }
                }
            });

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
                if(position!=0){

                    PreferenceHandler.getInstance(CommentsScreen.this).setPositionId(position);
                }
                CommentsScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getContents(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> getCat = categoryAPI.getContentsById(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<Contents>() {

                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {

                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            contents = response.body();

                            if(contents != null )
                            {

                                if(contents.getCommentsList()!=null&&contents.getCommentsList().size()!=0){

                                    commentsList = contents.getCommentsList();
                                    mProgressBar.setVisibility(View.GONE);
                                    CommentsListAdapter adapter = new CommentsListAdapter(CommentsScreen.this,contents.getCommentsList());
                                    mCommentsList.setAdapter(adapter);
                                }



                            }
                            else
                            {


                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {

                        mProgressBar.setVisibility(View.GONE);


                        Toast.makeText(CommentsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }


    private void postComment(final Comments blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                CommentAPI blogsAPI = Util.getClient().create(CommentAPI.class);
                Call<Comments> response = blogsAPI.postComment(blogs);
                response.enqueue(new Callback<Comments>() {
                    @Override
                    public void onResponse(Call<Comments> call, Response<Comments> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            getContents(contents.getContentId());
                            comment.setText("");

                        }
                        else
                        {
                            Toast.makeText(CommentsScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comments> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(CommentsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

}


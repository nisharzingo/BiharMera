package tv.merabihar.app.merabihar.UI.YoutubeSearch;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.view.Menu;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.R;

public class YoutubeSearchScreenMain extends AppCompatActivity {

    Toolbar mToolbar;
    MaterialSearchView mSearchView;
    FrameLayout mYoutubeContentLayout;

    String FRAGMENT_TAG = YoutubeSearchFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_search_screen);

        mToolbar = findViewById(R.id.youtube_search_toolbar);
        mSearchView =(MaterialSearchView) findViewById(R.id.youtube_search_material_search_view);
        mYoutubeContentLayout = findViewById(R.id.youtube_search_contents);
        mSearchView.setVoiceSearch(true);  // enabling the voice search
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Youtube Videos");


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.youtube_search_contents, new YoutubeSearchFragment(), FRAGMENT_TAG)
                .commit();



        // Running default youtube screen on separate thread
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                YoutubeSearchFragment fragment =
                        (YoutubeSearchFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                fragment.setSearchString("");
            }
        });


        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mSearchView.closeSearch();
                //   when user click on submit
                YoutubeSearchFragment fragment =
                        (YoutubeSearchFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                fragment.setSearchString(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                YoutubeSearchFragment fragment =
                        (YoutubeSearchFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                fragment.setSearchString(newText);
                return true;
            }
        });


        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //When the search is shown for the first time
            }
            @Override
            public void onSearchViewClosed() {
                // when search is closed


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.youtube_item_menu, menu);
        MenuItem item = menu.findItem(R.id.youtube_action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    // Result form voice
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

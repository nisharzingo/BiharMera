package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import tv.merabihar.app.merabihar.R;

public class TabPostMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_story);
        animateView();

    }




    private void animateView() {

        LinearLayout gal_ll = findViewById(R.id.ll_share_gal);
        gal_ll.setTranslationX(-300);
        gal_ll.animate().translationXBy(300).setDuration(500);

        LinearLayout youtube_ll = findViewById(R.id.ll_share_youtube);
        youtube_ll.setTranslationX(300);
        youtube_ll.animate().translationXBy(-300).setDuration(500);

        TextView share_something = findViewById(R.id.share_something);
        share_something.setTranslationY(-300);
        share_something.animate().translationYBy(300).setDuration(500);

    }
}

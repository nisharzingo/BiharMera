package tv.merabihar.app.merabihar.CustomFonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by ZingoHotels Tech on 30-10-2018.
 */

public class Mistral extends android.support.v7.widget.AppCompatTextView {

    public Mistral(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Mistral(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mistral(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mistral.ttf");
            setTypeface(tf);
        }
    }

}
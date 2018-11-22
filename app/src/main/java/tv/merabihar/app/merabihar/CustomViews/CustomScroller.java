package tv.merabihar.app.merabihar.CustomViews;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by ZingoHotels Tech on 21-11-2018.
 */

public class CustomScroller extends Scroller {

    private double scrollFactor = 1;

    public CustomScroller(Context context) {
        super(context);
    }

    public CustomScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
    }
}

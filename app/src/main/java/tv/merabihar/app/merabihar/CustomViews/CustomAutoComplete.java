package tv.merabihar.app.merabihar.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class CustomAutoComplete  extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public CustomAutoComplete(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
}


package tv.merabihar.app.merabihar.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import tv.merabihar.app.merabihar.R;

public class SnackbarViewer extends View {


    public SnackbarViewer(Context context) {
        super(context);
    }

    public static void showSnackbar(View parentView, String msg) {

        final Snackbar snackbar = Snackbar
                .make(parentView, msg , Snackbar.LENGTH_LONG );
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbView.setBackgroundColor(Color.BLUE);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

    }




}

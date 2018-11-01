package tv.merabihar.app.merabihar.CustomInterface;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public abstract class PageScrollListener extends RecyclerView.OnScrollListener {

    private String TAG = "PageScrollListener";
    LinearLayoutManager mlayoutManager;
    public PageScrollListener(LinearLayoutManager layoutManager)
    {
        this.mlayoutManager = layoutManager;
    }



   /* @Override
    public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
        super.onScrollStateChanged(recyclerView, newState);



        if(!isLoading() && !isLastPage())
        {

            if (!recyclerView.canScrollHorizontally(1) ) {
               // Toast.makeText(ChatDetailsActivity.this,"endOfScroll",Toast.LENGTH_LONG).show();
                loadMoreItems();

            }

            loadMoreItems();
        }
        else
        {
            Log.d(TAG, "FALSE: ");
        }

    }*/

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mlayoutManager.getChildCount();
        int totalItemCount = mlayoutManager.getItemCount();
        int firstVisibleItemPosition = mlayoutManager.findFirstVisibleItemPosition();
        int lastVisible  = mlayoutManager.findLastVisibleItemPosition();

        if(!isLoading() && !isLastPage())
        {

            boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
            if (totalItemCount > 0 && endHasBeenReached) {
                //you have reached to the bottom of your recycler view
                loadMoreItems();
            }
            Log.d(TAG, "TRUE: ");

        }
        else
        {
            Log.d(TAG, "FALSE: ");
        }

    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}

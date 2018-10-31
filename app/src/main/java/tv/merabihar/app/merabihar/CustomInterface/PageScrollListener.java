package tv.merabihar.app.merabihar.CustomInterface;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mlayoutManager.getChildCount();
        int totalItemCount = mlayoutManager.getItemCount();
        int firstVisibleItemPosition = mlayoutManager.findFirstVisibleItemPosition();

        if(!isLoading() && !isLastPage())
        {
            Log.d(TAG, "TRUE: ");
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    /*&& totalItemCount >= getTotalPageCount()*/) {
                loadMoreItems();
            }
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

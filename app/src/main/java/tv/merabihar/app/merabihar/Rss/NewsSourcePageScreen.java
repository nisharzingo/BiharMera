package tv.merabihar.app.merabihar.Rss;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import tv.merabihar.app.merabihar.R;

public class NewsSourcePageScreen extends AppCompatActivity {

    WebView mSourcePage;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_news_source_page_screen);

            mSourcePage = (WebView)findViewById(R.id.source_page);

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){

                url = bundle.getString("Source");
            }

            if(url!=null&&!url.isEmpty()){

                mSourcePage.getSettings().setJavaScriptEnabled(true);
                //mSourcePage.setWebChromeClient(new WebChromeClient());

                mSourcePage.setWebViewClient(new WebViewClient() {
                    ProgressDialog progressDialog;

                    //If you will not use this method url links are opeen in new brower not in webview
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    //Show loader on url load
                    public void onLoadResource (final WebView view, String url) {
                        if (progressDialog == null) {
                            // in standard case YourActivity.this
                            progressDialog = new ProgressDialog(view.getContext());
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                        }
                    }
                    public void onPageFinished(WebView view, String url) {
                        try{
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }catch(Exception exception){
                            exception.printStackTrace();
                        }
                    }

                });
                mSourcePage.loadUrl(url);

            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

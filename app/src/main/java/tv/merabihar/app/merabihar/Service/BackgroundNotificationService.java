package tv.merabihar.app.merabihar.Service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.commit451.youtubeextractor.YouTubeExtractor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import tv.merabihar.app.merabihar.CustomInterface.DownloadTaskVideo;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabHomeNewDesign;
import tv.merabihar.app.merabihar.WebAPI.RetrofitInterface;

/**
 * Created by ZingoHotels Tech on 15-11-2018.
 */

public class BackgroundNotificationService extends IntentService {

    public BackgroundNotificationService() {
        super("Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    String fileNames,downloadUrls,downloadUrl;

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Download")
                .setContentText("Downloading Video")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        Bundle bundle = intent.getExtras();

        if(bundle!=null){

            downloadUrls = bundle.getString("Url");
            fileNames = bundle.getString("File");

            if(downloadUrls!=null&&!downloadUrls.isEmpty()){

                downloadVideo(downloadUrls);
            }
        }



        //initRetrofit();

    }

    private void initRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://unsplash.com/")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadImage("photos/YYW9shdLIwo/download?force=true");
        try {

            downloadImage(request.execute().body());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void downloadVideo(final String YOUTUBE_ID){



        final YouTubeExtractor mExtractor = YouTubeExtractor.create();

        //mExtractor.extract(YOUTUBE_ID).enqueue(mExtractionCallback);

       /* new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                YouTubeExtractor mExtractor = YouTubeExtractor.create();

                mExtractor.extract(YOUTUBE_ID).enqueue(new Callback<YouTubeExtractionResult>() {
                    @Override
                    public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
                        bindVideoResult(response.body());
                    }

                    @Override
                    public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
                        onError(t);
                    }
                });
            }
        });*/


        String youtubeLink = "http://youtube.com/watch?v="+YOUTUBE_ID;

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;

                    List<Integer> iTags = Arrays.asList(22, 137, 18);

                    for (Integer iTag : iTags) {

                        YtFile ytFile = ytFiles.get(iTag);

                        if (ytFile != null) {

                            downloadUrl = ytFile.getUrl();

                            if (downloadUrl != null && !downloadUrl.isEmpty()) {

                                File sd = Environment.getExternalStorageDirectory();
                                String fileName = fileNames+ ".mp4";

                                File directory = new File(sd.getAbsolutePath()+"/MeraBihar App/Download/Video/");
                                //create directory if not exist
                                if (!directory.exists() && !directory.isDirectory()) {
                                    directory.mkdirs();
                                }


                                File file = new File(directory, fileName);
                                System.out.println("Download url "+downloadUrl);
                                //new DownloadTaskVideo(fileNames, downloadUrl);
                                new DownloadingTask().execute();


                                break;

                            }

                        }

                    }
// Here you can get download url





                }
            }
        };

        ytEx.execute(youtubeLink);


    }

    private void downloadImage(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "journaldev-image-downloaded.jpg");
        OutputStream outputStream = new FileOutputStream(outputFile);
        long total = 0;
        boolean downloadComplete = false;
        //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));

        while ((count = inputStream.read(data)) != -1) {

            total += count;
            int progress = (int) ((double) (total * 100) / (double) fileSize);


            updateNotification(progress);
            outputStream.write(data, 0, count);
            downloadComplete = true;
        }
        onDownloadComplete(downloadComplete);
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

    private void updateNotification(int currentProgress) {


        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendProgressUpdate(boolean downloadComplete) {

        Intent intent = new Intent(TabHomeNewDesign.PROGRESS_UPDATE);
        intent.putExtra("downloadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("Video Download Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }



    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Set Button Text when download started

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    ;//If Download completed then change button text

                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);



                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);


            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {


                }


                //Get File if SD card is present

                File sd = Environment.getExternalStorageDirectory();
                //String fileName = fileNames+ ".mp4";

                apkStorage = new File(sd.getAbsolutePath()+"/MeraBihar App/Download/Video/");

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                }

                outputFile = new File(apkStorage, fileNames);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
               /* while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }*/
                int count;
                long total = 0;
                boolean downloadComplete = false;
                //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                long fileSize = c.getContentLength();
                while ((count = is.read(buffer)) != -1) {

                    total += count;
                    int progress = (int) ((double) (total * 100) / (double) fileSize);


                    updateNotification(progress);
                    fos.write(buffer, 0, count);
                    downloadComplete = true;
                }
                onDownloadComplete(downloadComplete);

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;

            }

            return null;
        }
    }

}
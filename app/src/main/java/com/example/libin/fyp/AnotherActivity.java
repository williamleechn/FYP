package com.example.libin.fyp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        getSupportActionBar().hide();

        final int id = 1;
        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);

// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int count;
                        try {
                            URL url = new URL("http://scrapp.000webhostapp.com/app-debug.apk");
                            URLConnection connection = url.openConnection();
                            connection.connect();

                            // this will be useful so that you can show a typical 0-100%
                            // progress bar
                            int lengthOfFile = connection.getContentLength();

                            // download the file
                            InputStream input = new BufferedInputStream(url.openStream(), 8192);

                            // Output stream
                            OutputStream output = openFileOutput("beta.apk", Context.MODE_WORLD_READABLE);
//                new FileOutputStream(getFilesDir()+ "beta.apk", Context.MODE_WORLD_READABLE);

                            byte data[] = new byte[1024];
                            long total = 0;
                            while ((count = input.read(data)) != -1) {
                                total += count;
                                // publishing the progress....
                                // After this onProgressUpdate will be called
                                mBuilder.setProgress(100, (int) ((total * 100) / lengthOfFile), false);
                                // Displays the progress bar for the first time.
                                mNotifyManager.notify(id, mBuilder.build());

                                // writing data to file
                                output.write(data, 0, count);
                            }

                            // flushing output
                            output.flush();

                            // closing streams
                            output.close();
                            input.close();

                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(id, mBuilder.build());
                        File beta = new File(getFilesDir(), "beta.apk");
                        if (beta.exists()) {
                            Uri uri = Uri.fromFile(beta);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }
}

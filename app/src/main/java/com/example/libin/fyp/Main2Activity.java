package com.example.libin.fyp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {

    EditText etName, etEmail, etFeedback;
    ImageView send;
    private Toast toast;

    private void showToast(String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(this, text, duration);
        }

        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    private void showToast(int stringId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(this, stringId, duration);
        }

        toast.setText(getString(stringId));
        toast.setDuration(duration);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        etName = (EditText) findViewById(R.id.editName);
        etEmail = (EditText) findViewById(R.id.editEmail);
        etFeedback = (EditText) findViewById(R.id.editFeedback);
        send = (ImageView) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Uploading feedback...", Toast.LENGTH_SHORT);
                PrintWriter logWriter;
                try {
                    logWriter = new PrintWriter(new FileOutputStream(new File(getFilesDir(), "feedback.txt"), true));
                    logWriter.println(new Date().toString() + "\nName: " + etName.getText().toString() + "\nEmail: "
                            + etEmail.getText().toString() + "\nFeedback: " + etFeedback.getText().toString()
                            + "\n========================\n\n");
                    logWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                /////////////
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                        if (!isConnected) {
                            showToast("Oops, seems that you don't have an Internet connection...", Toast.LENGTH_LONG);
                        } else {
                            new UploadFeedback().execute();
                        }
                    }
                }.start();
            }
        });
    }

    public class UploadFeedback extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            //do loading operation here
            HttpUpload httpUpload = new HttpUpload();
            httpUpload.uploadFile(getFilesDir() + "/feedback.txt", "http://scrapp.000webhostapp.com/money_u_up_file.php");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showToast("Thank you! We have received your feedback/suggestion", Toast.LENGTH_SHORT);
            finish();
        }
    }
}

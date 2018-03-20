package com.example.libin.fyp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class LoginActivity extends AppCompatActivity {
    Animation shake;
    EditText etUser, etPass;
    Button register, login;
    String usernameInput;
    String passInput;
    ProgressBar progressBarLogin;
    private Toast toast;

    private void showToast(String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(this, text, duration);
        }

        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    private void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }

        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
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
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        
        etUser = (EditText) findViewById(R.id.editUsername);
        etPass = (EditText) findViewById(R.id.editPass);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        register = (Button) findViewById(R.id.buttonRegister);
        login = (Button) findViewById(R.id.buttLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("type", "login");
                startActivity(i);
            }
        });


        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = etUser.getText().toString();
                if (temp.contains("?") || temp.contains("*") || temp.contains("/") || temp.contains("\\") || temp.contains("<")
                        || temp.contains(">") || temp.contains(":") || temp.contains("\"") || temp.contains("|") || temp.contains(" ")) {
                    if (temp.contains("*")) {
                        temp = temp.replace("*", "");
                    } else if (temp.contains("?")) {
                        temp = temp.replace("?", "");
                    } else if (temp.contains("/")) {
                        temp = temp.replace("/", "");
                    } else if (temp.contains("\\")) {
                        temp = temp.replace("\\", "");
                    } else if (temp.contains("<")) {
                        temp = temp.replace("<", "");
                    } else if (temp.contains(">")) {
                        temp = temp.replace(">", "");
                    } else if (temp.contains(":")) {
                        temp = temp.replace(":", "");
                    } else if (temp.contains("\"")) {
                        temp = temp.replace("\"", "");
                    } else if (temp.contains("|")) {
                        temp = temp.replace("|", "");
                    } else if (temp.contains(" ")) {
                        temp = temp.replace(" ", "");
                    }
                    showToast("Username cannot contain ? * / \\ < > : \" | and space", Toast.LENGTH_LONG);
                    etUser.setText(temp);
                    etUser.setSelection(etUser.getText().length());
                    shakeView(etUser);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameInput = etUser.getText().toString();
                passInput = etPass.getText().toString();
                if (usernameInput.equals("") || passInput.equals("")) {
                    if (usernameInput.equals("")) {
//                        Toast.makeText(RegisterActivity.this, "Please enter a usernameInput", Toast.LENGTH_SHORT).show();
                        showToast("Please enter a username", Toast.LENGTH_SHORT);
                        shakeView(etUser);
                    } else if (passInput.equals("")) {
//                        Toast.makeText(RegisterActivity.this,"Please enter a password", Toast.LENGTH_SHORT).show();
                        showToast("Please enter a password", Toast.LENGTH_SHORT);
                        shakeView(etPass);
                    }
                } else {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                            if (!isConnected) {
                                showToast("Oops, seems that you don't have an Internet connection...", Toast.LENGTH_LONG);
                                progressBarLogin.setVisibility(View.GONE);
                            } else {
                                new File(getFilesDir(), "user").delete();
                                new DownloadVersionFile().execute("http://scrapp.000webhostapp.com/utilities/users/" + usernameInput + ".txt");
                            }
                        }
                    }.start();
                }
            }
        });
    }

    private void shakeView(final View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation(shake);
                    }
                });
            }
        }).start();

        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                view.clearAnimation();
            }
        }.start();
    }

    class DownloadVersionFile extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(new File(getFilesDir(), "user"));

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called

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
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        @Override
        protected void onPostExecute(String file_url) {
            File userFile = new File(getFilesDir(), "user");
            if (userFile.exists()) {
                try {
                    Scanner input = new Scanner(new FileInputStream(userFile));
                    String pass = input.nextLine();
                    if (passInput.equals(pass)) {
//                        showToast("Login Success!");
                        progressBarLogin.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, LoginSuccessActivity.class);
                        intent.putExtra("username", usernameInput);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Username or password incorrect");
                        progressBarLogin.setVisibility(View.GONE);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                progressBarLogin.setVisibility(View.GONE);
                showToast("Username or password incorrect");
            }
        }
    }
}

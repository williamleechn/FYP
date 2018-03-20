package com.example.libin.fyp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class RegisterActivity extends AppCompatActivity {
    Animation shake;
    EditText etUser, etPass, etConPass;
    Button register;
    String username;
    private RelativeLayout rlayout;
    private Toast toast;
    ProgressBar progressBarRegister;
    TextView textRegisterTitle;
    String type;

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
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        etUser = (EditText) findViewById(R.id.editUsername);
        etPass = (EditText) findViewById(R.id.editPass);
        etConPass = (EditText) findViewById(R.id.editConfirmPass);
        register = (Button) findViewById(R.id.buttRegister);
        rlayout = (RelativeLayout) findViewById(R.id.activity_register);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        textRegisterTitle = (TextView) findViewById(R.id.textRegisterTitle);

        type = getIntent().getExtras().getString("type");
        if (type.equals("changePass")) {
            textRegisterTitle.setText("Change Password");
            register.setText("Change Password");
        }


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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUser.getText().toString();
                String pass = etPass.getText().toString();
                String conPass = etConPass.getText().toString();
                if (username.equals("") || pass.equals("") || conPass.equals("")) {
                    if (username.equals("")) {
//                        Toast.makeText(RegisterActivity.this, "Please enter a usernameInput", Toast.LENGTH_SHORT).show();
                        showToast("Please enter a username", Toast.LENGTH_SHORT);
                        shakeView(etUser);
                    } else if (pass.equals("")) {
//                        Toast.makeText(RegisterActivity.this,"Please enter a password", Toast.LENGTH_SHORT).show();
                        showToast("Please enter a password", Toast.LENGTH_SHORT);
                        shakeView(etPass);
                    } else if (conPass.equals("")) {
//                        Toast.makeText(RegisterActivity.this,"Please confirm your password", Toast.LENGTH_SHORT).show();
                        showToast("Please confirm your password", Toast.LENGTH_SHORT);
                        shakeView(etConPass);
                    }
                } else if (!pass.equals("") && !conPass.equals("") && !conPass.equals(pass)) {
//                    Toast.makeText(RegisterActivity.this,"Passwords do not match", Toast.LENGTH_SHORT).show();
                    showToast("Passwords do not match", Toast.LENGTH_SHORT);
                    shakeView(etPass);
                    shakeView(etConPass);
                } else {
                    progressBarRegister.setVisibility(View.VISIBLE);
                    PrintWriter printWriter;
                    try {
                        printWriter = new PrintWriter(new FileOutputStream(new File(getFilesDir(), username + ".txt")));
                        printWriter.println(String.valueOf(pass));
                        printWriter.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
                                progressBarRegister.setVisibility(View.GONE);
                            } else {
                                new UploadFeedback().execute();
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

    public class UploadFeedback extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            //do loading operation here
            HttpUpload httpUpload = new HttpUpload();
            httpUpload.uploadFile(getFilesDir() + "/" + username + ".txt", "http://scrapp.000webhostapp.com/utilities_up_user.php");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBarRegister.setVisibility(View.GONE);
            if (type.equals("changePass")) {
                showToast("Your password has been changed", Toast.LENGTH_SHORT);

            } else {
                showToast("Your account has been registered", Toast.LENGTH_SHORT);
            }
            finish();
        }
    }
}

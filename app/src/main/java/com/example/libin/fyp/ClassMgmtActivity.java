package com.example.libin.fyp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Scanner;

public class ClassMgmtActivity extends AppCompatActivity {
    public TextView mClass;
    public Button mSearch;
    public Button add;
    public EditText mCourseCode;
    int fileIndex = 0;
    int currentFile = 1;
    String classDetail = "";
    Course course = new Course();
    MyDBHandler myDBHandler;
    private ProgressBar progressBar;
    ArrayList<Session> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_mgmt);
        myDBHandler = new MyDBHandler(this, null, null, 1);
        sessions = new ArrayList<>();
        mClass = (TextView) findViewById(R.id.classText);
        mSearch = (Button) findViewById(R.id.button);
        mCourseCode = (EditText) findViewById(R.id.courseCode);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSearchCourse);

        add = (Button) findViewById(R.id.button5);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(course.view());
//                myDBHandler.addSession(course.getSession(0));
//                ArrayList<Integer> index = new ArrayList<Integer>();
//                for (int i = 0; i < sessions.size(); i++) {
//                    for (int j = 1; j < sessions.size(); j++) {
//                        if (sessions.get(j).view().equals(sessions.get(i).view())) {
//                            if (j != i) {
//                                index.add(i);
//                            }
//                        }
//                    }
//                    for (int k = index.size() - 1; k > 0; k++) {
//                        sessions.remove(index.get(k));
//                    }
//                }
//                Toast.makeText(ClassMgmtActivity.this, "Size Before: " + sessions.size(), Toast.LENGTH_SHORT).show();
                for (int i = 1; i < sessions.size(); i++) {
                    Session a1 = sessions.get(i);
                    Session a2 = sessions.get(i-1);
                    if (a1.view().equals(a2.view())) {
                        sessions.remove(a1);
                    }
                }
                for (int i = 0; i < sessions.size(); i++) {
                    myDBHandler.addSession(sessions.get(i));
                    Toast.makeText(ClassMgmtActivity.this, sessions.get(i).view(), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(ClassMgmtActivity.this, "Size after: " + sessions.size(), Toast.LENGTH_SHORT).show();

            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessions.clear();
//                for (int i = 1; i <= 5; i++) {
//                    new File(getFilesDir(), i + ".csv").delete();
//                }
                classDetail = "";
                course.clear();
                currentFile = 1;
                File userFile = new File(getFilesDir(), "1.csv");
                if (userFile.exists()) {
                    try {
                        addCourse(mCourseCode.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    fileIndex = 1;
                    progressBar.setVisibility(View.VISIBLE);
                    new DownloadFile().execute("http://scrapp.000webhostapp.com/libin/1.csv");
                }
            }
        });
    }

    public void addCourse(String code1) throws FileNotFoundException {
        String currentScan = "";
        String endTime = "";
        boolean currentScanned = false;
        boolean duplicate = false;
        Scanner scanner = new Scanner(new FileInputStream(getFilesDir() + "/" + currentFile + ".csv"));
        ArrayList<String[]> al = new ArrayList<>();
        String[] rowArray;
        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine();
            rowArray = temp.split(",");
            al.add(rowArray);
        }
        String code = code1;
        code = code.toUpperCase();
        for (int j = 0; j < al.size(); j++) {
            for (int k = 0; k < al.get(j).length; k++) {
                if (al.get(j)[k].contains(code)) {
                    if ((al.get(j)[k] + "\n" + al.get(j)[0]).equals(currentScan)) {
                        duplicate = true;
                    } else {
                        currentScanned = false;
                    }
                    if (!currentScanned) {
                        currentScan = al.get(j)[k] + "\n" + al.get(j)[0];
                        currentScanned = true;
                    }

                    if (!duplicate) {
                        classDetail += al.get(0)[0] + "\n" + al.get(j)[k] + "\n" + al.get(j)[0] + "\n" + al.get(0)[k].substring(0, 5);
                        endTime = al.get(0)[k].substring(8) + "\n\n";
                        classDetail += " - " + endTime;
                        Session session = new Session();
                        session.setDay(al.get(0)[0]);
                        session.setName(al.get(j)[k]);
                        session.setPlace(al.get(j)[0]);
                        session.setStartTime(al.get(0)[k].substring(0, 5));
                        session.setEndTime(endTime);
                        course.addSession(session);
                        sessions.add(session);

//                        System.out.print(classDetail);
                    } else {
                        endTime = al.get(0)[k].substring(8) + "\n\n";
                        // Toast.makeText(this, String.valueOf(classDetail.length()), Toast.LENGTH_SHORT).show();
                        classDetail = classDetail.substring(0, classDetail.length() - 8);
                        classDetail += " " + endTime;
                        Session session1 = course.getSession(course.getSize() - 1);
                        session1.setEndTime(endTime);

//                        System.out.println(al.get(0)[k].substring(8));
                    }
                    duplicate = false;
                }
            }
        }
        mClass.setText(course.view());



//        myDBHandler.addSession(course.getSession(0));

//        for (int j = 0; j < al.size(); j++) {
//            for (int k = 0; k < al.get(j).length; k++) {
//                if (al.get(j)[k].contains(code)) {
//                    classDetail += al.get(0)[0] + "\n" + al.get(j)[k] + "\n" + al.get(j)[0] + "\n" + al.get(0)[k] + "\n\n";
//                    mClass.setText(classDetail);
//                }
//            }
//        }
        if (currentFile != 5) {
            currentFile++;
            addCourse(mCourseCode.getText().toString());
        }
    }

    class DownloadFile extends AsyncTask<String, String, String> {

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
                OutputStream output = new FileOutputStream(new File(getFilesDir(), fileIndex + ".csv"));

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
            if (fileIndex != 5) {
                fileIndex++;
                new DownloadFile().execute("http://scrapp.000webhostapp.com/libin/" + fileIndex + ".csv");
            } else {
                progressBar.setVisibility(View.GONE);
                try {
                    addCourse(mCourseCode.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
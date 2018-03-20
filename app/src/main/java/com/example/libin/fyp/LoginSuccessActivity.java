package com.example.libin.fyp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class LoginSuccessActivity extends AppCompatActivity {
    Button button;
    Button button3;
    Button setAlarm;
    Button changePassword;
    int count = 0;
    ArrayList<String[]> dataSets;
    private MyDBHandler myDBHandler;
    private int arrayCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        getSupportActionBar().hide();

        myDBHandler = new MyDBHandler(this, null, null, 1);
        button3 = (Button) findViewById(R.id.button3);
        button = (Button) findViewById(R.id.classmgn);
        setAlarm = (Button) findViewById(R.id.button4);
        changePassword = (Button) findViewById(R.id.button5);

        Button button6  = (Button) findViewById(R.id.button6);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSuccessActivity.this, ClassMgmtActivity.class));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSuccessActivity.this, ViewScheduleActivity.class));
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count = getData();
                if (count != 0) {
                    addCalendar();
//                    Toast.makeText(LoginSuccessActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();
                    count--;
                }
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginSuccessActivity.this, RegisterActivity.class);
                i.putExtra("type", "changePass");
                startActivity(i);
            }
        });
    }

    private int getData() {
        Cursor res = myDBHandler.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            return 0;
        } else {

            dataSets = new ArrayList<>();
            while (res.moveToNext()) {
                String course[] = new String[5];
                course[0] = res.getString(1);//day
                course[1] = res.getString(2);//name
                course[2] = res.getString(3);//place
                course[3] = res.getString(4);//start
                course[4] = res.getString(5);//end
                dataSets.add(course);
            }

            return dataSets.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (count != 0) {
//            Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
            addCalendar();
            count--;
        } else {
            arrayCount = 0;
//            Toast.makeText(this, "Add complete", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCalendar() {
        String day = dataSets.get(arrayCount)[0].substring(0, 2);
        String name = dataSets.get(arrayCount)[1];
        String place = dataSets.get(arrayCount)[2];
        int startTime = (int) Double.parseDouble(dataSets.get(arrayCount)[3]);
        int endTimeHour = (int) Double.parseDouble(dataSets.get(arrayCount)[4]);
        int endTimeMinute = Integer.parseInt(dataSets.get(arrayCount)[4].substring(3));

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 20, startTime, 0, 0);

        calendar.setTimeZone(TimeZone.getDefault());
        long start = calendar.getTimeInMillis();

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 20, endTimeHour, endTimeMinute, 0);
        long end = calendar.getTimeInMillis();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .setType("vnd.android.cursor.item/event")
                .putExtra(CalendarContract.Events.TITLE, name)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, place)
                .putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=" + day + ";UNTIL=20170701")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                .putExtra(CalendarContract.ACTION_EVENT_REMINDER, 30)
                .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(CalendarContract.Reminders.EVENT_ID, CalendarContract.Events._ID)
                .putExtra(CalendarContract.Events.ALLOWED_REMINDERS, "METHOD_DEFAULT")
                .putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                .putExtra(CalendarContract.Reminders.MINUTES, 30);

        startActivity(intent);
        arrayCount++;
    }
}

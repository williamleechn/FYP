package com.example.libin.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class AddCalendar extends AppCompatActivity {

    EditText day, name, place, start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        // If you want the start times to show up, you have to set them
        Calendar calendar = Calendar.getInstance();

        // Here we set a start time of Tuesday the 17th, 6pm
        calendar.set(2016, Calendar.APRIL, 20, 18, 0, 0);
        calendar.setTimeZone(TimeZone.getDefault());

        long start = calendar.getTimeInMillis();
        // add three hours in milliseconds to get end time of 9pm
        long end = calendar.getTimeInMillis() + 3 * 60 * 60 * 1000;

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .setType("vnd.android.cursor.item/event")
                .putExtra(CalendarContract.Events.TITLE, "Class")
                .putExtra(CalendarContract.Events.DESCRIPTION, "WMES3302")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "MM1")
                .putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=TH;UNTIL=20170701")

                // to specify start time use "beginTime" instead of "dtstart"
                //.putExtra(Events.DTSTART, calendar.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)

                // if you want to go from 6pm to 9pm, don't specify all day
                //.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);
    }
}
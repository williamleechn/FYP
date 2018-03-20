package com.example.libin.fyp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewScheduleActivity extends AppCompatActivity {
    TextView tvSchedule;
    MyDBHandler myDBHandler;
    Button delete;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        et = (EditText) findViewById(R.id.id);
        delete = (Button) findViewById(R.id.delete);
        myDBHandler = new MyDBHandler(this, null, null, 1);
        tvSchedule = (TextView) findViewById(R.id.textViewSchedule);
        showSchedual();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHandler.deleteCourse(et.getText().toString().trim());
                myDBHandler.updateData("4");
                showSchedual();
            }

        });
    }

    private void showSchedual() {
        Cursor res = myDBHandler.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            tvSchedule.setText("");
            return;
        }

        ArrayList<String> dataSets = new ArrayList<>();
        while (res.moveToNext()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Id: " + res.getString(0) + "\n");
            buffer.append("Day: " + res.getString(1) + "\n");
            buffer.append("Name: " + res.getString(2) + "\n");
            buffer.append("Place: " + res.getString(3) + "\n");
            buffer.append("Start: " + res.getString(4) + "\n");
            buffer.append("End: " + res.getString(5) + "\n");
            dataSets.add(buffer.toString());
        }

        System.out.println(dataSets);
        String result = "";
        for (int i = 0; i < dataSets.size(); i++) {
            result += dataSets.get(i) + "\n";
        }
        tvSchedule.setText(result);
    }
}


//        String schedule = myDBHandler.databaseToString();
//        for (int i = 0; i < dataSets.size(); i++) {
//            if (i != dataSets.size() - 1) {
//                if (dataSets.get(i).equals(dataSets.get(i + 1))) {
//                    dataSets.remove(i);
//                }
//            }
//            if (dataSets.get(dataSets.size() - 1).equals(dataSets.get(dataSets.size() - 2))) {
//                dataSets.remove(dataSets.size() - 1);
//            }
//        }

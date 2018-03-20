package com.example.libin.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button;
    Toast toast;
    ListView listView;
    ArrayList<String> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] names = {"Apple", "Banana", "Car", "Ice Cream", "Andrew", "Building", "Motorola", "Nokia", "Sony", "Samsung", "Sharp",
                "Toyota", "Mazda", "Nissan", "Honda", "Morning", "Evening", "Chicken", "Hello, World!"};
        button = (Button) findViewById(R.id.button);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.checkedTextView, names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = names[i];
//                String selectedItem = ((TextView)view).getText().toString();
                showToast("Clicked on: " + selectedItem, Toast.LENGTH_SHORT);
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);
                } else {
                    selectedItems.add(selectedItem);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String items = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    items += "" + (i + 1) + ". " + selectedItems.get(i) + "\n";
                }
                showToast(items, Toast.LENGTH_SHORT);
            }
        });
    }

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
}


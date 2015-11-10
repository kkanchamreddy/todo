package com.kkanchamreddy.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    private int itemPosition = -1;
    ArrayList<String> priorityList = new ArrayList<String>();
    String selectedPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String itemText = getIntent().getStringExtra("itemText");
        String priority = getIntent().getStringExtra("priority");
        itemPosition = getIntent().getIntExtra("pos", -1);

        EditText editText = (EditText) findViewById(R.id.etEditItem);
        Spinner spinner = (Spinner) findViewById(R.id.spPriority);


        priorityList.add("LOW");
        priorityList.add("MEDIUM");
        priorityList.add("HIGH");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item,priorityList);

        // Set the Adapter
        spinner.setAdapter(arrayAdapter);






        // Set the ClickListener for Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                selectedPriority = priorityList.get(i);


            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                selectedPriority = priorityList.get(0);

            }

        });

        if(itemText != null) {
            //Populate with any values from Main Activity
            if(priority != null) {
                spinner.setSelection(priorityList.indexOf(priority));
            }
            editText.setText(itemText);
        }


    }


    public void onSave(View v) {
        EditText etEdit = (EditText) findViewById(R.id.etEditItem);
        Spinner spinner = (Spinner) findViewById(R.id.spPriority);

        // Prepare data intent
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("editedItem", etEdit.getText().toString());
        data.putExtra("priority", priorityList.get(spinner.getSelectedItemPosition()));
        data.putExtra("pos", itemPosition);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        finish();

    }

}

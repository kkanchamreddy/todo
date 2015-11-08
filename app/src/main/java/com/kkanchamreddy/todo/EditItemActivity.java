package com.kkanchamreddy.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private int itemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String itemText = getIntent().getStringExtra("itemText");
        EditText editText = (EditText) findViewById(R.id.etEditItem);

        itemPosition = getIntent().getIntExtra("pos",0);

        editText.setText(itemText);
    }


    public void onSave(View v) {
        EditText etEdit = (EditText) findViewById(R.id.etEditItem);

        // Prepare data intent
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("editedItem", etEdit.getText().toString());
        data.putExtra("pos", itemPosition);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        finish();

    }

}

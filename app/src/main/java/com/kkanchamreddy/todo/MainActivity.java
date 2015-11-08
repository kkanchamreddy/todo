package com.kkanchamreddy.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the list view
        lvItems = (ListView) findViewById(R.id.lvItems);

        //init the items ArrayList
        //items = new ArrayList<>();
        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);

        lvItems.setAdapter(itemsAdapter);

        
        setupListviewListener();


    }

    private void setupListviewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                String itemText=(String)adapter.getItemAtPosition(pos);
                // put "extras" into the bundle for access in the second activity
                i.putExtra("itemText", itemText);
                i.putExtra("pos", pos);

                startActivityForResult(i, REQUEST_CODE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void  onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "items.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "items.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItemString = data.getExtras().getString("editedItem");
            int pos = data.getExtras().getInt("pos");

            // Toast the name to display temporarily on screen
            Toast.makeText(this, editedItemString, Toast.LENGTH_SHORT).show();


            items.set(pos, editedItemString);
            itemsAdapter.notifyDataSetChanged();
            writeItems();


        }
    }
}

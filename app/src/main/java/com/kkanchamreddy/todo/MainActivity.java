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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;
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
        items = new ArrayList<>();
        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);

        lvItems.setAdapter(itemsAdapter);

        
        setupListviewListener();


    }

    private void setupListviewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                Item removedItem = items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                deleteItem(removedItem);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                Item itemToEdit = (Item) adapter.getItemAtPosition(pos);
                // put "extras" into the bundle for access in the second activity
                i.putExtra("itemText", itemToEdit.text);
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

        //writeItems();
        long id = addItem(itemText);
        Item item = new Item(String.valueOf(id), itemText);
        itemsAdapter.add(item);

        etNewItem.setText("");

    }

    private void readItems() {
        TodoDBHelper dbHelper = TodoDBHelper.getInstance(this);
        List<Item> itemList= dbHelper.getAllItems();
        if(itemList != null) {
            for(int i = 0; i < itemList.size(); i++) {
                items.add(itemList.get(i));
            }
        }


    }

    private void deleteItem(Item item) {
        TodoDBHelper dbHelper = TodoDBHelper.getInstance(this);
        dbHelper.removeItem(item.id);
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

    private long addItem(String text) {

        TodoDBHelper dbHelperInstance = TodoDBHelper.getInstance(this);
        return dbHelperInstance.addItem(text);
    }

    private void updateItem(Item item) {
        TodoDBHelper dbHelperInstance = TodoDBHelper.getInstance(this);
        dbHelperInstance.updateItem(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItemString = data.getExtras().getString("editedItem");
            int pos = data.getExtras().getInt("pos");

            // Toast the name to display temporarily on screen
            Toast.makeText(this, editedItemString, Toast.LENGTH_SHORT).show();

            Item editedItem = items.get(pos);
            editedItem.text = editedItemString;

            itemsAdapter.notifyDataSetChanged();
            updateItem(editedItem);
            //writeItems();


        }
    }
}

package com.kkanchamreddy.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
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

        //itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        itemsAdapter = new ItemsAdapter(this, items);


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
                i.putExtra("priority", itemToEdit.priority);

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
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);


        // put "extras" into the bundle for access in the second activity
        //i.putExtra("itemText", "New Task");
        //i.putExtra("priority", "MEDIUM");

        startActivityForResult(i, REQUEST_CODE);

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



    private long addItem(String text, String priority) {

        TodoDBHelper dbHelperInstance = TodoDBHelper.getInstance(this);
        return dbHelperInstance.addItem(text, priority);
    }

    private void updateItem(Item item) {
        TodoDBHelper dbHelperInstance = TodoDBHelper.getInstance(this);
        dbHelperInstance.updateItem(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String itemText = data.getExtras().getString("editedItem");
            String priority = data.getExtras().getString("priority");
            int pos = data.getExtras().getInt("pos");

            // Toast the name to display temporarily on screen
            Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
            if(pos > -1) {
                Item editedItem = items.get(pos);
                editedItem.text = itemText;
                editedItem.priority = priority;

                itemsAdapter.notifyDataSetChanged();
                updateItem(editedItem);
            } else {
                long id = addItem(itemText, priority);
                Item newItem = new Item(String.valueOf(id), itemText, priority );
                itemsAdapter.add(newItem);
                //itemsAdapter.notifyDataSetChanged();
            }



            //writeItems();


        }
    }
}

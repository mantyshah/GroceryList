package com.example.manthanshah.grocerylist.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.manthanshah.grocerylist.Activities.Data.DataBaseHandler;
import com.example.manthanshah.grocerylist.Activities.Model.Grocery;
import com.example.manthanshah.grocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem, quantity;
    private Button saveButton;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DataBaseHandler(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createpopup();
            }
        });
        byPassActivity();
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

    public void createpopup()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        quantity = (EditText) view.findViewById(R.id.groceryQuatity);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Save To DB
                //ToDO: Go to Next Screen
                if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty())
                {
                    saveGroceryToDB(view);
                }

            }
        });

    }

    private void saveGroceryToDB(View view) {

        Grocery grocery = new Grocery();

        String newGrocery = groceryItem.getText().toString();
        String newquantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuatity(newquantity);

        //Save to DB

        db.addGrocery(grocery);

        Snackbar.make(view, "Item Saved!", Snackbar.LENGTH_SHORT).show();

        //Log.d("Item Added id: ", String.valueOf(db.countGrocery()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                //Start new Activity

                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);//1 sec

    }

    public void byPassActivity()
    {
        //Check if database is empty; if not then go to list activity

        if(db.countGrocery()> 0 )
        {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}

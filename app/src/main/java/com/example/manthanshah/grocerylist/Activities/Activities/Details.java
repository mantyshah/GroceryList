package com.example.manthanshah.grocerylist.Activities.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.manthanshah.grocerylist.R;

public class Details extends AppCompatActivity {

    private TextView itemName, itemQuantity, dateAdded;
    Button delete,edit;
    private int groceryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        itemName = (TextView) findViewById(R.id.nameTextDet);
        itemQuantity = (TextView) findViewById(R.id.quatityTextDet);
        dateAdded = (TextView) findViewById(R.id.dateTextDet);
        delete = (Button) findViewById(R.id.deleteButtonDet);
        edit = (Button) findViewById(R.id.editButtonDet);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            itemName.setText( bundle.getString("name"));
            itemQuantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryID = bundle.getInt("id");
        }


    }
}

package com.example.manthanshah.grocerylist.Activities.Interface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manthanshah.grocerylist.Activities.Activities.Details;
import com.example.manthanshah.grocerylist.Activities.Activities.MainActivity;
import com.example.manthanshah.grocerylist.Activities.Data.DataBaseHandler;
import com.example.manthanshah.grocerylist.Activities.Model.Grocery;
import com.example.manthanshah.grocerylist.R;

import java.util.List;

/**
 * Created by Manthan.Shah on 19-12-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;

    private List<Grocery> groceryList;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryList.get(position);

        holder.name.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuatity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, quantity, dateAdded;
        private Button editButton, deleteButton;
        public int id;

        private ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quatity);
            dateAdded = view.findViewById(R.id.dateAdded);

            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Go to Details Screen

                    int position = getAdapterPosition();

                    Grocery grocery = groceryList.get(position);
                    Intent intent =  new Intent(context, Details.class);

                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuatity());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    intent.putExtra("id", grocery.getId());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId())
            {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);

                    editItem(grocery);

                    break;

                case R.id.deleteButton:
                     position = getAdapterPosition();
                     grocery = groceryList.get(position);
                     deleteItem(grocery.getId());
                    break;
            }

        }

        private void editItem(final Grocery grocery)
        {
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem, quantity;
            final TextView title = view.findViewById(R.id.title);
            Button saveButton;
            title.setText(R.string.edit_grocery);

            groceryItem = view.findViewById(R.id.groceryItem);
            quantity = view.findViewById(R.id.groceryQuatity);
            saveButton = view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHandler db = new DataBaseHandler(context);

                    //Update Item

                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuatity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty())
                    {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 1200);//1 sec
                    }
                    else
                    {
                        Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }

        private void deleteItem(final int id) {

            //create confirmation_dialogue

            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialogue, null);

            Button noButton,yesButton;
            noButton = view.findViewById(R.id.noButton);
            yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Delete Item

                    DataBaseHandler db = new DataBaseHandler(context);
                    db.deleteGrocery(id);

                    groceryList.remove(getAdapterPosition());
                    Snackbar.make(view, "Grocery Deleted", Snackbar.LENGTH_LONG).show();
                    notifyItemRemoved(getAdapterPosition());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            DataBaseHandler db = new DataBaseHandler(context);
                            if(db.countGrocery() == 0)
                            {
                                Intent intent =  new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                if(context instanceof Activity){
                                ((Activity)context).finish(); }
                            }
                        }
                    }, 1000);//1 sec

                }
            });



        }
    }

}

package com.example.manthanshah.grocerylist.Activities.Data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.manthanshah.grocerylist.Activities.Model.Grocery;
import com.example.manthanshah.grocerylist.Activities.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * Created by Manthan.Shah on 18-12-2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DataBaseHandler(Context context) {
        super(context, Constants.DB_NAME,  null , Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + " ("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.KEY_GROCERYITEM + " TEXT, "
                + Constants.KEY_QTY_NUMBER + " TEXT, " + Constants.KEY_DATE_NAME +" TEXT);";

        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    //CRUD OPERATIONS

    // ADD GROCERY

    public void addGrocery(Grocery grocery)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERYITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTY_NUMBER, grocery.getQuatity());
        contentValues.put(Constants.KEY_DATE_NAME, currentDateTimeString);
        db.insert(Constants.TABLE_NAME, null, contentValues);

        Log.d("Saved", "Saved to Db named :  " + Constants.TABLE_NAME);
        db.close();
    }
//Get Grocery
    public Grocery getGrocery(int id)
    {
        SQLiteDatabase db =  this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                Constants.KEY_GROCERYITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?", new String[]{String.valueOf(id)}
                ,null,null,null, null);

        if(cursor !=null)
            cursor.moveToFirst();

        Grocery grocery = new Grocery();
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERYITEM)));
        grocery.setQuatity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

        //Convert timestamp to readable

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
        grocery.setDateItemAdded(formatDate);




        return grocery;
    }

    //Get all Grocery

    public List<Grocery> getAllGrocery()
    {
        SQLiteDatabase db =  this.getReadableDatabase();

        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME , new String[]{
                Constants.KEY_ID, Constants.KEY_GROCERYITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                null, null, null, null ,
                Constants.KEY_DATE_NAME + " DESC");
        if (cursor.moveToFirst())
        {
            do {
                    Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERYITEM)));
                grocery.setQuatity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                grocery.setDateItemAdded(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_NAME)));

                //Add to Array List

                groceryList.add(grocery);


            }while (cursor.moveToNext());
        }

        return groceryList;
    }

    //Update Grocery
    public int updateGrocery(Grocery grocery)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERYITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTY_NUMBER, grocery.getQuatity());
        contentValues.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //Updated Row


        return db.update(Constants.TABLE_NAME, contentValues, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(grocery.getId())});
    }



    //Delete Grocery

    public void deleteGrocery(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});

        db.close();

    }

    //Count Grocery

    public int countGrocery()
    {

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        return count;
    }
}

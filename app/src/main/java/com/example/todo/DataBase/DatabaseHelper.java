package com.example.todo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todo.Model.NoteModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DatabaseHelper  extends SQLiteOpenHelper {

    public  static  final String DATABASE_NAME = "notes.db";
    public  static  final int DATABASE_Version = 1;
    public  static  final String Table_NAME = "TODO";



    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        query ="CREATE TABLE "+ Table_NAME +"( ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT )";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Table_NAME);
        onCreate(sqLiteDatabase);
    }

    public  void addNotes(String title, String des){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Title", title);
        cv.put("Description", des);
        sqLiteDatabase.insert(Table_NAME, null, cv);
        sqLiteDatabase.close();
    }

    public void delete(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Table_NAME, "ID="+id, null);
        sqLiteDatabase.close();
    }

    public  void updateNotes(String title, String des, String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Title", title);
        cv.put("Description", des);
        sqLiteDatabase.update(Table_NAME,cv, "ID="+id, null);
        sqLiteDatabase.close();
    }

    public ArrayList<NoteModel> getNotes(){
        ArrayList<NoteModel> arrayList= new ArrayList<>();

        String get_value  = "SELECT * FROM "+Table_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(get_value, null);

        if(cursor.moveToFirst())
        {
            do{
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setDes(cursor.getString(2));
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }

        return arrayList;
    }

}

package com.igabeto.mayoromenor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "//data/data/com.igabeto.mayoromenor/databases/players_db";
    public static String DB_NAME = "players_db";
    private final Context context;
    public static int db_v = 1;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(db.isReadOnly()){
            db = getWritableDatabase();
        }
        db.execSQL("CREATE TABLE players (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT unique, normalRecord INTEGER, advancedRecord INTEGER, average REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

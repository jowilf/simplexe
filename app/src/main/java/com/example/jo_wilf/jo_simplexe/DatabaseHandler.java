package com.example.jo_wilf.jo_simplexe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JO on 03/02/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final boolean mdp=false;
    protected final static int VERSION = 1;
    protected final static String NOM = "database.db";
    public static synchronized DatabaseHandler getInstance(Context context){
        if(sInstance==null){sInstance=new DatabaseHandler(context, NOM, null, VERSION);}
        return sInstance;
    }
    public static DatabaseHandler sInstance;
    public DatabaseHandler(Context context, String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate (SQLiteDatabase  db){
        db.execSQL(" CREATE TABLE  motDePasse( id integer primary key  not null,mot TEXT);");
    }
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newversion){
        db.execSQL(" drop table if exists motDePasse");
        onCreate(db);
    }

}

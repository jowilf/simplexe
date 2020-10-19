package com.example.jo_wilf.jo_simplexe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by JO on 03/02/2017.
 */
public abstract class DAOBase {
    protected final static int VERSION=1;
    protected final static String NOM= "database.db";
    protected SQLiteDatabase mDb =null;
    protected DatabaseHandler mHandler =null;
    public DAOBase(Context context){
        this.mHandler=new DatabaseHandler(context, NOM, null , VERSION) ;
    }
    public SQLiteDatabase open(){
        mDb=mHandler.getWritableDatabase();
        return mDb;
    }
    public void close(){
        mDb.close();
    }
    public SQLiteDatabase getDb(){
        return mDb;
    }
}

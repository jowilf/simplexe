package com.example.jo_wilf.jo_simplexe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.SQLException;

/**
 * Created by JO on 03/02/2017.
 */
public class MdpDAO {
    protected final static int VERSION = 1;
    protected final static String NOM = "database.db";
    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public void modifier(Mdp m) {

    }

    public MdpDAO(Context context) {
        this.mHandler = new DatabaseHandler(context, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {

        mDb = mHandler.getWritableDatabase();
return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public long insertMdp() {
        ContentValues values = new ContentValues();
        values.put("mot", "valide");
        return mDb.insert("motdePasse", null, values);
    }

    public int updateMdp(Mdp mdp) {
        ContentValues values = new ContentValues();
        values.put("mot", mdp.get());
        return mDb.update("motDePasse", values, "id=1", null);
    }

    public boolean getval() {
        Cursor c = mDb.query("motDePasse", new String[]{"id", "mot"}, null, null, null, null, null);
        if (c.getCount()!=0)return false;
        else return true;
    }

    public Mdp cursortoMdp(Cursor c) {
        if (c.getCount() == 0)
            return null;

            Mdp mdp = new Mdp();
            mdp.set(c.getString(c.getColumnIndex("mot")));
        c.close();
        return mdp;

    }
}

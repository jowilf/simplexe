package com.example.jo_wilf.jo_simplexe;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;

public class LoginActivity extends AppCompatActivity {
    MdpDAO mdpDAO = new MdpDAO(this);
DatabaseHandler dbh =new DatabaseHandler(this,"mybase.db",null,1);
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payement);

        }



    public void btn_c(View view){
        EditText editText = (EditText) findViewById(R.id.mdp);
        String string = editText.getText().toString();
        if (string.equals("jowilf")) {
            SQLiteDatabase db=mdpDAO.open();
            ContentValues values=new ContentValues();
            values.put("mot","valide");
            db.insert("motDePasse", null, values);

mdpDAO.close();
            finish();
        }
        else{
            TextView textView=(TextView)findViewById(R.id.rep);
            textView.setText("Mot de passe erroné!\n Rééssayer");
        }
    }


    }




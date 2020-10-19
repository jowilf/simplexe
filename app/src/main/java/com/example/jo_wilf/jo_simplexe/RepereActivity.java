package com.example.jo_wilf.jo_simplexe;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RepereActivity extends AppCompatActivity {
    public ArrayList<String[][]> ts = new ArrayList<>();
    public ArrayList<String[]> var_hbases = new ArrayList<>();
    public Intent intent;
    public ArrayList<String[]> var_dbases = new ArrayList<>();
    public Fraction[][] ti;
    public boolean borne = false;
    public ArrayList<String[]> Ms = new ArrayList<>();
    public int num = 0;
    public ArrayList<Integer> piv_ilist=new ArrayList<>(),piv_jlist=new ArrayList<>();
    boolean degenerescence = false;
    public ArrayList<Integer> art_list = new ArrayList<Integer>(), egal_list = new ArrayList<Integer>(), ecar_list = new ArrayList<Integer>();
    int nbr_eqt, nbr_var, nbr_col, nbr_ecar, max_min;
    public ArrayList<String[]> enT2FString(ArrayList<String> tr) {
        ArrayList<String[]> t = new ArrayList<>();
        for (int k = 0; k < tr.size(); k++) {
            StringTokenizer tokenizer = new StringTokenizer(tr.get(k), " ");
            int n=tokenizer.countTokens();
            String []string = new String[n];
            for (int i = 0; i < n; i++) {
                string[i] = tokenizer.nextToken();
            }
            t.add(string);
        }
        return t;
    }
    public int pos_of(String[] v,String k)
    {
        int result=0;     int n=v.length;
        for(int i=0;i<n;i++){
            if(k.equals(v[i].trim())){
                result=i;i+=n;
            }
        }
        return  result;
    }
    public ArrayList<String> enTString(ArrayList<String[][]> tk) {
        ArrayList<String> tr = new ArrayList<>();
        for (int k = 0; k < tk.size(); k++) {
            String[][] t = tk.get(k);
            String string = "";

            for (int i = 0; i <= nbr_eqt; i++) {
                for (int j = 0; j <= nbr_col; j++) {
                    string += t[i][j] + " ";
                }
            }
            tr.add(string);
        }

        return tr;
    }

    public ArrayList<String[][]> enFraction2(ArrayList<String> tr) {
        ArrayList<String[][]> t = new ArrayList<>();
        for (int k = 0; k < tr.size(); k++) {
            String[][] tk = new String[nbr_eqt + 1][nbr_col + 1];
            StringTokenizer tokenizer = new StringTokenizer(tr.get(k));
            for (int i = 0; i <= nbr_eqt; i++) {
                for (int j = 0; j <= nbr_col; j++) {

    tk[i][j] = tokenizer.nextToken();

                }
            }
            t.add(tk);
        }
        return t;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("nbr_eqt", nbr_eqt);
        outState.putInt("nbr_var", nbr_var);
        outState.putInt("nbr_col", nbr_col);
        outState.putInt("nbr_ecar", nbr_ecar);
        outState.putInt("max_min", max_min);
        outState.putIntegerArrayList("ec", ecar_list);
        outState.putIntegerArrayList("ar", art_list);
        outState.putIntegerArrayList("eg", egal_list);
        outState.putBoolean("borne", borne);
        outState.putBoolean("is", degenerescence);
        outState.putStringArrayList("ti", enStrin(ti));
        outState.putStringArrayList("var_hbases", enT2String(var_hbases));
        outState.putStringArrayList("var_dbases", enT2String(var_dbases));
        outState.putStringArrayList("Ms", enT2String(Ms));

        intent.putExtra("maCle",outState);

    }
    public ArrayList<String> enStrin(Fraction[][] t) {
        ArrayList<String> tr = new ArrayList<>();
        for (int i = 0; i <= nbr_eqt; i++) {
            String string = "";

            for (int j = 0; j <= nbr_col; j++) {
                string += t[i][j].EnString() + " ";
            }
            tr.add(string);
        }

        return tr;
    }
    public ArrayList<String> enT2String(ArrayList<String[]> tk) {
        ArrayList<String> t = new ArrayList<>();
        for (int k = 0; k < tk.size(); k++) {
            String[] th = tk.get(k);
            String string = "";
            for (int i = 0; i < th.length; i++) {
                if (i != th.length - 1) string += th[i] + " ";
                else string += th[i];
            }
            t.add(string);
        }
        return t;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_eperea_ctivity);
        Bundle bundle=this.getIntent().getExtras();
        savedInstanceState=bundle.getBundle("macle");
        nbr_col = savedInstanceState.getInt("nbr_col");
        nbr_eqt = savedInstanceState.getInt("nbr_eqt");
        nbr_var = savedInstanceState.getInt("nbr_var");
        nbr_ecar = savedInstanceState.getInt("nbr_ecar");
        Ms = enT2FString(savedInstanceState.getStringArrayList("Ms"));
        var_dbases = enT2FString(savedInstanceState.getStringArrayList("var_dbases"));
        var_hbases = enT2FString(savedInstanceState.getStringArrayList("var_hbases"));
        degenerescence = savedInstanceState.getBoolean("is");
        piv_ilist=savedInstanceState.getIntegerArrayList("piv_i");
        piv_jlist=savedInstanceState.getIntegerArrayList("piv_j");
        borne = savedInstanceState.getBoolean("borne");
        egal_list = savedInstanceState.getIntegerArrayList("eg");
        ecar_list = savedInstanceState.getIntegerArrayList("ec");
        art_list = savedInstanceState.getIntegerArrayList("ar");
        max_min = savedInstanceState.getInt("max_min");
        ts = enFraction2(savedInstanceState.getStringArrayList("ts"));
        ti = enFraction(savedInstanceState.getStringArrayList("ti"));

        intent=new Intent(this,Main2Activity.class);
        String[] var_hbase = var_hbases.get(ts.size()-1);
        int nbr_cols= var_hbase.length;

        String[][] tf = ts.get(ts.size()-1);
        Repere repere=(Repere)findViewById(R.id.repere);
        repere.init();
        double x1=0,x2=0;
        String[] var_dbase = var_dbases.get(ts.size()-1);
   if(is_in(var_dbase,"x1")) {
            x1= endouble(new Fraction(tf[pos_of(var_dbase, "x1")+1][nbr_cols]));
        }
        if(is_in(var_dbase,"x2")) {
            x2= endouble(new Fraction(tf[pos_of(var_dbase, "x2")+1][nbr_cols]));
        }
            repere.set(ti, nbr_col, nbr_eqt,ts,Ms,degenerescence,borne,nbr_cols,x1,x2);
        repere.set2(art_list,egal_list,ecar_list);
        repere.trace();
    }
    public double endouble(Fraction e) {
        return (double) e.Numerateur / e.Denominateur;
    }

    public boolean is_in(String[] v,String k)
    {
        boolean result=false;     int n=v.length;
        for(int i=0;i<n;i++){
            if(k.equals(v[i].trim())){
                result=true;i+=n;
            }
        }
        return  result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public Fraction[][] enFraction(ArrayList<String> tr) {
        Fraction[][] t = new Fraction[nbr_eqt + 1][nbr_col + 1];
        for (int i = 0; i <= nbr_eqt; i++) {
            StringTokenizer tokenizer = new StringTokenizer(tr.get(i), " ");
            for (int j = 0; j <= nbr_col; j++) {
                t[i][j] = new Fraction(tokenizer.nextToken());
            }

        }
        return t;
    }

}

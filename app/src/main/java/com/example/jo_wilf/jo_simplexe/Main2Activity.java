package com.example.jo_wilf.jo_simplexe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main2Activity extends AppCompatActivity {
    public ArrayList<String[][]> ts = new ArrayList<>();
    public ArrayList<String[]> var_hbases = new ArrayList<>();
    public Intent intent;
    public ArrayList<String[]> var_dbases = new ArrayList<>();
    public ArrayList<String[]> ratio_sav = new ArrayList<>();
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
            StringTokenizer tokenizer = new StringTokenizer(tr.get(k), " ");
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

        outState.putStringArrayList("ratio", enT2String(ratio_sav));
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
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
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
        ratio_sav=enT2FString(savedInstanceState.getStringArrayList("ratio"));
        borne = savedInstanceState.getBoolean("borne");
        egal_list = savedInstanceState.getIntegerArrayList("eg");
        ecar_list = savedInstanceState.getIntegerArrayList("ec");
        art_list = savedInstanceState.getIntegerArrayList("ar");
        max_min = savedInstanceState.getInt("max_min");
        ts = enFraction2(savedInstanceState.getStringArrayList("ts"));
        ti = enFraction(savedInstanceState.getStringArrayList("ti"));

        show_t();
        intent=new Intent(this,MainActivity.class);
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

    public void show_t(int n) {
        if(n<ts.size()-1){
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        tableLayout.removeAllViews();
        LayoutInflater factory = LayoutInflater.from(this);
        String[] var_hbase = var_hbases.get(n);
        int piv_i=piv_ilist.get(n);
        int piv_j=piv_jlist.get(n);
        String[] var_dbase = var_dbases.get(n);
            String[] ratio=ratio_sav.get(n);
        int nbr_cols = var_hbase.length;
        String[][] tf = ts.get(n);
        String[] Mf = Ms.get(n);
        TableRow[] tableRows = new TableRow[nbr_eqt + 2];
        TableRow tableRow = (TableRow) factory.inflate(R.layout.tablerow, null);
        TextView textV = (TextView) factory.inflate(R.layout.textview, null);
        ((TextView) textV.findViewById(R.id.textView)).setText("");
        tableRow.addView(textV);
        for (int i = 0; i < nbr_cols; i++) {
            if(i!=piv_j){TextView textVi = (TextView) factory.inflate(R.layout.textv, null);
            ((TextView) textVi.findViewById(R.id.textView)).setText(var_hbase[i]);
            tableRow.addView(textVi);
        }
            else{
                TextView textVi = (TextView) factory.inflate(R.layout.tablerow2, null);
                ((TextView) textVi.findViewById(R.id.textView)).setText(Html.fromHtml("<b>"+var_hbase[i]+"</b>"));
                tableRow.addView(textVi);
            }
        }
            TextView textVw = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textVw.findViewById(R.id.textView)).setText("");
            tableRow.addView(textVw);
            TextView textVi = (TextView) factory.inflate(R.layout.textv, null);
            ((TextView) textVi.findViewById(R.id.textView)).setText(Html.fromHtml("<b> R </b>"));
            tableRow.addView(textVi);
        tableLayout.addView(tableRow);
        for (int i = 1; i <= nbr_eqt; i++) {
            if (i != piv_i) {
                TextView textView1 = (TextView) factory.inflate(R.layout.textv, null);
                ((TextView) textView1.findViewById(R.id.textView)).setText(var_dbase[i - 1]);
                tableRows[i] = (TableRow) factory.inflate(R.layout.tablerow, null);
                tableRows[i].addView(textView1);
                for (int j = 0; j <= nbr_cols; j++) {
                    if (j != piv_j) {
                        TextView textView = (TextView) factory.inflate(R.layout.textv, null);
                        ((TextView) textView.findViewById(R.id.textView)).setText(" " + tf[i][j] + " ");
                        tableRows[i].addView(textView);
                    } else {
                        TextView textView = (TextView) factory.inflate(R.layout.tablerow2, null);
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b> " + tf[i][j] + " </b>"));
                        tableRows[i].addView(textView);
                    }


                }
                TextView textViewn = (TextView) factory.inflate(R.layout.textv, null);
                ((TextView) textViewn.findViewById(R.id.textView)).setText(ratio[i - 1]);
                tableRows[i].addView(textViewn);

            } else {
                TextView textView1 = (TextView) factory.inflate(R.layout.tablerow2, null);
                ((TextView) textView1.findViewById(R.id.textView)).setText(Html.fromHtml("<b>" + var_dbase[i - 1] + "</b>"));
                tableRows[i] = (TableRow) factory.inflate(R.layout.tablerow, null);
                tableRows[i].addView(textView1);
                for (int j = 0; j <= nbr_cols; j++) {
                        TextView textView = (TextView) factory.inflate(R.layout.tablerow2, null);
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b> " + tf[i][j] + " </b>"));
                        tableRows[i].addView(textView);


                }
                TextView textViewn = (TextView) factory.inflate(R.layout.tablerow2, null);
                ((TextView) textViewn.findViewById(R.id.textView)).setText(Html.fromHtml("<b>" + ratio[i - 1] + "</b>"));
                tableRows[i].addView(textViewn);
            }

            tableLayout.addView(tableRows[i]);

        }
        TextView textView1 = (TextView) factory.inflate(R.layout.textv, null);
        ((TextView) textView1.findViewById(R.id.textView)).setText(" -Z ");
        tableRows[0] = (TableRow) factory.inflate(R.layout.tablerow, null);
        tableRows[0].addView(textView1);
        for (int i = 0; i <= nbr_cols; i++) {
            if(i!=piv_j){
                TextView textView = (TextView) factory.inflate(R.layout.textv, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(" " + Mf[i] + " ");
                tableRows[0].addView(textView);
            }
        else{
                TextView textView = (TextView) factory.inflate(R.layout.tablerow2, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b> " + Mf[i] + " </b>"));
                tableRows[0].addView(textView);
            }
        }

        tableLayout.addView(tableRows[0]);
        TextView ttt=(TextView) findViewById(R.id.numero);
    ttt.setText(Html.fromHtml("Tableau N° :<b>  "+(n+1)+"</b>"));
        }


        //LE second
        else{
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            tableLayout.removeAllViews();
            LayoutInflater factory = LayoutInflater.from(this);
            String[] var_hbase = var_hbases.get(n);
            String[] var_dbase = var_dbases.get(n);

            int nbr_cols = var_hbase.length;
            String[][] tf = ts.get(n);
            String[] Mf = Ms.get(n);
            TableRow[] tableRows = new TableRow[nbr_eqt + 2];
            TableRow tableRow = (TableRow) factory.inflate(R.layout.tablerow, null);
            TextView textV = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textV.findViewById(R.id.textView)).setText("");
            tableRow.addView(textV);
            for (int i = 0; i < nbr_cols; i++) {
             {TextView textVi = (TextView) factory.inflate(R.layout.textv, null);
                    ((TextView) textVi.findViewById(R.id.textView)).setText(var_hbase[i]);
                    tableRow.addView(textVi);
                }
             }
            tableLayout.addView(tableRow);
            for (int i = 1; i <= nbr_eqt; i++) {
                    TextView textView1 = (TextView) factory.inflate(R.layout.textv, null);
                    ((TextView) textView1.findViewById(R.id.textView)).setText(var_dbase[i - 1]);
                    tableRows[i] = (TableRow) factory.inflate(R.layout.tablerow, null);
                    tableRows[i].addView(textView1);
                    for (int j = 0; j <= nbr_cols; j++) {
                              TextView textView = (TextView) factory.inflate(R.layout.textv, null);
                            ((TextView) textView.findViewById(R.id.textView)).setText(" " + tf[i][j] + " ");
                            tableRows[i].addView(textView);



                }

                tableLayout.addView(tableRows[i]);

            }
            TextView textView1 = (TextView) factory.inflate(R.layout.textv, null);
            ((TextView) textView1.findViewById(R.id.textView)).setText(" -Z ");
            tableRows[0] = (TableRow) factory.inflate(R.layout.tablerow, null);
            tableRows[0].addView(textView1);
            for (int i = 0; i <= nbr_cols; i++) {
                    TextView textView = (TextView) factory.inflate(R.layout.textv, null);
                    ((TextView) textView.findViewById(R.id.textView)).setText(" " + Mf[i] + " ");
                    tableRows[0].addView(textView);
            }

            tableLayout.addView(tableRows[0]);
            TextView ttt=(TextView) findViewById(R.id.numero);
            ttt.setText(Html.fromHtml("Tableau N° :<b>  "+(n+1)+"</b>"));
        }
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
    public void show_t() {
        LayoutInflater factory=LayoutInflater.from(this);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayouts);
        show_t(num);
        int nbr_tab=ts.size()-1;
        String[] var_hbase = var_hbases.get(nbr_tab);
        String[] var_dbase = var_dbases.get(nbr_tab);
        int nbr_cols = var_hbase.length;
        String[][] tf = ts.get(nbr_tab);
        String[] Mf = Ms.get(nbr_tab);
 if(degenerescence)
 {
     linearLayout.removeAllViews();
     TextView textView = (TextView) factory.inflate(R.layout.textview1, null);
     ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>Il s'agit d'une degenercence . \nJe me suis volontairement arrêter au \n"+String.valueOf(ts.size())+"ème tableau</b>"));
     linearLayout.addView(textView);

 }
 else if((Mf[nbr_cols].indexOf("M"))>=0){
     linearLayout.removeAllViews();
     TextView textView = (TextView) factory.inflate(R.layout.textview1, null);
     ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>Il s'agit d'une infaisabilité !!</b>"));
     linearLayout.addView(textView);

 }
  else   if(borne){
     linearLayout.removeAllViews();
            TextView textView = (TextView) factory.inflate(R.layout.textview1, null);
            ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>La solution est sans borne !!</b>"));
          linearLayout.addView(textView);
        }
        else{
    for(int i=1;i<=nbr_var;i++){
        TextView textView = (TextView) factory.inflate(R.layout.textview1, null);
        String text="x"+String.valueOf(i);
        if(is_in(var_dbase,text)){
            int k=pos_of(var_dbase,text);
            ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>"+text+" = "+ tf[k+1][nbr_cols]+"</b>"));
        }
        else {
            ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>"+text+" = 0</b>"));

        }
         linearLayout.addView(textView);
    }
            TextView textView = (TextView) factory.inflate(R.layout.textview1, null);
          String tuto;
            if(max_min==0) tuto="Zmax";
            else tuto="Zmin";
                  ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>"+tuto+" = "+(tf[0][nbr_cols]).replace("-","")+"</b>"));
        linearLayout.addView(textView);
        }
        findViewById(R.id.pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) affiche(1);
                else {
                    num -= 1;
                    show_t(num);
                }
            }
        });
        findViewById(R.id.suiv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == ts.size() - 1) affiche(5);
                else {
                    num++;
                    show_t(num);
                }
            }
        });
        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) affiche(1);
                else {
                    num = 0;
                    show_t(num);
                }
            }
        });
        findViewById(R.id.fin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == ts.size() - 1) affiche(5);
                else {
                    num = ts.size() - 1;
                    show_t(num);
                }
            }
        });
        findViewById(R.id.retour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   finish();
               }catch (Exception e){
                   Bundle outState=new Bundle();
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
                outState.putIntegerArrayList("piv_i",piv_ilist);
                outState.putIntegerArrayList("piv_j",piv_jlist);

                outState.putStringArrayList("ts",enTString(ts));
                intent.putExtra("macle",outState);

                startActivity(intent);
               }
                    }


        });
    }

    public void affiche(int k) {
        String string = "";
        if (ts.size() == 1) string = "C'est le SEUL tableau";
        else if (k == 1 || k == 3) string = " C'est déjà le PREMIER tableau";
        else string = " C'est déjà le DERNIER tableau";
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }


}

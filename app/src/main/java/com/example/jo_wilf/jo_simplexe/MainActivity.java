package com.example.jo_wilf.jo_simplexe;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import android.database.sqlite.SQLiteDatabase;
import  com.example.jo_wilf.jo_simplexe.Fraction;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {
    public ArrayList<String[][]> ts = new ArrayList<>();
    public ArrayList<String[]> var_hbases = new ArrayList<>();
    public Intent intent;
    public ArrayList<String[]> var_dbases = new ArrayList<>();
    public ArrayList<String[]> ratio_sav = new ArrayList<>();
    public Fraction[][] ti;
    public ArrayList<Integer> piv_ilist=new ArrayList<>(),piv_jlist=new ArrayList<>();
    public boolean borne = false;
    public ArrayList<String[]> Ms = new ArrayList<>();
    public int num = 0;
    boolean degenerescence = false;
    public ArrayList<Integer> art_list = new ArrayList<Integer>(), egal_list = new ArrayList<Integer>(), ecar_list = new ArrayList<Integer>();
    int nbr_eqt, nbr_var, nbr_col, nbr_ecar, max_min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            setContentView(R.layout.activity);
            init();
            MdpDAO mdpDAO = new MdpDAO(this);
            SQLiteDatabase db=mdpDAO.open();
            Cursor c = db.query("motDePasse", new String[]{"id", "mot"}, null, null, null, null, null);
            if (c.getCount()==0){
                mdpDAO.close();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
            }
            mdpDAO.close();
        } else {
            max_min = savedInstanceState.getInt("max_min");
            nbr_col = savedInstanceState.getInt("nbr_col");
            nbr_eqt = savedInstanceState.getInt("nbr_eqt");
            nbr_var = savedInstanceState.getInt("nbr_var");
            nbr_ecar = savedInstanceState.getInt("nbr_ecar");
            piv_ilist=savedInstanceState.getIntegerArrayList("piv_i");
            piv_jlist=savedInstanceState.getIntegerArrayList("piv_j");
            Ms = enT2FString(savedInstanceState.getStringArrayList("Ms"));
            var_dbases = enT2FString(savedInstanceState.getStringArrayList("var_dbases"));
            var_hbases = enT2FString(savedInstanceState.getStringArrayList("var_hbases"));
            ratio_sav=enT2FString(savedInstanceState.getStringArrayList("ratio"));
            degenerescence = savedInstanceState.getBoolean("is");
            borne = savedInstanceState.getBoolean("borne");
            egal_list = savedInstanceState.getIntegerArrayList("eg");
            ecar_list = savedInstanceState.getIntegerArrayList("ec");
            art_list = savedInstanceState.getIntegerArrayList("ar");
            try {
                ts = enFraction2(savedInstanceState.getStringArrayList("ts"));
            }catch (Exception e){}
                ti = enFraction(savedInstanceState.getStringArrayList("ti"));
            setContentView(R.layout.activity);
                init2();
              }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent=new Intent(this,Main2Activity.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            nbr_eqt = tableLayout.getChildCount() - 1;
            Spinner spinner = (Spinner) findViewById(R.id.spinne);
            max_min = spinner.getSelectedItemPosition();
            TableRow Row = (TableRow) tableLayout.getChildAt(0);
            nbr_var = (Row.getChildCount() - 1) / 2;
            standard(tableLayout, max_min, art_list, egal_list, ecar_list);
            nbr_col = nbr_var + (2 * art_list.size()) - egal_list.size() + ecar_list.size();
            double t[][] = new double[nbr_eqt + 1][nbr_col + 1];
            Fraction tf[][] = new Fraction[nbr_eqt + 1][nbr_col + 1];
            ti = new Fraction[nbr_eqt + 1][nbr_col + 1];
            Fraction[] Mf = new Fraction[nbr_col + 1];
            for (int j = 0; j <= nbr_col; j++) {
                for (int i = 0; i <= nbr_eqt; i++) tf[i][j] = new Fraction();
                Mf[j] = new Fraction();
            }
            int[] var_hbase = new int[nbr_col];
            ArrayList<Integer> var_dbase = new ArrayList<>();
            for (int y = 0; y < nbr_col; y++) {
                var_hbase[y] = y + 1;
            }
            double[] M = new double[nbr_col + 1];
            remplir(tableLayout, nbr_eqt, M, t, Mf, var_dbase, tf, nbr_col, nbr_var, max_min, art_list, egal_list, ecar_list);
            for (int j = 0; j <= nbr_col; j++) {
                for (int i = 0; i <= nbr_eqt; i++) ti[i][j] = tf[i][j];
            }

        outState.putInt("nbr_eqt", nbr_eqt);
        outState.putInt("nbr_var", nbr_var);
        outState.putInt("nbr_col", nbr_col);
        outState.putInt("nbr_ecar", nbr_ecar);
        outState.putInt("max_min", max_min);
        outState.putIntegerArrayList("piv_i", piv_ilist);
        outState.putIntegerArrayList("piv_j",piv_jlist);
        outState.putIntegerArrayList("ec", ecar_list);
        outState.putIntegerArrayList("ar", art_list);
        outState.putIntegerArrayList("eg", egal_list);
        outState.putBoolean("borne", borne);
        outState.putBoolean("is", degenerescence);
        outState.putStringArrayList("ti", enStrin(ti));
        outState.putStringArrayList("var_hbases", enT2String(var_hbases));
        outState.putStringArrayList("ratio", enT2String(ratio_sav));
        outState.putStringArrayList("var_dbases", enT2String(var_dbases));
        outState.putStringArrayList("Ms", enT2String(Ms));
outState.putStringArrayList("ts",enTString(ts));
intent.putExtra("macle",outState);

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

    public ArrayList<String[]> enT2FString(ArrayList<String> tr) {
        ArrayList<String[]> t = new ArrayList<>();
        for (int k = 0; k < tr.size(); k++) {
            StringTokenizer tokenizer = new StringTokenizer(tr.get(k), " ");
            int n=tokenizer.countTokens();
            String string[] = new String[n];
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
                    try {
                        string += t[i][j] + " ";
                    }
                    catch(Exception e){;}
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
if(id==R.id.eq_settings) init();
else {
    LayoutInflater factory = LayoutInflater.from(this);
    AlertDialog.Builder adb = new AlertDialog.Builder(this);
   // adb.setView(alertdialog);
}
        return super.onOptionsItemSelected(item);
    }

    public void text_click(View view) {
        final TextView textView = (TextView) view;
        String text = textView.getText().toString();
        int position = text.indexOf("X");
        int k = text.indexOf("=");
        if ((text != "") && (position == -1) && (k == -1)) {
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertdialog = factory.inflate(R.layout.alert_dialog, null);
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(alertdialog);
            (alertdialog.findViewById(R.id.b1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());

                }
            });
            (alertdialog.findViewById(R.id.b2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());

                }
            });
            (alertdialog.findViewById(R.id.b3)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b4)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b5)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b6)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b7)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b8)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.b9)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.bz)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    textView1.setText(textView1.getText() + "" + ((Button) v).getText());
                }
            });
            (alertdialog.findViewById(R.id.bD)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    String string = textView1.getText().toString();
                    if (string.length() > 0)
                        textView1.setText(string.substring(0, string.length() - 1));
                }
            });
            (alertdialog.findViewById(R.id.bm)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    String string = textView1.getText().toString();
                    if ((string.indexOf("-") < 0) && (!string.equals("0"))) {
                        string = "-" + string;
                        textView1.setText(string);
                    } else if ((!string.equals("0"))) textView1.setText(string.substring(1));
                }
            });
            (alertdialog.findViewById(R.id.bP)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    String string = textView1.getText().toString();
                    int p1 = string.indexOf(".");
                    int p2 = string.indexOf("/");
                    if ((p1 >= 0) || (p2 >= 0))
                        Toast.makeText(getApplicationContext(), "Il n'est pas possible de mettre \".\" et\"/\" dans un nombre", Toast.LENGTH_SHORT).show();
                    else textView1.setText(textView1.getText() + ".");
                }
            });
            (alertdialog.findViewById(R.id.bS)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1 = ((TextView) alertdialog.findViewById(R.id.edit_text1));
                    String string = textView1.getText().toString();
                    int p1 = string.indexOf(".");
                    int p2 = string.indexOf("/");
                    if ((p1 >= 0) || (p2 >= 0))
                        Toast.makeText(getApplicationContext(), "Vous n'êtes pas autorisé à faire cette action ", Toast.LENGTH_SHORT).show();
                    else textView1.setText(textView1.getText() + "/");
                }
            });
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TextView editText = (TextView) alertdialog.findViewById(R.id.edit_text1);

                    try {
                        String string = editText.getText().toString();
                        int p1;
                        p1 = string.indexOf(".");
                        double n;
                        if (p1 > 0) n = Double.parseDouble(editText.getText().toString());
                        else {
                            Fraction fraction = new Fraction(editText.getText().toString());
                            n = (double) fraction.getNumerateur() / fraction.getDenominateur();
                            string = fraction.EnString();
                        }
                        if (n < 0)
                            textView.setText("(" + string + ")");
                        else textView.setText(string);
                    } catch (Exception e) {
                        Toast msg = Toast.makeText(getApplicationContext(), "Le nombre entrer n'est pas valide !!", Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }

            });
            adb.setCancelable(true);
            adb.show();
        }
    }

    public static String traduit_decim(String nombr) {
        nombr = nombr.trim().replace("(","").replace(")","").replace(" ","");
        int p = nombr.indexOf(".");
        if (p > 0) {
            long math = (long) Math.pow(10, nombr.length() - p - 1);
            return ((nombr.substring(0, p) + nombr.substring(p + 1)) + "/" + math);
        } else return nombr;

    }

    public void btn_click(View view) {
        LayoutInflater factory = LayoutInflater.from(this);
        int id = view.getId();
        if (id == R.id.ajout_col) {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            int nbre_lig = tableLayout.getChildCount();
            TableRow tableRow = (TableRow) tableLayout.getChildAt(0);
            TextView tex = (TextView) tableRow.getChildAt(tableRow.getChildCount() - 1);
            String s = "<i><b>" + tex.getText() + " </b></i>+";
            tex.setText(Html.fromHtml(s));
            TextView textVie = (TextView) factory.inflate(R.layout.textview, null);
            s = "<i><b>" + "X" + String.valueOf(((tableRow.getChildCount() - 1) / 2) + 1) + "</b></i>";
            ((TextView) textVie.findViewById(R.id.textView)).setText(Html.fromHtml(s));
            TextView textVi = (TextView) factory.inflate(R.layout.textvie, null);
            TextView ti = ((TextView) textVi.findViewById(R.id.textView));
            ti.setText("0 ");
            tableRow.addView(textVi);
            tableRow.addView(textVie);

            int i;
            for (i = 1; i < nbre_lig; i++) {
                TableRow row = (TableRow) tableLayout.getChildAt(i);
                TextView tex2 = (TextView) row.getChildAt(row.getChildCount() - 3);
                int posi = (tex2.getText()).toString().indexOf("+");
                if (posi == -1)
                    tex2.setText(Html.fromHtml("<i><b>" + tex2.getText() + " </b></i>+"));
                s = "<i><b>" + "X" + String.valueOf((tableRow.getChildCount() - 1) / 2) + "</b></i>";
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(s));
                TextView textV = (TextView) factory.inflate(R.layout.textvie, null);
                TextView t = ((TextView) textV.findViewById(R.id.textView));
                t.setText("0");
                row.addView(textV, row.getChildCount() - 2);

                row.addView(textView, row.getChildCount() - 2);


            }
        } else if (id == R.id.ajout_lign) {

            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            TableRow tableRow = (TableRow) tableLayout.getChildAt(0);
            TableRow row = (TableRow) factory.inflate(R.layout.tablerow, null);
            TextView textVie = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textVie.findViewById(R.id.textView)).setText("");
            int x = (tableRow.getChildCount() - 1) / 2;
            row.addView(textVie);
            for (int i = 1; i <= (2 * x); i++) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                int rest = i % 2;
                if (i == 2 * x) {


                    String b = "<i><b>" + "X" + String.valueOf(i / 2) + "</b></i>";
                    ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(b));
                    row.addView(textView);

                } else if (rest != 0) {
                    TextView te = (TextView) factory.inflate(R.layout.textvie, null);

                    TextView t = ((TextView) te.findViewById(R.id.textView));
                    t.setText("0");
                    row.addView(te);
                } else {

                    String b = "<i><b>X" + String.valueOf(i / 2) + " </b></i>+";
                    ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(b));
                    row.addView(textView);

                }
            }
            TextView textView = (TextView) factory.inflate(R.layout.textvie, null);
            Spinner spinner = (Spinner) factory.inflate(R.layout.spinner, null);
            row.addView(spinner);
            TextView t = ((TextView) textView.findViewById(R.id.textView));
            t.setText("0");
            row.addView(textView);
            tableLayout.addView(row);


        } else if (id == R.id.supr_col) {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            int nbr_l = tableLayout.getChildCount();
            int nbre_x = (row.getChildCount() - 1) / 2;
            if (nbre_x >= 2) {
                row.removeViews(row.getChildCount() - 2, 2);
                TextView tft = (TextView) row.getChildAt(row.getChildCount() - 1);
                String text2 = tft.getText().toString().replace("+", "");
                tft.setText(Html.fromHtml("<i><b>" + text2 + "</b></i>"));
                for (int j = 1; j < nbr_l; j++) {
                    TableLayout tableLayou = (TableLayout) findViewById(R.id.tablelayout);

                    TableRow tableRow = (TableRow) tableLayou.getChildAt(j);
                    tableRow.removeViews(tableRow.getChildCount() - 4, 2);
                    TextView tft1 = (TextView) tableRow.getChildAt(tableRow.getChildCount() - 3);
                    String text1 = tft1.getText().toString().replace("+", "");
                    tft1.setText(Html.fromHtml("<i><b>" + text1 + "</b></i>"));
                    ;

                }
            } else {
                Toast msg = Toast.makeText(this, "Il doit y avoir au \nmoins une variable", Toast.LENGTH_SHORT);
                msg.show();
            }
        } else {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            int nbr_l = tableLayout.getChildCount();
            if (nbr_l > 2) {
                tableLayout.removeViewAt(nbr_l - 1);
            } else {
                Toast msg = Toast.makeText(this, "Il doit y avoir au \nmoins une equation", Toast.LENGTH_SHORT);
                msg.show();
            }


        }

    }

    public void init2() {
        TableRow[] tableRows = new TableRow[nbr_eqt + 1];
        LayoutInflater factory = LayoutInflater.from(this);
        int v = 0;
        tableRows[0] = (TableRow) factory.inflate(R.layout.tablerow, null);
        for (int i = 0; (i < 2 * nbr_var + 1); i++) {
            int j = i % 2;
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            if (i == 2 * nbr_var)
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<i><b>X" + nbr_var + "</b></i>"));
            else if (i == 0)
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>Z =</b>"));
            else if ((j != 0)) {
                textView = (TextView) factory.inflate(R.layout.textvie, null);
                TextView t = ((TextView) textView.findViewById(R.id.textView));
                t.setText(ti[0][v].EnString());
                v++;
            } else {
                String tt = "<i><b>X" + String.valueOf(i / 2) + " </b></i>+";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
            }
            tableRows[0].addView(textView);
        }
        for (int k = 1; k < nbr_eqt + 1; k++) {
            tableRows[k] = (TableRow) factory.inflate(R.layout.tablerow, null);
            v = 0;
            for (int i = 0; i <= 2 * nbr_var + 2; i++) {
                int j = i % 2;
                if (i == 2 * nbr_var + 1) {
                    Spinner spinner = (Spinner) factory.inflate(R.layout.spinner, null);
                    tableRows[k].addView(spinner);
                    if (is_in(ecar_list, k)) spinner.setSelection(0);
                    else if (is_in(egal_list, k)) spinner.setSelection(1);
                    else spinner.setSelection(2);
                } else if (i == 2 * nbr_var + 2) {
                    TextView textView = (TextView) factory.inflate(R.layout.textvie, null);
                    TextView t = ((TextView) textView.findViewById(R.id.textView));
                    t.setText(ti[k][nbr_col].EnString());
                    tableRows[k].addView(textView);
                } else {
                    TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                    if (i == 2*nbr_var)
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<i><b>X" + nbr_var + "</b></i>"));
                    else if (i == 0)
                        ((TextView) textView.findViewById(R.id.textView)).setText("");
                    else if ((j != 0)) {
                        textView = (TextView) factory.inflate(R.layout.textvie, null);
                        TextView t = ((TextView) textView.findViewById(R.id.textView));
                        t.setText(ti[k][v].EnString());
                        v++;
                    } else {
                        String tt = "<i><b>X" + String.valueOf(i / 2) + " </b></i>+";
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                    }
                    tableRows[k].addView(textView);
                }
            }

        }
        Spinner spinner = (Spinner) findViewById(R.id.spinne);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        tableLayout.removeAllViews();
        for (int i = 0; i < nbr_eqt + 1; i++) {

            tableLayout.addView(tableRows[i], new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        spinner.setSelection(max_min);
    }

    public void init() {
        TableRow[] tableRows = new TableRow[3];
        LayoutInflater factory = LayoutInflater.from(this);
        tableRows[0] = (TableRow) factory.inflate(R.layout.tablerow, null);
        for (int i = 0; i < 5; i++) {
            int j = i % 2;
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            if (i == 4)
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<i><b>X2</b></i>"));
            else if (i == 0)
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<b>Z =</b>"));
            else if ((j != 0)) {
                textView = (TextView) factory.inflate(R.layout.textvie, null);
                TextView t = ((TextView) textView.findViewById(R.id.textView));
                t.setText("0");
            } else {
                String tt = "<i><b>X" + String.valueOf(i / 2) + " </b></i>+";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
            }
            tableRows[0].addView(textView);
        }
        for (int k = 1; k < 3; k++) {
            tableRows[k] = (TableRow) factory.inflate(R.layout.tablerow, null);
            for (int i = 0; i <= 6; i++) {
                int j = i % 2;
                if (i == 5) {
                    Spinner spinner = (Spinner) factory.inflate(R.layout.spinner, null);
                    tableRows[k].addView(spinner);
                } else if (i == 6) {
                    TextView textView = (TextView) factory.inflate(R.layout.textvie, null);
                    TextView t = ((TextView) textView.findViewById(R.id.textView));
                    t.setText("0");
                    tableRows[k].addView(textView);
                } else {
                    TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                    if (i == 4)
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("<i><b>X2</b></i>"));
                    else if (i == 0)
                        ((TextView) textView.findViewById(R.id.textView)).setText("");
                    else if ((j != 0)) {
                        textView = (TextView) factory.inflate(R.layout.textvie, null);
                        TextView t = ((TextView) textView.findViewById(R.id.textView));
                        t.setText("0");
                    } else {
                        String tt = "<i><b>X" + String.valueOf(i / 2) + " </b></i>+";
                        ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                    }
                    tableRows[k].addView(textView);
                }
            }
        }
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        tableLayout.removeAllViews();
        for (int i = 0; i < 3; i++) {

            tableLayout.addView(tableRows[i], new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    public double endouble(Fraction e) {
        return (double) e.Numerateur / e.Denominateur;
    }

    public int position(int[] t, int nbr) {
        int i = 0;
        for (int k = 0; k < t.length; k++) {
            if (t[k] == nbr) {
                i = k;
                k = t.length;
            }
        }
        return i;
    }



    public void execute_click(View v) {
        try {
            ts.clear();
            degenerescence = false;
            borne = false;
            piv_ilist.clear();
            int tab_maxi = 0;
            piv_jlist.clear();
            Ms.clear();
            ecar_list.clear();
            egal_list.clear();
            art_list.clear();
            var_dbases.clear();
            var_hbases.clear();
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            nbr_eqt = tableLayout.getChildCount() - 1;
            Spinner spinner = (Spinner) findViewById(R.id.spinne);
            max_min = spinner.getSelectedItemPosition();
            TableRow Row = (TableRow) tableLayout.getChildAt(0);
            nbr_var = (Row.getChildCount() - 1) / 2;
            standard(tableLayout, max_min, art_list, egal_list, ecar_list);
            nbr_col = nbr_var + (2 * art_list.size()) - egal_list.size() + ecar_list.size();
            double t[][] = new double[nbr_eqt + 1][nbr_col + 1];
            Fraction tf[][] = new Fraction[nbr_eqt + 1][nbr_col + 1];
            ti = new Fraction[nbr_eqt + 1][nbr_col + 1];
            Fraction[] Mf = new Fraction[nbr_col + 1];
            for (int j = 0; j <= nbr_col; j++) {
                for (int i = 0; i <= nbr_eqt; i++) tf[i][j] = new Fraction();
                Mf[j] = new Fraction();
            }
            int[] var_hbase = new int[nbr_col];
            ArrayList<Integer> var_dbase = new ArrayList<>();
            for (int y = 0; y < nbr_col; y++) {
                var_hbase[y] = y + 1;
            }
            double[] M = new double[nbr_col + 1];
            remplir(tableLayout, nbr_eqt, M, t, Mf, var_dbase, tf, nbr_col, nbr_var, max_min, art_list, egal_list, ecar_list);
            nbr_ecar = ecar_list.size() + art_list.size() - egal_list.size();
            ts.add(enString(tf));
            var_dbases.add(enString(var_dbase));
            var_hbases.add(enString(var_hbase, 0));
            Ms.add(enString(tf, Mf, t, M));
            for (int j = 0; j <= nbr_col; j++) {
                for (int i = 0; i <= nbr_eqt; i++) ti[i][j] = tf[i][j];
            }
            boolean is_m = false;
            int isi = 0;
            int nbr_tablo = 0;
            tab_maxi = nbr_eqt * nbr_var * 10;
            if (max_min == 0) {
                boolean controlleur_Z = control_Z(t, nbr_col, 0), controlleur_M = control_M(M, 0, 0);
                int piv_j, piv_i, suprime = 0;
                double[] ratio = new double[nbr_eqt];
                Fraction[] ratiof = new Fraction[nbr_eqt];
                Fraction[] col_ratiof = new Fraction[nbr_eqt + 1];
                double[] col_ratio = new double[nbr_eqt + 1];
                while (((controlleur_M || controlleur_Z)) && (nbr_tablo < tab_maxi)) {
                    if (controlleur_M) {
                        piv_j = minimum(M, nbr_col - suprime, 0);
                        double joc, jocelin = M[piv_j];
                        boolean pivoteur = control_pivo(M, nbr_col - suprime, jocelin);
                        if (pivoteur) {
                            ArrayList<Integer> tablot = new ArrayList<>();
                            for (int z = 0; z < nbr_col - suprime; z++) {
                                if (M[z] == jocelin) tablot.add(z);
                            }
                            piv_j = tablot.get(0);
                            joc = t[0][piv_j];
                            for (int z = 1; z < tablot.size(); z++) {
                                int piv2 = tablot.get(z);
                                if (t[0][piv2] > joc) {
                                    piv_j = piv2;
                                    joc = t[0][piv_j];
                                }
                            }
                        }
                    } else {
                        is_m = true;
                        isi++;
                        piv_j = minimum2(t, nbr_col - suprime, 0);
                    }
                    for (int y = 1; y <= nbr_eqt; y++) {
                        ratiof[y - 1] = tf[y][piv_j];
                        ratio[y - 1] = endouble(ratiof[y - 1]);
                    }
                    for (int y = 0; y <= nbr_eqt; y++) {
                        col_ratio[y] = t[y][piv_j];
                        col_ratiof[y] = tf[y][piv_j];
                    }
                    for (int y = 0; y < nbr_eqt; y++) {
                        if (ratio[y] != 0) {
                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                            ratio[y] = endouble(ratiof[y]);
                        }
                    }
                    boolean controlleur_ratio = control_ratio(ratio);
                    if (controlleur_ratio) {
                        piv_i = mini_ratio(ratio);
                        int save_piv = piv_j;
                        ArrayList<Integer> piv_non = new ArrayList<>();

                        int nbr_count = sign_count(M, suprime, max_min);
                        if ((suprime < art_list.size()) && (controlleur_M))
                            while (((var_dbase.get(piv_i - 1) <= (nbr_var + nbr_ecar)) && (controlleur_M))) {
                                piv_non.add(piv_j);
                                if (piv_non.size() == nbr_count) {
                                    piv_j = save_piv;
                                    for (int y = 1; y <= nbr_eqt; y++) {
                                        ratiof[y - 1] = tf[y][piv_j];
                                        ratio[y - 1] = endouble(ratiof[y - 1]);
                                    }
                                    for (int y = 0; y <= nbr_eqt; y++) {
                                        col_ratio[y] = t[y][piv_j];
                                        col_ratiof[y] = tf[y][piv_j];
                                    }
                                    for (int y = 0; y < nbr_eqt; y++) {
                                        if (ratio[y] != 0) {
                                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                                            ratio[y] = endouble(ratiof[y]);
                                        }
                                    }
                                    controlleur_M = false;
                                    piv_i = mini_ratio(ratio);
                                } else {
                                    piv_j = minimum(M, nbr_col - suprime, piv_non, 0);
                                    double joc, jocelin = M[piv_j];
                                    boolean pivoteur = control_pivo(M, piv_non, nbr_col - suprime, jocelin);
                                    if (pivoteur) {
                                        ArrayList<Integer> tablot = new ArrayList<>();
                                        for (int z = 0; z < nbr_col - suprime; z++) {
                                            if ((M[z] == jocelin) && (!is_in(piv_non, z)))
                                                tablot.add(z);
                                        }
                                        piv_j = tablot.get(0);
                                        joc = t[0][piv_j];
                                        for (int z = 1; z < tablot.size(); z++) {
                                            int piv2 = tablot.get(z);
                                            if (t[0][piv2] < joc) {
                                                piv_j = piv2;
                                                joc = t[0][piv_j];
                                            }

                                        }
                                    }
                                    for (int y = 1; y <= nbr_eqt; y++) {
                                        ratiof[y - 1] = tf[y][piv_j];
                                        ratio[y - 1] = endouble(ratiof[y - 1]);
                                    }
                                    for (int y = 0; y <= nbr_eqt; y++) {
                                        col_ratio[y] = t[y][piv_j];
                                        col_ratiof[y] = tf[y][piv_j];
                                    }
                                    for (int y = 0; y < nbr_eqt; y++) {
                                        if (ratio[y] != 0) {
                                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                                            ratio[y] = endouble(ratiof[y]);
                                        }
                                    }
                                    controlleur_ratio = control_ratio(ratio);
                                    if (controlleur_ratio)
                                        piv_i = mini_ratio(ratio);

                                }
                            }
                        if (var_dbase.get(piv_i - 1) > (nbr_var + nbr_ecar)) {
                            int l = var_dbase.get(piv_i - 1);
                            int pos = position(var_hbase, l);
                            for (int i = 0; i <= nbr_eqt; i++) {
                                for (int j = pos; j < nbr_col; j++) {
                                    t[i][j] = t[i][j + 1];
                                    tf[i][j] = tf[i][j + 1];
                                }
                            }
                            for (int j = pos; j < nbr_col; j++) {
                                if (j != nbr_col - 1) var_hbase[j] = var_hbase[j + 1];
                                M[j] = M[j + 1];
                                Mf[j] = Mf[j + 1];
                            }
                            suprime++;
                        }
                        var_dbase.remove(piv_i - 1);
                        var_dbase.add(piv_i - 1, var_hbase[piv_j]);

                        Fraction pivotf = tf[piv_i][piv_j];
                        for (int y = 0; y <= nbr_col; y++) {
                            tf[piv_i][y] = Fraction.divide(tf[piv_i][y], pivotf);
                            t[piv_i][y] = endouble(tf[piv_i][y]);
                        }
                        for (int y = 0; y <= nbr_eqt; y++) {
                            if (y != piv_i) {
                                for (int z = 0; z <= nbr_col; z++) {
                                    tf[y][z] = Fraction.substract(tf[y][z], Fraction.multiply(col_ratiof[y], tf[piv_i][z]));
                                    t[y][z] = endouble(tf[y][z]);
                                }
                            }
                        }
                        Fraction pivotMf = Mf[piv_j];

                        if ((!is_m) && (isi <= 1)) {
                            for (int z = 0; z <= nbr_col; z++) {
                                Mf[z] = Fraction.substract(Mf[z], Fraction.multiply(pivotMf, tf[piv_i][z]));
                                M[z] = endouble(Mf[z]);
                            }
                        }
                        nbr_tablo++;
                        piv_ilist.add(piv_i);
                        piv_jlist.add(piv_j);
                        ts.add(enString(tf));
                        var_dbases.add(enString(var_dbase));
                        var_hbases.add(enString(var_hbase, suprime));
                        Ms.add(enString(tf, Mf, t, M));
                        ratio_sav.add(enStr_rat(ratiof));
                        controlleur_Z = control_Z(t, nbr_col - suprime, 0);
                        controlleur_M = control_M(M, suprime, 0);
                    } else {
                        borne = true;
                        controlleur_M = false;
                        controlleur_Z = false;
                    }
                    if (nbr_tablo == tab_maxi) degenerescence = true;
                }
            } else {
                boolean controlleur_Z = control_Z(t, nbr_col, 1), controlleur_M = control_M(M, 0, 1);

                int piv_j, piv_i, suprime = 0;
                double[] ratio = new double[nbr_eqt];
                Fraction[] ratiof = new Fraction[nbr_eqt];
                Fraction[] col_ratiof = new Fraction[nbr_eqt + 1];
                double[] col_ratio = new double[nbr_eqt + 1];
                while ((controlleur_M || controlleur_Z) && (nbr_tablo < tab_maxi)) {
                    if (controlleur_M) {
                        piv_j = minimum(M, nbr_col - suprime, 1);
                        double joc, jocelin = M[piv_j];
                        boolean pivoteur = control_pivo(M, nbr_col - suprime, jocelin);
                        if (pivoteur) {
                            ArrayList<Integer> tablot = new ArrayList<>();
                            for (int z = 0; z < nbr_col - suprime; z++) {
                                if (M[z] == jocelin) tablot.add(z);
                            }
                            piv_j = tablot.get(0);
                            joc = t[0][piv_j];
                            for (int z = 1; z < tablot.size(); z++) {
                                int piv2 = tablot.get(z);
                                if (t[0][piv2] < joc) {
                                    piv_j = piv2;
                                    joc = t[0][piv_j];
                                }

                            }
                        }
                    } else {
                        is_m = true;
                        isi++;
                        piv_j = minimum2(t, nbr_col - suprime, 1);
                    }
                    for (int y = 1; y <= nbr_eqt; y++) {
                        ratiof[y - 1] = tf[y][piv_j];
                        ratio[y - 1] = endouble(ratiof[y - 1]);
                    }
                    for (int y = 0; y <= nbr_eqt; y++) {
                        col_ratio[y] = t[y][piv_j];
                        col_ratiof[y] = tf[y][piv_j];
                    }
                    for (int y = 0; y < nbr_eqt; y++) {
                        if (ratio[y] != 0) {
                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                            ratio[y] = endouble(ratiof[y]);
                        }
                    }
                    boolean controlleur_ratio = control_ratio(ratio);
                    if (controlleur_ratio) {
                        piv_i = mini_ratio(ratio);
                        int save_piv = piv_j;
                        ArrayList<Integer> piv_non = new ArrayList<>();

                        int nbr_count = sign_count(M, suprime, max_min);
                        if ((suprime < art_list.size()) && (controlleur_M))
                            while (((var_dbase.get(piv_i - 1) <= (nbr_var + nbr_ecar)) && (controlleur_M))) {
                                piv_non.add(piv_j);
                                if (piv_non.size() == nbr_count) {
                                    piv_j = save_piv;

                                    for (int y = 1; y <= nbr_eqt; y++) {
                                        ratiof[y - 1] = tf[y][piv_j];
                                        ratio[y - 1] = endouble(ratiof[y - 1]);
                                    }
                                    for (int y = 0; y <= nbr_eqt; y++) {
                                        col_ratio[y] = t[y][piv_j];
                                        col_ratiof[y] = tf[y][piv_j];
                                    }
                                    for (int y = 0; y < nbr_eqt; y++) {
                                        if (ratio[y] != 0) {
                                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                                            ratio[y] = endouble(ratiof[y]);
                                        }
                                    }
                                    piv_i = mini_ratio(ratio);
                                    controlleur_M = false;
                                } else {
                                    piv_j = minimum(M, nbr_col - suprime, piv_non, 1);
                                    double joc, jocelin = M[piv_j];
                                    boolean pivoteur = control_pivo(M, piv_non, nbr_col - suprime, jocelin);
                                    if (pivoteur) {
                                        ArrayList<Integer> tablot = new ArrayList<>();
                                        for (int z = 0; z < nbr_col - suprime; z++) {
                                            if ((M[z] == jocelin) && (!is_in(piv_non, z)))
                                                tablot.add(z);
                                        }
                                        piv_j = tablot.get(0);
                                        joc = t[0][piv_j];
                                        for (int z = 1; z < tablot.size(); z++) {
                                            int piv2 = tablot.get(z);
                                            if (t[0][piv2] < joc) {
                                                piv_j = piv2;
                                                joc = t[0][piv_j];
                                            }

                                        }
                                    }
                                    for (int y = 1; y <= nbr_eqt; y++) {
                                        ratiof[y - 1] = tf[y][piv_j];
                                        ratio[y - 1] = endouble(ratiof[y - 1]);
                                    }
                                    for (int y = 0; y <= nbr_eqt; y++) {
                                        col_ratio[y] = t[y][piv_j];
                                        col_ratiof[y] = tf[y][piv_j];
                                    }
                                    for (int y = 0; y < nbr_eqt; y++) {
                                        if (ratio[y] != 0) {
                                            ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                                            ratio[y] = endouble(ratiof[y]);
                                        }
                                    }
                                    controlleur_ratio = control_ratio(ratio);
                                    if (controlleur_ratio)
                                        piv_i = mini_ratio(ratio);

                                }
                            }
                        for (int y = 1; y <= nbr_eqt; y++) {
                            ratiof[y - 1] = tf[y][piv_j];
                            ratio[y - 1] = endouble(ratiof[y - 1]);
                        }
                        for (int y = 0; y <= nbr_eqt; y++) {
                            col_ratio[y] = t[y][piv_j];
                            col_ratiof[y] = tf[y][piv_j];
                        }
                        for (int y = 0; y < nbr_eqt; y++) {
                            if (ratio[y] != 0) {
                                ratiof[y] = Fraction.divide(tf[y + 1][nbr_col], ratiof[y]);
                                ratio[y] = endouble(ratiof[y]);
                            }
                        }
                        if (var_dbase.get(piv_i - 1) > (nbr_var + nbr_ecar)) {
                            int l = var_dbase.get(piv_i - 1);
                            int pos = position(var_hbase, l);
                            for (int i = 0; i <= nbr_eqt; i++) {
                                for (int j = pos; j < nbr_col; j++) {
                                    t[i][j] = t[i][j + 1];
                                    tf[i][j] = tf[i][j + 1];
                                }
                            }
                            for (int j = pos; j < nbr_col; j++) {
                                if (j != nbr_col - 1) var_hbase[j] = var_hbase[j + 1];
                                M[j] = M[j + 1];
                                Mf[j] = Mf[j + 1];
                            }
                            suprime++;
                        }
                        var_dbase.remove(piv_i - 1);
                        var_dbase.add(piv_i - 1, var_hbase[piv_j]);

                        Fraction pivotf = tf[piv_i][piv_j];
                        for (int y = 0; y <= nbr_col; y++) {
                            tf[piv_i][y] = Fraction.divide(tf[piv_i][y], pivotf);
                            t[piv_i][y] = endouble(tf[piv_i][y]);
                        }
                        for (int y = 0; y <= nbr_eqt; y++) {
                            if (y != piv_i) {
                                for (int z = 0; z <= nbr_col; z++) {
                                    tf[y][z] = Fraction.substract(tf[y][z], Fraction.multiply(col_ratiof[y], tf[piv_i][z]));
                                    t[y][z] = endouble(tf[y][z]);
                                }
                            }
                        }
                        Fraction pivotMf = Mf[piv_j];

                        if ((!is_m) && (isi <= 1)) {
                            for (int z = 0; z <= nbr_col; z++) {
                                Mf[z] = Fraction.substract(Mf[z], Fraction.multiply(pivotMf, tf[piv_i][z]));
                                M[z] = endouble(Mf[z]);
                            }
                        }
                        nbr_tablo++;
                        piv_ilist.add(piv_i);
                        piv_jlist.add(piv_j);
                        ts.add(enString(tf));
                        var_dbases.add(enString(var_dbase));
                        var_hbases.add(enString(var_hbase, suprime));
                        Ms.add(enString(tf, Mf, t, M));
                        ratio_sav.add(enStr_rat(ratiof));
                        controlleur_Z = control_Z(t, nbr_col - suprime, 1);
                        controlleur_M = control_M(M, suprime, 1);

                    } else {
                        borne = true;
                        controlleur_M = false;
                        controlleur_Z = false;

                    }

                    if (nbr_tablo == tab_maxi) degenerescence = true;
                }

            }
            Bundle outState = new Bundle();
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
            outState.putStringArrayList("ratio", enT2String(ratio_sav));
            outState.putStringArrayList("Ms", enT2String(Ms));
            outState.putIntegerArrayList("piv_i", piv_ilist);
            outState.putIntegerArrayList("piv_j", piv_jlist);

            outState.putStringArrayList("ts", enTString(ts));
            intent.putExtra("macle", outState);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this,"Vérifier vos nombres",Toast.LENGTH_SHORT).show();
        }
    }

    public String[] enString(Fraction[][] tf, Fraction[] Mf, double[][] t, double[] M) {
        String[] mot = new String[nbr_col + 1];
        for (int y = 0; y <= nbr_col; y++) {
            if ((M[y] < 0) && (M[y] != -1)) {
                mot[y] = Mf[y].EnString() + "M";
            } else if ((M[y] > 0) && (M[y] != 1)) {
                mot[y] = "+" + Mf[y].EnString() + "M";
            } else if (M[y] == 1) {
                mot[y] = "+M";
            } else if (M[y] == -1) {
                mot[y] = "-M";
            } else if (M[y] == 0) {
                mot[y] = "";
            }
        }
        for (int y = 0; y <= nbr_col; y++) {
            String str = "";
            if (t[0][y] != 0) {
                str = tf[0][y].EnString() + mot[y];
            } else if ((t[0][y] == 0) && (M[y] < 0)) {
                str = mot[y];
            } else if ((t[0][y] == 0) && (M[y] > 0) && (M[y] != 1)) {
                str = Mf[y].EnString() + "M";
            } else if ((t[0][y] == 0) && (M[y] == 1)) {
                str = "M";
            } else if ((t[0][y] == 0) && (M[y] == 0)) {
                str = "0";
            }
            mot[y] = str;
        }
        return mot;
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

    public String[] enString(int[] var_hbase, int sup) {

        String[] tr = new String[nbr_col - sup];
        for (int i = 0; i < nbr_col - sup; i++) {
            int n = var_hbase[i];
            String string;
            if (n <= nbr_var) {
                string = "x" + n;
            } else if (n <= (nbr_ecar + nbr_var)) {
                string = "e" + (n - nbr_var);
            } else {
                string = "a" + (n - nbr_var - nbr_ecar);
            }
            tr[i] = string;
        }
        return tr;
    }

    public String[][] enString(Fraction[][] t) {
        String[][] tr = new String[nbr_eqt + 1][nbr_col + 1];
        for (int i = 0; i <= nbr_col; i++) {
            for (int j = 0; j <= nbr_eqt; j++) {
                tr[j][i] = t[j][i].EnString();
            }
        }
        return tr;
    }
     public String[] enStr_rat(Fraction[] fraction){
         String[] resultat= new String [nbr_eqt];
         for(int i=0;i<nbr_eqt;i++){
             if(endouble(fraction[i])>0) resultat[i]=fraction[i].EnString();
             else resultat[i]="?";
         }
         return resultat;
     }
    public String[] enString(ArrayList<Integer> var_dbase) {
        String[] renvoi = new String[nbr_eqt];

        for (int i = 1; i <= nbr_eqt; i++) {
            int n = var_dbase.get(i - 1);
            String string;
            if (n <= nbr_var) {
                string = "x" + n;
            } else if (n <= (nbr_ecar + nbr_var)) {
                string = "e" + (n - nbr_var);
            } else {
                string = "a" + (n - nbr_var - nbr_ecar);
            }
            renvoi[i - 1] = string;
        }
        return renvoi;
    }

    public int sign_count(double[] M, int sup, int mm) {
        int k = 0;
        if (mm == 0) {

            for (int i = 0; i < M.length - sup; i++)
                if (M[i] > 0) k++;
        } else {
            for (int i = 0; i < M.length - sup; i++)
                if (M[i] < 0) k++;
        }
        return k;
    }

    public static int mini_ratio(double[] t) {
        int k = 0, i = 0, j = t.length;
        double mini = 0;
        while (i < j) {
            if (t[i] > 0) {
                mini = t[i];
                k = i;
                i = j;
            }
            i++;
        }
        for (int z = k + 1; z < j; z++) {
            if ((t[z] > 0) && (t[z] < mini)) {
                mini = t[z];
                k = z;
            }
        }
        return k + 1;
    }

    public static boolean control_ratio(double[] t) {
        boolean renvoi = false;
        int z;
        for (z = 0; z < t.length; z++) {
            if (t[z] > 0) {
                renvoi = true;
            }
        }
        return renvoi;
    }

    public static int minimum2(double[][] t, int j, int max_min) {
        int k = 0;
        double maxi = t[0][0];
        if (max_min == 1) {
            for (int i = 1; i < j; i++) {
                if (t[0][i] < maxi) {
                    maxi = t[0][i];
                    k = i;
                }
            }
        } else {
            for (int i = 1; i < j; i++) {
                if (t[0][i] > maxi) {
                    maxi = t[0][i];
                    k = i;
                }
            }
        }
        return k;
    }

    public static boolean control_pivo(double[] t, ArrayList<Integer> r, int j, double n) {
        int l = 0;
        for (int i = 0; i < j; i++) {
            if ((t[i] == n) && (!is_in(r, i))) {
                l++;
            }
        }
        return (l > 1);
    }

    public static boolean control_pivo(double[] t, int j, double n) {
        int l = 0;
        for (int i = 0; i < j; i++) {
            if (t[i] == n) {
                l++;
            }
        }
        return (l > 1);
    }

    public static int minimum(double[] t, int j, ArrayList<Integer> r, int max_min) {
        int k = 0, i = 0;
        double mini = 0;
        while (i < j) {
            if (!is_in(r, i)) {
                mini = t[i];
                k = i;
                i = j;
            }
            i++;
        }
        if (max_min == 0) {
            for (int z = k + 1; z < j; z++) {
                if ((t[z] > mini) && (!is_in(r, z))) {
                    mini = t[z];
                    k = z;
                }
            }
        } else {
            for (int z = k + 1; z < j; z++) {
                if ((t[z] < mini) && (!is_in(r, z))) {
                    mini = t[z];
                    k = z;
                }
            }
        }
        return k;
    }

    public static int minimum(double[] t, int j, int max_min) {

        int k = 0;
        double mini = t[0];
        if (max_min == 0) {
            for (int i = 1; i < j; i++) {
                if (t[i] > mini) {
                    mini = t[i];
                    k = i;
                }
            }
        } else {
            for (int i = 1; i < j; i++) {
                if (t[i] < mini) {
                    mini = t[i];
                    k = i;
                }
            }
        }
        return k;
    }

    public static boolean control_M(double[] M, int suppr, int max_min) {
        boolean renvoi = false;
        int z;
        int j = M.length - 1 - suppr;
        if (max_min == 1) {
            for (z = 0; z < j; z++)
                if (M[z] < 0) {
                    renvoi = true;
                    z = j;
                }
        } else {

            for (z = 0; z < j; z++)
                if (M[z] > 0) {
                    renvoi = true;
                    z = j;
                }
        }
        return renvoi;
    }

    public static boolean control_Z(double[][] t, int j, int max_min) {
        boolean renvoi = false;
        int z;

        if (max_min == 0) {
            for (z = 0; z < j; z++) {
                if (t[0][z] > 0) {
                    renvoi = true;
                }
            }
        } else {
            for (z = 0; z < j; z++) {
                if (t[0][z] < 0) {
                    renvoi = true;
                }
            }
        }
        return renvoi;
    }

    public void remplir(ViewGroup viewGroup, int nbr_eqt, double[] M, double[][] t, Fraction[] Mf, ArrayList<Integer> var_dbase, Fraction[][] tf, int nbr_col, int nbr_var, int max_min, ArrayList<Integer> art_list, ArrayList<Integer> egal_list, ArrayList<Integer> ecar_list) {
        LayoutInflater factory = LayoutInflater.from(this);
        TableLayout tableLayout = (TableLayout) viewGroup;
        int ligne = tableLayout.getChildCount();
        TableRow[] tableRows = new TableRow[ligne];
        int ec = 0, eg = ecar_list.size() + art_list.size() - egal_list.size(), em = eg;
        tableRows[0] = (TableRow) tableLayout.getChildAt(0);
        int k = 0;
        for (int i = 1; i < (2 * nbr_var + 1); i += 2) {
            TextView textView = (TextView) tableRows[0].getChildAt(i);
            String text = textView.getText().toString();
            if (text.equals("")) {
                TextView textVie = (TextView) tableRows[0].getChildAt(i + 1);
                String tex = textVie.getText().toString();
                if (!tex.equals("")) {
                    tf[0][i - 1 - k] = new Fraction("1");
                    k++;
                } else {
                    tf[0][i - 1 - k] = new Fraction();
                    k++;
                }
            } else {

                tf[0][i - 1 - k] = new Fraction(traduit_decim(text.replace("+", "")).trim());
                k++;

            }
        }
        for (int i = 1; i < tableRows.length; i++) {
            tableRows[i] = (TableRow) tableLayout.getChildAt(i);
            int z = 0;
            for (int j = 1; j < (2 * nbr_var + 1); j += 2) {
                TextView textView = (TextView) tableRows[i].getChildAt(j);
                String text = textView.getText().toString();
                if (text.equals("")) {
                    TextView textVie = (TextView) tableRows[i].getChildAt(j + 1);
                    String tex = textVie.getText().toString();
                    if (!tex.equals("")) {
                        tf[i][j - 1 - z] = new Fraction("1");
                        z++;
                    } else {
                        tf[i][j - 1 - z] = new Fraction();
                        z++;
                    }
                } else {
                    tf[i][j - 1 - z] = new Fraction(traduit_decim(text.replace("+", "")).trim());
                    z++;
                }
            }


            boolean is_ecart, is_egal;
            is_ecart = is_in(ecar_list, i);
            is_egal = is_in(egal_list, i);
            if (is_ecart) {
                tf[i][nbr_var + ec] = new Fraction("1");
                var_dbase.add(nbr_var + ec + 1);
                ec++;
            } else if (is_egal) {
                tf[i][nbr_var + eg] = new Fraction("1");
                var_dbase.add(nbr_var + eg + 1);
                eg++;
            } else {

                tf[i][nbr_var + ec] = new Fraction("-1");
                ec++;
                tf[i][nbr_var + eg] = new Fraction("1");

                var_dbase.add(nbr_var + eg + 1);
                eg++;
            }

            int childCount = tableRows[i].getChildCount();
            TextView textView = (TextView) tableRows[i].getChildAt(childCount - 1);
            tf[i][nbr_col]= new Fraction(traduit_decim(textView.getText().toString()));

        }
        for (int j = 0; j < nbr_col - (eg - em); j++) {
            Fraction m = new Fraction();
            for (int i = 0; i < art_list.size(); i++) {
                m = Fraction.sum(tf[art_list.get(i)][j], m);
            }
            if (max_min == 0) Mf[j] = m;
            else Mf[j] = Fraction.multiply(new Fraction("-1"), m);
        }
        Fraction m = new Fraction();
        for (int i = 0; i < art_list.size(); i++) {
            m = Fraction.sum(tf[art_list.get(i)][nbr_col], m);
        }
        if (max_min == 0) Mf[nbr_col] = m;
        else Mf[nbr_col] = Fraction.multiply(new Fraction("-1"), m);
        TableRow tableRow = (TableRow) factory.inflate(R.layout.tablerownoir, null);
        TextView textVie = (TextView) factory.inflate(R.layout.textview, null);
        ((TextView) textVie.findViewById(R.id.textView)).setText("");
        tableRow.addView(textVie);
        int nbr_ecar = ecar_list.size() + art_list.size() - egal_list.size();
        for (int i = 0; i < nbr_var; i++) {
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textView.findViewById(R.id.textView)).setText("x" + String.valueOf(i + 1));
            tableRow.addView(textView);
        }

        for (int i = 0; i < nbr_ecar; i++) {
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textView.findViewById(R.id.textView)).setText("e" + String.valueOf(i + 1));
            tableRow.addView(textView);
        }
        for (int i = 0; i < art_list.size(); i++) {
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            ((TextView) textView.findViewById(R.id.textView)).setText("a" + String.valueOf(i + 1));
            tableRow.addView(textView);
        }
        for (int i = 1; i < ligne; i++) {
            tableRows[i] = (TableRow) factory.inflate(R.layout.tablerownoir, null);
            TextView textVi = (TextView) factory.inflate(R.layout.textview, null);
            int n = var_dbase.get(i - 1);
            String string;
            if (n <= nbr_var) {
                string = "x" + n;
            } else if (n <= (nbr_ecar + nbr_var)) {
                string = "e" + (n - nbr_var);
            } else {
                string = "a" + (n - nbr_var - nbr_ecar);
            }
            ((TextView) textVi.findViewById(R.id.textView)).setText(string);
            tableRows[i].addView(textVi);
            for (int j = 0; j < nbr_col + 1; j++) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(tf[i][j].EnString());
                tableRows[i].addView(textView);
            }
        }
        tableRows[0] = (TableRow) factory.inflate(R.layout.tablerownoir, null);
        TextView textView1 = (TextView) factory.inflate(R.layout.textview, null);
        ((TextView) textView1.findViewById(R.id.textView)).setText("-Z");
        tableRows[0].addView(textView1);
        for (int j = 0; j <= nbr_col; j++) {
            for (int i = 0; i <= nbr_eqt; i++) t[i][j] = endouble(tf[i][j]);
            M[j] = endouble(Mf[j]);
        }
        String[] mot = new String[nbr_col + 1];
        for (int y = 0; y <= nbr_col; y++) {
            if ((M[y] < 0) && (M[y] != -1)) {
                mot[y] = Mf[y].EnString() + "M";
            } else if ((M[y] > 0) && (M[y] != 1)) {
                mot[y] = "+" + Mf[y].EnString() + "M";
            } else if (M[y] == 1) {
                mot[y] = "+M";
            } else if (M[y] == -1) {
                mot[y] = "-M";
            } else if (M[y] == 0) {
                mot[y] = "";
            }
        }
        for (int y = 0; y <= nbr_col; y++) {
            TextView textView = (TextView) factory.inflate(R.layout.textview, null);
            String str = "";
            if (t[0][y] != 0) {
                str = tf[0][y].EnString() + mot[y];
            } else if ((t[0][y] == 0) && (M[y] < 0)) {
                str = mot[y];
            } else if ((t[0][y] == 0) && (M[y] > 0) && (M[y] != 1)) {
                str = Mf[y].EnString() + "M";
            } else if ((t[0][y] == 0) && (M[y] == 1)) {
                str = "M";
            } else if ((t[0][y] == 0) && (M[y] == 0)) {
                str = "0";
            }
            ((TextView) textView.findViewById(R.id.textView)).setText(str);
            tableRows[0].addView(textView);

        }
    }

    public static boolean is_in(ArrayList<Integer> art, int i) {
        boolean envoi = false;
        for (int j = 0; j < art.size(); j++)
            if (i == art.get(j)) {
                envoi = true;
                j = art.size() + 1;
            }
        return envoi;
    }


    public void standard(ViewGroup viewGroup, int max_min, ArrayList<Integer> art_list, ArrayList<Integer> egal_list, ArrayList<Integer> ecar_list) {
        LayoutInflater factory = LayoutInflater.from(this);
        TableLayout tableLayout = (TableLayout) viewGroup;
        int lign = tableLayout.getChildCount();
        int e = 1, a = 1;
        TableRow[] tableRows = new TableRow[lign];
        for (int i = 1; i < lign; i++) {
            tableRows[i] = (TableRow) tableLayout.getChildAt(i);
            int childCount = tableRows[i].getChildCount();
            Spinner spinner = (Spinner) tableRows[i].getChildAt(childCount - 2);
            int selectedItem = spinner.getSelectedItemPosition();
            if (selectedItem == 0) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("+ <i>e" + String.valueOf(e++) + "</i>"));
                ecar_list.add(i);
            } else if (selectedItem == 1) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                String tt = "+ <i>a" + String.valueOf(a) + "</i>";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                a++;
                art_list.add(i);
                egal_list.add(i);
            } else {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                String tt = "+ <i>a" + String.valueOf(a) + "</i>";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                a++;
                art_list.add(i);
                TextView textVie = (TextView) factory.inflate(R.layout.textview, null);
                tt = "- <i>e" + String.valueOf(e) + "</i>";
                ((TextView) textVie.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                e++;
            }
        }
        tableRows[0] = (TableRow) tableLayout.getChildAt(0);
        if (max_min == 0) {
            TextView text = (TextView) tableRows[0].getChildAt(0);
            int childCount = tableRows[0].getChildCount();


        } else {
            TextView text = (TextView) tableRows[0].getChildAt(0);
             int childCount = tableRows[0].getChildCount();


        }


    }
    public void standard2(ViewGroup viewGroup, int max_min, ArrayList<Integer> art_list, ArrayList<Integer> egal_list, ArrayList<Integer> ecar_list) {
        LayoutInflater factory = LayoutInflater.from(this);
        TableLayout tableLayout = (TableLayout) viewGroup;
        int lign = tableLayout.getChildCount();
        int e = 1, a = 1;
        TableRow[] tableRows = new TableRow[lign];
        for (int i = 1; i < lign; i++) {
            tableRows[i] = (TableRow) tableLayout.getChildAt(i);
            int childCount = tableRows[i].getChildCount();
            Spinner spinner = (Spinner) tableRows[i].getChildAt(childCount - 2);
            int selectedItem = spinner.getSelectedItemPosition();
            if (selectedItem == 0) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml("+ <i>e" + String.valueOf(e++) + "</i>"));
                art_list.add(i);
                } else if (selectedItem == 1) {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                String tt = "+ <i>a" + String.valueOf(a) + "</i>";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                a++;
                ecar_list.add(i);
                art_list.add(i+1);
                egal_list.add(i);
                lign++;i++;
            } else {
                TextView textView = (TextView) factory.inflate(R.layout.textview, null);
                String tt = "+ <i>a" + String.valueOf(a) + "</i>";
                ((TextView) textView.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                a++;ecar_list.add(i);
                TextView textVie = (TextView) factory.inflate(R.layout.textview, null);
                tt = "- <i>e" + String.valueOf(e) + "</i>";
                ((TextView) textVie.findViewById(R.id.textView)).setText(Html.fromHtml(tt));
                e++;
            }
        }
        tableRows[0] = (TableRow) tableLayout.getChildAt(0);
        if (max_min == 0) {
            TextView text = (TextView) tableRows[0].getChildAt(0);
            int childCount = tableRows[0].getChildCount();


        } else {
            TextView text = (TextView) tableRows[0].getChildAt(0);
            int childCount = tableRows[0].getChildCount();


        }


    }
    public void dual_click(View view){
        try {
            ecar_list.clear();
            art_list.clear();
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
            nbr_var = tableLayout.getChildCount() - 1;
            Spinner spinner = (Spinner) findViewById(R.id.spinne);
            max_min = spinner.getSelectedItemPosition();
            if (max_min == 0) max_min = 1;
            else max_min = 0;
            TableRow Row = (TableRow) tableLayout.getChildAt(0);
            nbr_eqt = (Row.getChildCount() - 1) / 2;
            standard2(tableLayout, max_min, art_list, egal_list, ecar_list);
            if ((max_min == 0 && art_list.size() == 0) || (max_min == 1 && ecar_list.size() == 0)) {
                nbr_col = nbr_var + (2 * art_list.size()) + ecar_list.size();
                double t[][] = new double[nbr_eqt + 1][nbr_col + 1];
                Fraction tf[][] = new Fraction[nbr_eqt + 1][nbr_col + 1];
                ti = new Fraction[nbr_eqt + 1][nbr_col + 1];
                Fraction[] Mf = new Fraction[nbr_col + 1];
                for (int j = 0; j <= nbr_col; j++) {
                    for (int i = 0; i <= nbr_eqt; i++) tf[i][j] = new Fraction();
                    Mf[j] = new Fraction();
                }
                int[] var_hbase = new int[nbr_col];
                ArrayList<Integer> var_dbase = new ArrayList<>();
                for (int y = 0; y < nbr_col; y++) {
                    var_hbase[y] = y + 1;
                }
                double[] M = new double[nbr_col + 1];
                remplir2(tableLayout, nbr_eqt, M, t, Mf, var_dbase, tf, nbr_col, nbr_var, max_min, art_list, egal_list, ecar_list);
                for (int i = 0; i <= nbr_eqt; i++)
                    for (int j = 0; j <= nbr_col; j++) {
                        ti[i][j] = tf[i][j];
                    }
                if (max_min == 0) {
                    ecar_list.clear();
                    for (int i = 1; i <= nbr_eqt; i++) ecar_list.add(i);
                } else {
                    art_list.clear();
                    for (int i = 1; i <= nbr_eqt; i++) art_list.add(i);
                }
                init2();
            } else {
                Toast.makeText(this, "Vous ne pouvez pas dualiser\n cette equation", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this,"Vérifier vos nombres",Toast.LENGTH_SHORT).show();
        }
    }
    public void remplir2(ViewGroup viewGroup, int nbr_eqt, double[] M, double[][] t, Fraction[] Mf, ArrayList<Integer> var_dbase, Fraction[][] tf, int nbr_col, int nbr_var, int max_min, ArrayList<Integer> art_list, ArrayList<Integer> egal_list, ArrayList<Integer> ecar_list) {
        LayoutInflater factory = LayoutInflater.from(this);
        TableLayout tableLayout = (TableLayout) viewGroup;
        int ligne = tableLayout.getChildCount();
        TableRow[] tableRows = new TableRow[ligne];
        int ec = 0, eg = ecar_list.size() + art_list.size(), em = eg;
        tableRows[0] = (TableRow) tableLayout.getChildAt(0);
        int k = 0;
        for (int i = 1; i < (2 * nbr_eqt + 1); i += 2) {
            TextView textView = (TextView) tableRows[0].getChildAt(i);
            String text = textView.getText().toString();
             tf[i  - k][nbr_col] = new Fraction(traduit_decim(text.replace("+", "")).trim());
                k++;


        }
        for (int i = 1; i < tableRows.length; i++) {
            tableRows[i] = (TableRow) tableLayout.getChildAt(i);
            int z = 0;
            for (int j = 1; j < (2 * nbr_eqt + 1); j += 2) {
                TextView textView = (TextView) tableRows[i].getChildAt(j);
                String text = textView.getText().toString();

                    tf[j  - z][i-1] = new Fraction(traduit_decim(text.replace("+", "")).trim());
                    z++;

            }


             int childCount = tableRows[i].getChildCount();
            TextView textView = (TextView) tableRows[i].getChildAt(childCount - 1);
            tf[0][i-1] = new Fraction(traduit_decim(textView.getText().toString()));

        }

    }
}




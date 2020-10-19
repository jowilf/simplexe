package com.example.jo_wilf.jo_simplexe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Display;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class Repere extends TextView {
    private Paint mPaint;
    public ArrayList<String[][]> ts = new ArrayList<>();
    boolean degenerescence = false;

    Path path1=new Path();
    public ArrayList<String[]> Ms = new ArrayList<>();
    public int mColor,nbr_co;
    public boolean borne = false;
    ArrayList<Line> mesLignes =new ArrayList<>();
    public ArrayList<Integer> art_list = new ArrayList<Integer>(), egal_list = new ArrayList<Integer>(), ecar_list = new ArrayList<Integer>();
    public boolean bool;
    public ArrayList<Double> mes_valx=new ArrayList(), mes_valy=new ArrayList();
    public  int mDivider,x,y,xs,ys,pas,nbr_col=1,nbr_eqt=1,pas_x,pas_y;
    public Fraction[][] ti;
    double nx=0.1,ny=nx,x1p,x2p,nxp,nyp,sx,sy;
    public void set2( ArrayList<Integer> art_lis ,ArrayList<Integer> egal_lis,ArrayList<Integer> ecar_lis){
        art_list=art_lis;
        ecar_list=ecar_lis;
        egal_list=egal_lis;
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

    public void set(Fraction[][]t,int nbr,int nby,  ArrayList<String[][]> tr,ArrayList<String[]> M,boolean deg,boolean bo,int nbr_cox,double xa,double xb){
        ti=t;nbr_col=nbr;nbr_eqt=nby;
        degenerescence=deg;
        borne=bo;
        Ms=M;
 ts=tr;
        nbr_co=nbr_cox;
        x1p=xa;x2p=xb;

        sx=x1p;sy=x2p;
        //setText(getText()+"   "+x1p+"   "+x2p);



    }
    public Repere(Context context, AttributeSet attr){
        super(context, attr);
    init();
    }
    public Repere(Context context){
        super(context);
        init();
    }
    public void fn_eco(Canvas canvas,Fraction a){
        int i=0;
        ti[i][nbr_col]=a;
        pas_x=x/pas-4;pas_y=y/pas-4;
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth((float) 2);
        double x1=0,y1=0,x2=0,y2=0;
        if((endouble(ti[i][0])!=0)&&(endouble(ti[i][1])!=0)){
            x1=(-2*nx*pas);
            y1=fx(x1,i);
            x2=xs*nx;
            y2=fx(x2,i);
            canvas.drawLine(pos_x(x1),pos_y(y1,y),pos_x(x2),pos_y(y2, y),paint);
        }
        else if((endouble(ti[i][0])==0)&&(endouble(ti[i][1])!=0)){
            y2=endouble(Fraction.divide(ti[i][nbr_col],ti[i][1]));
            y1=y2;
            x1=-2*pas;
            x2=xs;
            canvas.drawLine((float)x1,pos_y(y1,y),(float)x2,pos_y(y2, y),paint);
        }
        else if ((endouble(ti[i][1])==0)&&(endouble(ti[i][0])!=0)){
            x1=endouble(Fraction.divide(ti[i][nbr_col],ti[i][0]));
            x2=x1;
            y1=-2*pas;
            y2=ys;
            canvas.drawLine(pos_x(x1),(float)y1,pos_x(x2),(float)y2,paint);
        }
        }
public  void choix(){
    pas_x=x/pas-6;pas_y=y/pas-6;
    Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.GRAY);
    paint.setStrokeWidth((float) 2);

    double x1=0,y1=0,x2=0,y2=0;

    for(int i=1;i<=nbr_eqt;i++){
        if(endouble(ti[i][nbr_col])==0){

        }
        else  if((endouble(ti[i][0])!=0)&&(endouble(ti[i][1])!=0)){
            x1=endouble(Fraction.divide(ti[i][nbr_col],ti[i][0]));
            y1=0;
            x2=0;
            y2=endouble(Fraction.divide(ti[i][nbr_col],ti[i][1]));
           if(((Math.abs(Math.max(x1,x2))/pas_x))>nx) nx=((Math.abs(Math.max(x1,x2))/pas_x));
            if(((Math.abs(Math.max(y1,y2))/pas_y))>ny) ny=((Math.abs(Math.max(y1,y2))/pas_y));
        }
        else if((endouble(ti[i][0])==0)&&(endouble(ti[i][1])!=0)){
            y2=endouble(Fraction.divide(ti[i][nbr_col],ti[i][1]));
            y1=y2;
            if(((Math.abs(Math.max(y1,y2))/pas_y))>ny) ny=((Math.abs(Math.max(y1,y2))/pas_y));
        }
        else if((endouble(ti[i][1])==0)&&(endouble(ti[i][0])!=0)){
            x1=endouble(Fraction.divide(ti[i][nbr_col],ti[i][0]));
            x2=x1;
            if(((Math.abs(Math.max(x1,x2))/pas_x))>nx) nx=((Math.abs(Math.max(x1,x2))/pas_x));
        }
    }
}
    public void fnt(Canvas canvas,int x,int y, int pas,int i){
     pas_x=x/pas-4;pas_y=y/pas-4;
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        double x1=0,y1=0,x2=0,y2=0;
        Fraction a=ti[i][0],b=ti[i][1],c=ti[i][nbr_col];
        if(endouble(ti[i][nbr_col])==0){
            x1=(-2*nx*pas);
            y1=fx(x1,i);
            x2=(xs-2*pas)*nx;
            y2=fx(x2,i);
           setText(Double.toString(x2)+Double.toString(x2));
            if((endouble(a)<0&&endouble(b)>0))
            {  if (is_in(ecar_list,i)) {
                /*Path path=new Path();
                path.moveTo(pos_x(0), pos_y(0, y));
                path.lineTo(pos_x(nx), pos_y(0, y));
                path.lineTo(pos_x(0), pos_y((float) 2 * ny, y));
                path.lineTo(pos_x(0), pos_y(0, y));
                // path.close();
                Paint paint1=new Paint();
                paint1.setStyle(Paint.Style.FILL_AND_STROKE);
                paint1.setColor(Color.YELLOW);
                // path.setFillType(Path.FillType.EVEN_ODD);
                paint1.setStrokeWidth(2);
                canvas.drawPath(path, paint1);
                canvas.drawLine(pos_x(0),pos_y(0,y),pos_x(3 * nx),pos_y(3 * ny, y),mPaint);

*/

                paint.setAntiAlias(true);
                Path path=new Path();
                path.moveTo(pos_x(0), pos_y(0, y));
                setText("\n" + pos_x(0) + "\n"+pos_y(0, y));
                path.lineTo(pos_x(x2), pos_y(0, y));
                path.lineTo(pos_x(x2), pos_y(y2, y));
                setText("\n" + pos_x(x2) +"\n" + pos_y(y2, y));
                path.lineTo(pos_x(0), pos_y(0, y));
                path.close();
                path1=path;
                canvas.drawPath(path, paint);
                traceLigne(canvas,new Line(pos_x(x1), pos_y(y1, y),pos_x(x2), pos_y(y2, y),paint));
            }
                else {

                Path path = new Path();
                path.moveTo(pos_x(0), pos_y(0, y));
                path.lineTo(pos_x(x2),pos_y(y2, y));
                if(y2<(y-2*pas)*nx) path.lineTo(xs,0);
                path.lineTo(2*pas,0);
                path.lineTo(pos_x(0), pos_y(0, y));
                canvas.drawPath(path, paint);
            }
            }
           mesLignes.add(new Line(pos_x(x1), pos_y(y1, y),pos_x(x2), pos_y(y2, y),paint));

        }
        else if((endouble(ti[i][0])!=0)&&(endouble(ti[i][1])!=0)){
            x1=(-2*nx*pas);
            y1=fx(x1,i);
            x2=xs*nx;
            y2=fx(x2,i);
           mesLignes.add(new Line(pos_x(x1), pos_y(y1, y), pos_x(x2), pos_y(y2, y),paint));
        }
    else if((endouble(ti[i][0])==0)&&(endouble(ti[i][1])!=0)){
        y2=endouble(Fraction.divide(ti[i][nbr_col],ti[i][1]));
        y1=y2;
        x1=-2*pas;
        x2=xs;
         mesLignes.add(new Line((float) x1, pos_y(y1, y), (float) x2, pos_y(y2, y),paint));
    }
    else if ((endouble(ti[i][1])==0)&&(endouble(ti[i][0])!=0)){
        x1=endouble(Fraction.divide(ti[i][nbr_col],ti[i][0]));
        x2=x1;
        y1=-2*pas;
        y2=ys;
       mesLignes.add(new Line(pos_x(x1), (float) y1, pos_x(x2), (float) y2,paint));
    }

       }

    public double endouble(Fraction e) {
        return (double) e.Numerateur / e.Denominateur;
    }

    public double fx(double x, int i) {
        return (x * endouble(Fraction.divide(Fraction.multiply(ti[i][0],new Fraction("-1")),ti[i][1]))+endouble(Fraction.divide(ti[i][nbr_col], ti[i][1])));
    } public float pos_x(double i){
        return (float)((i/nx)*pas+2*pas);
    }
    public float pos_y(double i,int y){
        return (float)(y-(i/ny)*pas-2*pas);
    }
    public float pos_xp(double i){
        return (float)((i/nxp)*pas+2*pas);
    }
    public float pos_yp(double i,int y){
        return (float)(y-(i/nyp)*pas-2*pas);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        mes_valx.clear();
        mes_valy.clear();
        if (nbr_col == 1) for (int j = 0; j <= nbr_col; j++) {
            for (int k = 0; k <= nbr_eqt; k++) ti[k][j] = new Fraction();
        }
        x = getWidth();
        y = getHeight();
        int rest_x = 0, rest_y = 0;
        if (x > y) pas = (int)(y-0.01*y)/mDivider;
        else pas =(int) (x-0.01*x)/mDivider;

        for (int i = 0; i < x; i += pas) {
           canvas.drawLine(i, 0, i, y, mPaint);
            rest_x = x - i;
        }
        for (int i = 0; i < y; i += pas) {
            rest_y = y - i;
            canvas.drawLine(0, i, x, i, mPaint);
        }
        xs=x;
        ys=y;
        x -= rest_x;
        y -= rest_y;
        choix();
        nx=new BigDecimal(nx).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        ny=new BigDecimal(ny).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1); paint.setTextSize(pas);
        paint.setColor(Color.BLACK);
        //canvas.drawLine(2 * pas, pas, 2 * pas, y - pas, paint);
        mesLignes.add(new Line(2 * pas, pas, 2 * pas, y - pas, paint));
       // canvas.drawLine(x - pas, pas, x - pas, y - pas, paint);
        //canvas.drawLine(2 * pas, pas, (float) (pas + 0.7 * pas), 2 * pas, paint);
        mesLignes.add(new Line(2 * pas, pas, (float) (pas + 0.7 * pas), 2 * pas, paint));
        //canvas.drawLine(2 * pas, pas, (float) (2 * pas + 0.3 * pas), 2 * pas, paint);
        mesLignes.add(new Line(2 * pas, pas, (float) (2 * pas + 0.3 * pas), 2 * pas, paint));
        //canvas.drawLine(pas, y - 2 * pas, x - pas, y - 2 * pas, paint);
        mesLignes.add(new Line(pas, y - 2 * pas, x - pas, y - 2 * pas, paint));
        //canvas.drawLine(x - pas, y - 2 * pas, x - 2 * pas, (float) (y - 2 * pas - 0.3 * pas), paint);
        mesLignes.add(new Line(x - pas, y - 2 * pas, x - 2 * pas, (float) (y - 2 * pas - 0.3 * pas), paint));
        //canvas.drawLine(x - pas, y - 2 * pas, x - 2 * pas, (float) (y - pas - 0.7 * pas), paint);
        mesLignes.add(new Line(x - pas, y - 2 * pas, x - 2 * pas, (float) (y - pas - 0.7 * pas), paint));
        for (int i = 1; i <= nbr_eqt; i++) {
                   try {
                       fnt(canvas, x, y, pas, i);
           }
           catch(Exception e) {
           }
             }
        int n=ts.size()-1;
        String[][] tf = ts.get(n);
        String[] Mf = Ms.get(n);
        if(degenerescence);
       else  if(borne)  fn_eco(canvas, new Fraction("0"));
            else if(((Mf[nbr_co].indexOf("M"))>=0));
        else {

            fn_eco(canvas, new Fraction("0"));
            fn_eco(canvas, new Fraction(Mf[nbr_co].replace("-", "")));
            Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint1.setColor(Color.RED);
            paint1.setStrokeWidth(2f);
            nxp = 10;
            nyp = nxp;
            sx = x1p;
            sy = x2p;
            sx /= nx;
            sx *= 10;
            sy /= ny;
            sy *= 10;
            Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setTextSize(pas);
            paint2.setColor(Color.RED);
            paint2.setStrokeWidth(0.5f);
            if (x1p != 0 && x2p != 0) {
                for (int i = 0; i <= sx - pas / mDivider; i += 2 * (pas / mDivider)) {
                    canvas.drawLine(pos_xp(i), pos_yp(sy, y), pos_xp(i + pas / mDivider), pos_yp(sy, y), paint1);

                }
                for (int i = 0; i <= sy - pas / mDivider; i += 2 * (pas / mDivider)) {
                    canvas.drawLine(pos_xp(sx), pos_yp(i, y), pos_xp(sx), pos_yp(i + pas / mDivider, y), paint1);
                }
                   canvas.drawText("a", pos_xp(sx - nxp / 4), pos_y(-ny, y), paint2);
                canvas.drawText("b", pos_x(-nx), pos_yp(sy - nxp / 3, y), paint2);
                }
            else if(x1p==0&&x2p!=0){

                canvas.drawText("b", pos_x(-nx), pos_yp(sy - nxp / 3, y), paint2);
            }
            else if (x2p==0&&x1p!=0){
                canvas.drawText("a", pos_xp(sx - nxp / 4), pos_y(-ny, y), paint2);
            }
            canvas.drawCircle(pos_xp(sx), pos_yp(sy, y), (float) (pas / (8)), paint1);
            paint2.setColor(Color.BLACK);
            canvas.drawText("0", pos_x(nx / 4), pos_y(-ny, y), paint2);
            canvas.drawText("x2", pas / 2,2* pas, paint2);
            canvas.drawText("x1",x-pas,y-pas,paint2);/*
paint2.setColor(Color.YELLOW);
       //     for(int i=0;i<=40;i+=2)
         //     {int j=(60-(6/4)*i);
                  Paint paint3=new Paint();
                  paint3.setColor(Color.YELLOW);
            int i=0;canvas.concat(new Matrix().);
         vas.drawRect(pos_x(0),pos_y(0,y),pos_x(0),pos_y(60,y),paint2);
           //     }*/
        }

        super.onDraw(canvas);

        Path path = new Path();
        path.moveTo(pos_x(0), pos_y(0, y));
         path.lineTo(pos_x(nx), pos_y(0, y));
        path.lineTo(pos_x(0), pos_y((float) 2 * ny, y));
        path.lineTo(pos_x(0), pos_y(0, y));
        path.close();
        Paint paint1=new Paint();
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        paint1.setColor(Color.YELLOW);
       // path.setFillType(Path.FillType.EVEN_ODD);
        paint1.setStrokeWidth(2);
        canvas.drawPath(path, paint1);
        canvas.drawLine(pos_x(0),pos_y(0,y),pos_x(3 * nx),pos_y(3 * ny, y),mPaint);

    }
    public void init(){
        x=getWidth();
        y=getHeight();
        mDivider=20;
        mColor=Color.GRAY;
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(0.3f);
        mPaint.setTextSize(5);
        mPaint.setDither(true);
        mPaint.setColor(mColor);

    }
    public void trace (){invalidate();}
class Line {
    float x1,y1,x2,y2;
    Paint paint;
    Line(double _x1,double _y1,double _x2,double _y2,Paint _paint){
        x1=(float)_x1;
        x2=(float)_x2;
        y1=(float)_y1;
        y2=(float)_y2;
        paint=_paint;
    }

}
    public void traceLigne (Canvas canvas,Line line){
        canvas.drawLine(line.x1,line.y1,line.x2,line.y2,line.paint);
    }
}

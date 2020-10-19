package com.example.jo_wilf.jo_simplexe;

/**
 * Created by JO_WILF on 08/08/2016.
 */
import java.util.*;
import java.math.BigInteger;
public class Fraction
{
    public long Denominateur,Numerateur;
    public  Fraction()
    {
        Numerateur=0;
        Denominateur=1;
    }
    public long getDenominateur()
    {return Denominateur;}
    public long getNumerateur()
    {return Numerateur;}
    public Fraction(String traduit)
    {

        int localise=traduit.indexOf("/");

        if(localise==-1)
        {
            Numerateur=Long.parseLong(traduit);
            Denominateur=1;
        }
        else
        {
            String num=traduit.substring(0,localise);
            Numerateur=Long.parseLong(num);
            String denom=traduit.substring(localise + 1);
            Denominateur=Long.parseLong(denom);
        }
       long division=pgcd(absolu(Numerateur),absolu(Denominateur));
        Numerateur=Numerateur/division;
        Denominateur=Denominateur/division;

    }
    public static Fraction substract(Fraction a,Fraction b)
    {
        long num,denom;
        num=(a.Numerateur*b.Denominateur)-(a.Denominateur*b.Numerateur);
        denom=b.Denominateur*a.Denominateur;

        String renvoi=String.valueOf(num)+"/"+String.valueOf(denom);
        return new Fraction(renvoi);
    }
    public static Fraction sum(Fraction a,Fraction b)
    {
        long num,denom;
        num=(a.Numerateur*b.Denominateur)+(a.Denominateur*b.Numerateur);
        denom=b.Denominateur*a.Denominateur;
        String renvoi=String.valueOf(num)+"/"+String.valueOf(denom);
        return new Fraction(renvoi);
    }
    public static Fraction divide(Fraction a, Fraction b)
    {
        long num,denom;
        num=a.Numerateur*b.Denominateur;
        denom=a.Denominateur*b.Numerateur;
        String renvoi=String.valueOf(num)+"/"+String.valueOf(denom);
        return new Fraction(renvoi);
    }
    public static Fraction multiply(Fraction a, Fraction b)
    {
        long num,denom;
        num=a.Numerateur*b.Numerateur;
        denom=a.Denominateur*b.Denominateur;
        String renvoi=String.valueOf(num)+"/"+String.valueOf(denom);
        return new Fraction(renvoi);

    }
    public String EnString()
    {
        long division,num,denom;

        division=pgcd(absolu(Numerateur), absolu(Denominateur));
        num=Numerateur/division;
        denom=Denominateur/division;
        if(denom<0){denom=-1*denom;num=-1*num;}
        if((num!=0)&& (denom!=1))
        {
            return (String.valueOf(num)+"/"+String.valueOf(denom));
        }
        else if(num==0)
        {
            return ("0");
        }
        else {return String.valueOf(num);}
    }
    public static long absolu(long x)
    {
        if (x<0)return -x;
        else return x;
    }
    public static long pgcd(long a,long b)
    {

        long reste=a%b;
        while(reste!=0)
        {
            a=b;
            b=reste;
            reste=a%b;
        }
        return b;
    }
}


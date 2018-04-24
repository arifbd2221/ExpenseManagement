package com.countonit.user.swipetabpractice;

/**
 * Created by User on 7/20/2017.
 */

public class PayeeDataBase{

    public static final String Table_Name="payee_list";
    public static final String Table_Payer="payer_list";
    public static final String id="_id";
    public static final String payer_id="_id";
    public static final String Payee_Name="Payee_Name";
    public static final String Payer_Name="Payer_Name";
    public static final String Amount="Amount";
    public static final String Payer_Amount="Amount";
    public static final String Email="Email";
    public static final String Payer_Email="Email";
    public static final String Dates="Date";
    public static final String Payer_Dates="Date";
    public static final String phone="Phone";
    public static final String Create_Table="CREATE TABLE "+Table_Name+" ("+id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Payee_Name+" VARCHAR(30), "+Amount+" VARCHAR(15), "+Email+" VARCHAR(30), "+Dates+" VARCHAR(15), "+phone+" varchar(15) );";
    public static final String Payer_Table="CREATE TABLE "+Table_Payer+" ("+payer_id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Payer_Name+" VARCHAR(30), "+Payer_Amount+" VARCHAR(15), "+Payer_Email+" VARCHAR(30), "+Payer_Dates+" VARCHAR(15), "+phone+" varchar(15) );";

    public static final String makeQuery="DROP TABLE IF EXISTS "+Table_Name;
    public static final String payerQuery="DROP TABLE IF EXISTS "+Table_Payer;


}

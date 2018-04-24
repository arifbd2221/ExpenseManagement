package com.countonit.user.swipetabpractice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.DataBase2;
import com.countonit.user.swipetabpractice.DataBase3;
import com.countonit.user.swipetabpractice.PayeeDataBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by User on 8/5/2017.
 */

public class MainDB extends SQLiteOpenHelper {

    ArrayList<String> exp=new ArrayList<String>();

    private static final String Income_Table="Income_Table";
    private static final String Income_ID="_id";
    private static final String Income_Amount="Income";
    private static final String Income_Date="Income_Date";
    private static final String Income_Month="Income_Month";
    private static final String Income_Source="Income_Source";
    private static final String Income_Note="Income_Note";
    private static final String Income_Time="Income_Time";
    private static final String Income_Year="Income_Year";
    private static final String Income_Payment_method="Income_Payment_method";

    private static final String CreateIncomeTable="create table "+Income_Table+" ("+Income_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Income_Amount+" number(50), "+Income_Date+" varchar(30), "+Income_Month+" number(5), "+Income_Source+" varchar(30), "+Income_Note+" varchar(80), "+Income_Time+" varchar(20), "+Income_Payment_method+" varchar(20), "+Income_Year+" number(10) );";
    private static final String DropIncomeTable="drop table if exists "+Income_Table;


    private Context context;
    private static final String DB_Name="Count_on_it";
    private static final int DB_Version=2;
    private static final String Table_Name="category_expense_date";
    private static final String ID="_id";
    private static final String Category="category";
    private static final String Expense_Title="expense_title";
    private static final String Expense_Cost="expense_cost";
    private static final String Dates="date";
    private static final String Month="month";
    private static final String Year="year";
    private static final String Budget="budget";
    private static final String Times="time";

    private static final String Create_Table="create table "+Table_Name+" ( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Category+" varchar(20), "+Expense_Title+" varchar(20), "+Expense_Cost+" number(10), "+Budget+" number(10), "+Dates+" varchar(15), "+Month+" varchar(15), "+Year+" number(10), "+Times+" varchar(20), Image blog );" ;
    private static final String Drop_table="drop table if exists "+Table_Name;



    public MainDB(Context context){
        super(context,DB_Name,null,DB_Version);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Create_Table);
            db.execSQL(DataBase2.Create_Table);
            db.execSQL(DataBase3.Table);
            db.execSQL(PayeeDataBase.Create_Table);
            db.execSQL(PayeeDataBase.Payer_Table);
            db.execSQL(CreateIncomeTable);
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            db.execSQL(Drop_table);
            db.execSQL(DataBase2.query);
            db.execSQL(DataBase3.query);
            db.execSQL(PayeeDataBase.makeQuery);
            db.execSQL(PayeeDataBase.payerQuery);
            db.execSQL(DropIncomeTable);
            onCreate(db);
        }catch (SQLException s){
            s.printStackTrace();
        }


    }




    public void insertImage(int id,byte[] image){
        SQLiteDatabase database=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("Image",image);

        database.update(Table_Name,contentValues,ID+" =? ",new String[]{id+""});


        /*String sql="update "+Table_Name+" set Image =? where _id="+id;

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(id,image);

        statement.executeInsert();*/
        database.close();

    }



        // Income Table


    public void insertIncome(int income,String dates,int month,String incomeSource,String incomeNote,String time)
    {
        SQLiteDatabase database=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(Income_Amount,income);
        contentValues.put(Income_Date,dates);
        contentValues.put(Income_Month,month);
        contentValues.put(Income_Source,incomeSource);
        contentValues.put(Income_Note,incomeNote);
        contentValues.put(Income_Time,time);
        contentValues.put(Income_Year,new SimpleDateFormat("yyyy").format(new Date()));
        database.insert(Income_Table,null,contentValues);

        database.close();

    }




    public ArrayList<String> getAllIncomeTime(int month,String year){
        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> incomeTimeList=new ArrayList<>();

        Cursor cursor=null;
        try{
        cursor=database.query(Income_Table,new String[]{Income_Time},Income_Month+" =? and "+Income_Year+" =?",new String[]{month+"",year},null,null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                incomeTimeList.add(cursor.getString(cursor.getColumnIndex(Income_Time)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }



        return incomeTimeList;
    }

    public ArrayList<String> getAllIncomeSource(int month,String year){
        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> incomeTimeList=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(Income_Table,new String[]{Income_Source},Income_Month+" =? and "+Income_Year+" =? ",new String[]{month+"",year},null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                incomeTimeList.add(cursor.getString(cursor.getColumnIndex(Income_Source)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }



        return incomeTimeList;
    }


    public ArrayList<String> getAllIncomeSource(){
        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> incomeTimeList=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(true,Income_Table,new String[]{Income_Source},null,null,null,null,null,null);


            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                incomeTimeList.add(cursor.getString(cursor.getColumnIndex(Income_Source)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
            cursor.close();
            database.close();
        }


        return incomeTimeList;
    }




    public int getAllIncomeBySource(String income_Source,int month,int year){

        SQLiteDatabase database=getReadableDatabase();
        int sum=0;
        Cursor cursor=null;
        try{
            cursor=database.query(Income_Table,new String[]{Income_Amount},Income_Source+" =? and "+Income_Month+" =? and "+Income_Year+" =? ",new String[]{income_Source,month+"",year+""},null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                sum=sum+Integer.parseInt(cursor.getString(cursor.getColumnIndex(Income_Amount)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }

        return sum;
    }








    public void deleteIncome(){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(Income_Table,null,null);
    }



    public ArrayList<Integer> getAllIncome(int month,String year){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<Integer> incomeList=new ArrayList<>();
        Cursor cursor=null;
        try {
            cursor=database.query(Income_Table,new String[]{Income_Amount},Income_Month+" =? and "+Income_Year+" =? ",new String[]{month+"",year},null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                incomeList.add(cursor.getInt(cursor.getColumnIndex(Income_Amount)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
            cursor.close();
            database.close();
        }

        return incomeList;
    }


    public ArrayList<String> getAllIncomeDate(int month,String year){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> incomeDateList=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(Income_Table,new String[]{Income_Date},Income_Month+" =? and "+Income_Year+" =? ",new String[]{month+"",year},null,null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                incomeDateList.add(cursor.getString(cursor.getColumnIndex(Income_Date)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
                database.close();
        }
        return incomeDateList;
    }



    // Income Table



// mainDb starts



    public ArrayList<Integer> getAllExpenseCostByCategory()
    {
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=null;
        ArrayList<Integer> amountOfCat=new ArrayList<>();
        try{

            cursor=database.rawQuery("select sum("+Expense_Cost+") from "+Table_Name+" group by category",null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                amountOfCat.add(cursor.getInt(cursor.getColumnIndex(Expense_Cost)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            database.close();
        }

       //amountOfCat.add(cursor.getCount());
        return amountOfCat;


    }




    public void createCategoryExpense(String cat,String expense,int budget){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = new Date();

        if ( !checkExpense(expense)) {
            SQLiteDatabase db = getWritableDatabase();
            try {

                ContentValues contentValues = new ContentValues();
                contentValues.put(Category, cat.substring(0, 1).toUpperCase() + cat.substring(1));
                contentValues.put(Expense_Title, expense);
                contentValues.put(Expense_Cost, 0);
                contentValues.put(Budget, budget);
                contentValues.put(Dates, dateFormat1.format(date1));
                contentValues.put(Month, Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                contentValues.put(Year, Calendar.getInstance().get(Calendar.YEAR));
                contentValues.put(Times, "0");
                db.insert(Table_Name, null, contentValues);
            } finally {
                db.close();
            }
        }
        else {
            Toast.makeText(context,expense+" is Already exist,Try Another One",Toast.LENGTH_LONG).show();
        }
    }

    public void createCategoryExpense(String cat,String expense,int budget,int cost){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = new Date();

        if ( !checkExpense(expense)) {
            SQLiteDatabase db = getWritableDatabase();
            try {

                ContentValues contentValues = new ContentValues();
                contentValues.put(Category, cat.substring(0, 1).toUpperCase() + cat.substring(1));
                contentValues.put(Expense_Title, expense);
                contentValues.put(Expense_Cost, cost);
                contentValues.put(Budget, budget);
                contentValues.put(Dates, dateFormat1.format(date1));
                contentValues.put(Month, Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                contentValues.put(Year, Calendar.getInstance().get(Calendar.YEAR));
                contentValues.put(Times, "0");
                db.insert(Table_Name, null, contentValues);
            } finally {
                db.close();
            }
        }
        else {
            Toast.makeText(context,expense+" is Already exist,Try Another One",Toast.LENGTH_LONG).show();
        }
    }


    public void updateExpense(String oExpense,String nExpense,String cat){

        SQLiteDatabase db=getReadableDatabase();
        try{

            ContentValues contentValues=new ContentValues();
            //contentValues.put(Category,cat);
            contentValues.put(Expense_Title,nExpense);
            db.update(Table_Name,contentValues,Category+" =? and "+Expense_Title+" =? ",new String[]{cat.substring(0,1).toUpperCase()+cat.substring(1),oExpense});

        }finally {
            db.close();
        }
    }
    public void createExpenseCost(String cat,String title,int cost){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String str=dateFormat.format(date);

        SQLiteDatabase db=null;

        try {
            db=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Expense_Cost,cost);
            contentValues.put(Dates,str);

            //Cursor cursor=db.query(Table_Name,new String[]{Expense_Cost},Category+" ?= and "+Expense_Title+" ?= and "+Dates+" ?=",new String[]{cat,title,dateFormat.format(date)},null,null,null);
            db.update(Table_Name,contentValues,str+" =? ",new String[]{});

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.close();
        }

    }


    public int getIdBy(String name){

        SQLiteDatabase db=getWritableDatabase();
        String[] columns={ID};
        String[] selectionArgs={name};
        int id=0;
        Cursor cursor=null;
        try{
            cursor=db.query(Table_Name,columns,Expense_Title+" =? ",selectionArgs,null,null,null);

            while(cursor.moveToNext()){
                int index=cursor.getColumnIndex(ID);
                id=cursor.getInt(index);
            }
        }catch (Exception e){

        }finally {
            if (cursor != null)
            cursor.close();
            db.close();
        }

        return id;
    }


    public byte[] getExpenseImage(int id){

            SQLiteDatabase database=getReadableDatabase();
            Cursor cursor=null;
            byte[] image =null;
        try {
                cursor=database.query(Table_Name,new String[]{"Image"},ID+" =? ",new String[]{id+""},null,null,null);
                image = new byte[0];
                for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()) {
                    image = cursor.getBlob(cursor.getColumnIndex("Image"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            database.close();
            }
        return image;
    }



    public int getCosts(String category,String expense){

        SQLiteDatabase db=getReadableDatabase();
        int cost=0;
        Cursor cursor=null;
        try{
            cursor=db.query(Table_Name,new String[]{Expense_Cost},Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense},null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                cost=cursor.getInt(cursor.getColumnIndex(Expense_Cost));
            }
        }catch (NullPointerException np){
            np.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return cost;
    }

    public int getTotalCost(){
        int total=0;
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor=database.query(Table_Name,new String[]{Expense_Cost},null,null,null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                total=total+cursor.getInt(cursor.getColumnIndex(Expense_Cost));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            database.close();
        }
        return total;
    }


    public String getCategoryByExpense(String expense){

        SQLiteDatabase db=getReadableDatabase();
        String string=null;
        Cursor cursor=null;
        try{
            cursor=db.query(Table_Name,new String[]{Category},Expense_Title+" =? ",new String[]{expense},null,null,null);

            while (cursor.moveToNext()){
                string=cursor.getString(cursor.getColumnIndex(Category));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
            cursor.close();
            db.close();
        }

        return string;
    }


    public ArrayList<String> getExpenseTitle(){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> list=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(Table_Name,new String[]{Expense_Title},null,null,null,null,null);
            while (cursor.moveToNext()){
                String string=cursor.getString(cursor.getColumnIndex(Expense_Title));
                list.add(string);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
                database.close();

        }


        return list;

    }


    public ArrayList<String> getAllCategory(){

        SQLiteDatabase database =getReadableDatabase();
        ArrayList<String> list=new ArrayList<>();
        Cursor cursor=null;

        try{
            cursor=database.query(true,Table_Name,new String[]{Category},null,null,null,null,null,null);
            //Cursor cursor=database.rawQuery("select distinct"+Category+" from "+Table_Name,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                list.add(cursor.getString(cursor.getColumnIndex(Category)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
            cursor.close();
            database.close();

        }



        return list;

    }

    public int delete(String current){
        SQLiteDatabase db=getWritableDatabase();
        String[] whereArgs={current};
        int count=db.delete(Table_Name,Expense_Title+" =? ",whereArgs);

        db.close();

        return count;
    }


    public ArrayList<String> getExpenseTitleByCategory(String category){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> list=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(Table_Name,new String[]{Expense_Title},Category+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1)},null,null,null);

            while (cursor.moveToNext()){
                list.add(cursor.getString(cursor.getColumnIndex(Expense_Title)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
                database.close();

        }

        return list;

    }


    public int getBudgetByExpense(String category,String expense){

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=null;
        int budget=0;
        try{
                 cursor=database.query(Table_Name,new String[]{Budget},Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense},null,null,null);

            //Cursor cursor=database.rawQuery("select "+Budget+" from "+Table_Name+" where "+Category+" =? and "+Expense_Title+" =? ",new String[]{category,expense});


            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                budget=cursor.getInt(cursor.getColumnIndex(Budget));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            database.close();

        }

        return budget;
    }


    public void updateCost(String category,String expense,int cost){
        int oCost=getCosts(category,expense);
        SQLiteDatabase database=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Expense_Cost,cost+oCost);
        database.update(Table_Name,contentValues,Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense});


        database.close();

    }


    public int getTotalBudget(){

        int budget=0;
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=null;
        try{
            cursor=database.query(Table_Name,new String[]{Budget},null,null,null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                budget=budget+(cursor.getInt(cursor.getColumnIndex(Budget)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor == null)
                cursor.close();
            database.close();

        }
        return budget;
    }

    public ArrayList<Integer> getCostByCategory(String category){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<Integer> costs=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(Table_Name,new String[]{Expense_Cost},Category+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1)},null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                costs.add(cursor.getInt(cursor.getColumnIndex(Expense_Cost)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
            cursor.close();
            database.close();
        }

            return costs;
    }


    public void updateBudget(String expense,String category,int nBudget){
        SQLiteDatabase database=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Budget,nBudget);
        database.update(Table_Name,contentValues,Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense});
        database.close();
    }

    public void updateTime(String category,String expense,String time){
        SQLiteDatabase database=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Times,time);
        database.update(Table_Name,contentValues,Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense});
        database.close();
    }


    public String getTime(String category,String expense){
        SQLiteDatabase database=getReadableDatabase();
        String time=null;
        Cursor cursor=null;

        try{
            cursor=database.query(Table_Name,new String[]{Times},Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense},null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                time=cursor.getString(cursor.getColumnIndex(Times));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }


        return time;
    }



    public ArrayList<String> getAllCategoryByMonthYear(String month,int year){
        //SQLiteDatabase database=getReadableDatabase();



        ArrayList<String> cat=new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor=getReadableDatabase().query(true,Table_Name,new String[]{Category},Month+" =? and "+Year+" =?",new String[]{month,year+""},null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                cat.add(cursor.getString(cursor.getColumnIndex(Category)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

            finally {
            if (cursor != null)
            cursor.close();
        }
        //Toast.makeText(context,""+cat.size(),Toast.LENGTH_SHORT).show();




            return cat;
    }


    public String[] getAllExpenseByCatMonthYear(String cat,String month,int year){

        SQLiteDatabase database=getReadableDatabase();
        String[] expList=null;
        Cursor cursor=null;
        try{
            cursor=database.query(Table_Name,new String[]{Expense_Title},Category+" =? and "+Month+" =? and "+Year+" =?",new String[]{cat.substring(0,1).toUpperCase()+cat.substring(1),month,year+""},null,null,null);
            int i=0;
            if (cursor.getCount() >0)
                expList=new String[cursor.getCount()];


            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                expList[i]=cursor.getString(cursor.getColumnIndex(Expense_Title));
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
                database.close();

        }

        return expList;

    }
    public float[] getAllCostByCatMonthYear(String cat,String month,int year){

        SQLiteDatabase database=getReadableDatabase();
        String[] costList=null;
        float[] costs=null;
        Cursor cursor=null;

        try{
            cursor=database.query(Table_Name,new String[]{Expense_Cost},Category+" =? and "+Month+" =? and "+Year+" =?",new String[]{cat.substring(0,1).toUpperCase()+cat.substring(1),month,year+""},null,null,null);
            int i=0;
            if (cursor.getCount() >0) {
                costList = new String[cursor.getCount()];
                costs=new float[cursor.getCount()];
            }


            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                costs[i]=Float.parseFloat(costList[i]=cursor.getInt(cursor.getColumnIndex(Expense_Cost))+"");
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            database.close();

        }

        return costs;

    }



    public void resetAllWhenMonthEnd(){

        SQLiteDatabase database=getReadableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(Expense_Cost,0);
        contentValues.put(Budget,0);

        database.update(Table_Name,contentValues,null,null);
        database.close();

    }


    public void upDateMonthYear(String category,String expense){

        SQLiteDatabase database=getReadableDatabase();

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");

        ContentValues contentValues=new ContentValues();

        contentValues.put(Dates,dateFormat1.format(new Date()));
        contentValues.put(Month,Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        contentValues.put(Year,Calendar.getInstance().get(Calendar.YEAR));

        database.update(Table_Name,contentValues,Category+" =? and "+Expense_Title+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense});

        database.close();

    }





// database 2



    public boolean isMothRecordEmpty(){

        SQLiteDatabase database=getReadableDatabase();
        boolean b = false;
        Cursor cursor=null;
        try{
            cursor=database.rawQuery("select * from "+DataBase2.Table_Name,null);
        }catch (SQLiteException sql){
            sql.printStackTrace();
            Toast.makeText(context,"Sorry there is a problem",Toast.LENGTH_SHORT).show();

        }finally {
            {
                if (cursor != null && (cursor.getCount() > 0)){
                    cursor.close();
                    b=true;
                }

                database.close();
            }
        }
        return b;
    }


    public boolean availableThisMonth(String month,int year){

        SQLiteDatabase database=getReadableDatabase();
        boolean b = false;
        Cursor cursor=null;
        try{
            cursor=database.query(DataBase2.Table_Name,new String[] {DataBase2.Year},DataBase2.Month_Name+" =? and "+DataBase2.Year+" =? ",new String[]{month,year+""},null,null,null);

        }catch (SQLiteException sql){
            sql.printStackTrace();
            Toast.makeText(context,"Sorry there is a problem",Toast.LENGTH_SHORT).show();

        }finally {
            {
                if (cursor != null && (cursor.getCount() > 0)){
                    cursor.close();
                    b=true;
                }

                    database.close();
            }
        }
        return b;
    }



    public long insertData(String expense,String totalECost,String month,String income,int year,int budget){


        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(DataBase2.List_Column,expense);
        contentValues.put(DataBase2.Month_Name,month);
        contentValues.put(DataBase2.Column_Name,totalECost);
        contentValues.put(DataBase2.Column_Income,income);
        contentValues.put(DataBase2.Year,year);
        contentValues.put(DataBase2.Budget,budget);
        long id=db.insert(DataBase2.Table_Name,null,contentValues);
        return id;

    }


    public void updateMonthData(String expense,String totalECost,String month,String income,int year,int budget){

        SQLiteDatabase database=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        String[] where=new String[]{};

        contentValues.put(DataBase2.List_Column,expense);
        contentValues.put(DataBase2.Month_Name,month);
        contentValues.put(DataBase2.Column_Name,totalECost);
        contentValues.put(DataBase2.Column_Income,income);
        contentValues.put(DataBase2.Year,year);
        contentValues.put(DataBase2.Budget,budget);
        try {
            database.update(DataBase2.Table_Name,contentValues,null,null);
        }catch (SQLiteException sql){
            sql.printStackTrace();
        }finally {
            database.close();
        }

    }

    public ArrayList<String> getAllExpense(String month){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.List_Column};
        String[] selectionArgs={month};
        Cursor cursor=null;
        try{
            cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? ",selectionArgs,null,null,null);

            //while (cursor.moveToNext())
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase2.List_Column);
                String expense=cursor.getString(index);
                //String amount=cursor.getString(index+1);
                list.add(expense);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
         if (cursor != null)
             cursor.close();
            db.close();

        }
        return list;
    }


    public ArrayList<String> getMonthBudget(String month,String year){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.Budget};
        String[] selectionArgs={month,year};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? and "+DataBase2.Year+" =? ",selectionArgs,null,null,null);

            //while (cursor.moveToNext())
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase2.Budget);
                String amount=cursor.getString(index);
                //String amount=cursor.getString(index+1);
                list.add(amount);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }

    public ArrayList<String> getMonthBudget(String month){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.Budget};
        String[] selectionArgs={month};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? ",selectionArgs,null,null,null);

            //while (cursor.moveToNext())
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase2.Budget);
                String amount=cursor.getString(index);
                //String amount=cursor.getString(index+1);
                list.add(amount);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }


    public ArrayList<String> getAllAmount(String month){

        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.Column_Name};
        String[] selectionArgs={month};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? ",selectionArgs,null,null,null);

            //while (cursor.moveToNext())
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase2.Column_Name);
                String amount=cursor.getString(index);
                //String amount=cursor.getString(index+1);
                list.add(amount);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }


    public ArrayList<String> getAllAmount(String month,String year){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.Column_Name};
        String[] selectionArgs={month,year};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? and "+DataBase2.Year+" =? ",selectionArgs,null,null,null);

            //while (cursor.moveToNext())
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase2.Column_Name);
                String amount=cursor.getString(index);
                //String amount=cursor.getString(index+1);
                list.add(amount);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }


    public boolean getMonth(String month){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase2.Month_Name};
        String[] selectionArgs={month};
        String month_name=null;
        Cursor cursor=db.query(DataBase2.Table_Name,columns,DataBase2.Month_Name+" =? ",selectionArgs,null,null,null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DataBase2.Month_Name);
            month_name = cursor.getString(index);
            list.add(month_name);
        }

        cursor.close();
        db.close();

        return list.get(0).equals(month);
    }


//database 2





    //database 3






    public synchronized void insertData(String date,String expense,String totaleCost,String month,String year){

        SQLiteDatabase db = null;
        int check = 0;
        try {
            db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBase3.DATE, date);
            contentValues.put(DataBase3.EXPENSE, expense);
            contentValues.put(DataBase3.COST, totaleCost);
            contentValues.put(DataBase3.CATEGORY,getCategoryByExpense(expense));
            contentValues.put(DataBase3.MONTH,month);
            contentValues.put(DataBase3.YEAR,year);
            ArrayList<String> dateArray = new ArrayList<String>();
            dateArray = getDBInfo();
            for (int i = 0; i < dateArray.size(); i++) {
                //Toast.makeText(context,dateArray.get(i)+" "+exp.get(i),Toast.LENGTH_SHORT).show();
                if (dateArray.get(i).equals(date) && exp.get(i).equals(expense)) {
                    update(date, expense, totaleCost);
                    check = 1;
                }
            }
            if (check == 0) {
                getWritableDatabase().insert(DataBase3.TABLE_NAME, null, contentValues);
            }
        }

        finally{
            db.close();
        }

    }

    public int update(String currentDate,String expense,String cost){

        int count=0;

        try {
            ContentValues contentValues = new ContentValues();
            int oCost = getCostByDateExpense(currentDate, expense);
            int fCost = oCost + Integer.parseInt(cost);
            contentValues.put(DataBase3.COST, fCost + "");
            String[] whreArgs = {currentDate, expense};

            count = this.getWritableDatabase().update(DataBase3.TABLE_NAME, contentValues, DataBase3.DATE + " =? AND " + DataBase3.EXPENSE + " =?", whreArgs);
        }catch (Exception e){
            e.printStackTrace();
        }

        return count;
    }

    public ArrayList<String> getDBInfo(){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase3.DATE,DataBase3.EXPENSE,DataBase3.COST};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase3.TABLE_NAME,columns,null,null,null,null,null);

            while (cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase3.DATE);
                String date=cursor.getString(index);
                String str=cursor.getString(index+1);
                exp.add(str);
                list.add(date);
                // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();

        }


        return list;
    }

    public ArrayList<String> getCostInfo(){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DataBase3.COST};
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase3.TABLE_NAME,columns,null,null,null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                list.add(cursor.getString(cursor.getColumnIndex(DataBase3.COST)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();

        }

        return list;
    }

    public String getTotalCost3(){
        SQLiteDatabase db=getReadableDatabase();
        String str=null;
        String sql="select sum(COST) from DATE_COST";
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            str=cursor.getString(0);
        }


        cursor.close();
        db.close();
        return str;
    }


    public ArrayList<String> getCostByDate(String date){

        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> costList=new ArrayList<>();

        //Cursor cursor=db.query(TABLE_NAME,null,DATE+" BETWEEN ? AND ?",new String[]{from,to},null,null,null,null);
        // DataBase3.DATE+" =? ",new String[]{date}
        Cursor cursor=null;

        try{
            cursor=db.query(DataBase3.TABLE_NAME,new String[]{DataBase3.COST},DataBase3.DATE+" =? ",new String[]{date},null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                costList.add(cursor.getString(cursor.getColumnIndex(DataBase3.COST)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();

        }


        return costList;
    }


    public int getCostByDateExpense(String date,String expense){

        SQLiteDatabase database=getReadableDatabase();
        int cost=0;
        Cursor cursor=null;

        try{
            cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.COST},DataBase3.DATE+" =? and "+DataBase3.EXPENSE+" =? ",new String[]{date,expense},null,null,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                cost=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBase3.COST)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
            database.close();

        }

        return cost;

    }

    public ArrayList<String> getAllDates(){
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.DATE},null,null,null,null,null,null);
        ArrayList<String> dates=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            dates.add(cursor.getString(cursor.getColumnIndex(DataBase3.DATE)));
        }


        cursor.close();
        database.close();
        return dates;
    }


    public ArrayList<String> getDistinctDate(){

        SQLiteDatabase database=getReadableDatabase();
        String sql="select distinct("+DataBase3.DATE+")"+" from "+DataBase3.TABLE_NAME;
        Cursor cursor=database.rawQuery(sql,null);
        ArrayList<String> dDate=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            dDate.add(cursor.getString(cursor.getColumnIndex(DataBase3.DATE)));
        }



        cursor.close();
        database.close();
        return dDate;
    }


    public String[] getAllExpenseByDate(String date){

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.EXPENSE},DataBase3.DATE+" =? ",new String[]{date},null,null,null);
        String[] exp=new String[cursor.getCount()];
        int i=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            exp[i]=cursor.getString(cursor.getColumnIndex(DataBase3.EXPENSE));
            i++;
        }
        cursor.close();
        database.close();

        return exp;
    }

    public String[] getAllExpenseByDate(String cat,String month,String year){

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.EXPENSE},DataBase3.CATEGORY+" = ? and "+DataBase3.MONTH+" =? and "+DataBase3.YEAR+" =? ",new String[]{cat,month,year},null,null,null);
        String[] exp=new String[cursor.getCount()];
        int i=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            exp[i]=cursor.getString(cursor.getColumnIndex(DataBase3.EXPENSE));
            i++;
        }
        cursor.close();
        database.close();

        return exp;
    }

    public float[] getAllCostByDate(String cat,String month,String year){

        SQLiteDatabase database=getReadableDatabase();
        float[] costs=null;
        Cursor cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.COST},DataBase3.CATEGORY+" = ? and "+DataBase3.MONTH+" =? and "+DataBase3.YEAR+" =? ",new String[]{cat,month,year},null,null,null);
        costs=new float[cursor.getCount()];
        int i=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            costs[i]=Float.parseFloat(cursor.getString(cursor.getColumnIndex(DataBase3.COST)));
            i++;
        }
        cursor.close();
        database.close();

        return costs;
    }


    public String[] getCategoryByCurrentDate(String d){

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.query(true,DataBase3.TABLE_NAME,new String[]{DataBase3.CATEGORY},DataBase3.DATE+" =? ",new String[]{d},null,null,null,null);
        String[] cat=new String[cursor.getCount()];


        //Toast.makeText(context,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
        int i=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            cat[i]=cursor.getString(cursor.getColumnIndex(DataBase3.CATEGORY));
            i++;
        }

        cursor.close();
        database.close();
        return cat;
    }

    public ArrayList<String> getCategoryByCurrentDate(String month,String year){

        Cursor cursor=null;
        try{
            cursor=getReadableDatabase().query(true,DataBase3.TABLE_NAME,new String[]{DataBase3.CATEGORY},DataBase3.MONTH+" =? and "+DataBase3.YEAR+" =? ",new String[]{month,year},null,null,null,null);
        }catch (NullPointerException np){
            np.printStackTrace();
        }
        ArrayList<String> cat=new ArrayList<>();


        //Toast.makeText(context,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
        try{
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                cat.add(cursor.getString(cursor.getColumnIndex(DataBase3.CATEGORY)));
            }
        }catch (NullPointerException np){
            np.printStackTrace();
        }

        cursor.close();
        return cat;
    }


    public int getTotalOfCategory(String category,String d){

        SQLiteDatabase database=getReadableDatabase();

        int cost=0;

        Cursor cursor = database.query(DataBase3.TABLE_NAME, new String[]{DataBase3.COST}, DataBase3.DATE + " =? and " + DataBase3.CATEGORY + " =? ", new String[]{d,category.substring(0,1).toUpperCase()+category.substring(1)}, null, null, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            cost=cost+(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBase3.COST))));
        }
        cursor.close();
        database.close();
        return cost;

    }

    public String getAllExpensesByCategory(String category,String date){

        SQLiteDatabase database=getReadableDatabase();
        StringBuffer sb=new StringBuffer();
        Cursor cursor=database.query(DataBase3.TABLE_NAME,new String[]{DataBase3.EXPENSE},DataBase3.CATEGORY+" =? and "+DataBase3.DATE+" =? ",new String[]{category,date},null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            sb.append(cursor.getString(cursor.getColumnIndex(DataBase3.EXPENSE)));
            sb.append("\n");
        }

        cursor.close();
        database.close();
        return sb.toString();

    }

    public int delete3(String current){
        SQLiteDatabase db=getWritableDatabase();
        String[] whereArgs={current};
        int count=0;
        try{
            count=db.delete(DataBase3.TABLE_NAME,DataBase3.EXPENSE+" =? ",whereArgs);
        }catch (SQLiteException sq){
            sq.printStackTrace();
        }
        finally {
            db.close();
        }

        return count;
    }






    public void updateExpenseTitle3(String category,String expense_Title,String nExpense)
    {
        SQLiteDatabase database=getReadableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBase3.EXPENSE,nExpense);
        try{
        database.update(DataBase3.TABLE_NAME,contentValues,DataBase3.CATEGORY+" =? and "+DataBase3.EXPENSE+" =? ",new String[]{category.substring(0,1).toUpperCase()+category.substring(1),expense_Title});
        }finally {
            database.close();
        }
    }








    // payee+payer database




    public void insertPayeeInfo(String name,String amount,String email,String date,String phone){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PayeeDataBase.Payee_Name,name);
        values.put(PayeeDataBase.Amount,amount);
        values.put(PayeeDataBase.Email,email);
        values.put(PayeeDataBase.phone,phone);
        values.put(Dates,date);


        try {
            db.insert(PayeeDataBase.Table_Name, null, values);
        }catch (SQLException s){
            s.printStackTrace();
        }finally {
            db.close();
        }
    }



    public void insertPayerInfo(String name,String amount,String email,String date,String phone){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PayeeDataBase.Payer_Name,name);
        values.put(PayeeDataBase.Payer_Amount,amount);
        values.put(PayeeDataBase.Payer_Email,email);
        values.put(PayeeDataBase.Payer_Dates,date);
        values.put(PayeeDataBase.phone,phone);


        try {
            long l = db.insert(PayeeDataBase.Table_Payer, null, values);

        }catch (SQLException s){
            s.printStackTrace();
        }
        finally {
            db.close();
        }
    }







    public ArrayList<String> getName(){

        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Payee_Name};
        Cursor cursor=db.query(PayeeDataBase.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Payee_Name);
            String str=cursor.getString(index);
            list.add(str);
        }

        cursor.close();
        db.close();
        return list;
    }


    public ArrayList<String> getPayerName(){

        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Payer_Name};
        Cursor cursor=db.query(PayeeDataBase.Table_Payer,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Payer_Name);
            String str=cursor.getString(index);
            list.add(str);
        }

        cursor.close();
        db.close();
        return list;
    }



    public ArrayList<String> getAmount(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Amount};
        Cursor cursor=db.query(PayeeDataBase.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Amount);
            String str=cursor.getString(index);
            list.add(str);
        }
        cursor.close();
        db.close();
        return list;
    }


    public ArrayList<String> getPayerAmount(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Payer_Amount};
        Cursor cursor=db.query(PayeeDataBase.Table_Payer,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Payer_Amount);
            String str=cursor.getString(index);
            list.add(str);
        }
        cursor.close();
        db.close();
        return list;
    }




    public ArrayList<String> getEmail(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Email};
        Cursor cursor=db.query(PayeeDataBase.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Email);
            String str=cursor.getString(index);
            list.add(str);
        }
        cursor.close();
        db.close();
        return list;
    }


    public ArrayList<String> getPayerEmail(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Payer_Email};
        Cursor cursor=db.query(PayeeDataBase.Table_Payer,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Payer_Email);
            String str=cursor.getString(index);
            list.add(str);
        }

        cursor.close();
        db.close();
        return list;
    }



    public ArrayList<String> getPhoneNumbers(String s){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.phone};
        Cursor cursor=db.query(s,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.phone);
            String str=cursor.getString(index);
            list.add(str);
        }

        cursor.close();
        db.close();

        return list;
    }


    public ArrayList<String> getDate(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Dates};
        Cursor cursor=db.query(PayeeDataBase.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Dates);
            String str=cursor.getString(index);
            list.add(str);
        }
        cursor.close();
        db.close();

        return list;
    }


    public ArrayList<String> getPayerDate(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<String> list=new ArrayList<String>();
        String[] columns={PayeeDataBase.Payer_Dates};
        Cursor cursor=db.query(PayeeDataBase.Table_Payer,columns,null,null,null,null,null);

        while (cursor.moveToNext()){

            int index=cursor.getColumnIndex(PayeeDataBase.Payer_Dates);
            String str=cursor.getString(index);
            list.add(str);
        }

        db.close();
        cursor.close();
        db.close();
        return list;
    }


    public void deleteRecords(String name,String email,String dates,int i){

        SQLiteDatabase database=getWritableDatabase();

        if (i == 0) {
            database.delete(PayeeDataBase.Table_Name,PayeeDataBase.Payee_Name+" =? and "+PayeeDataBase.Email+" =? and "+PayeeDataBase.Dates+" =? ",new String[]{name,email,dates});

        }
        else{
            database.delete(PayeeDataBase.Table_Payer,PayeeDataBase.Payer_Name+" =? and "+PayeeDataBase.Payer_Email+" =? and "+PayeeDataBase.Payer_Dates+" =? ",new String[]{name,email,dates});
        }
        database.close();
    }


    public int getAllAmountOfAllCategory(String cate) {


        SQLiteDatabase database=getReadableDatabase();

        Cursor cursor=database.query(Table_Name,new String[]{Expense_Cost},Category+" =? ",new String[]{cate},null,null,null);

        int total=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            total=total+(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Expense_Cost))));
        }

        cursor.close();
        database.close();
        return total;
    }



    public ArrayList<String> getAllExpenseOfced(){

        SQLiteDatabase database=getReadableDatabase();

        Cursor cursor=database.query(Table_Name,new String[]{Expense_Title},null,null,null,null,null);
        ArrayList<String> list=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex(Expense_Title)));
        }
        if (cursor != null)
            cursor.close();
        database.close();
        return list;
    }

    public void deleteAllTables() {

        SQLiteDatabase database = getWritableDatabase();
        ArrayList<String> arrTblNames = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrTblNames.add(cursor.getString(cursor.getColumnIndex("name")));
                cursor.moveToNext();
            }


            for (int i = 0; i < arrTblNames.size(); i++) {
                try{
                    database.delete(arrTblNames.get(i),null,null);
                }catch (SQLiteException sl){
                    sl.printStackTrace();
                }


            }
        }
        cursor.close();
        database.close();
    }



    public String[] getAllYears(){

        String[] years=null;
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.query(true,Income_Table,new String[]{Income_Year},null,null,null,null,null,null);
        try {
            years=new String[cursor.getCount()+1];
        }catch (Exception e){
            e.printStackTrace();
        }

        int i=1;

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i==1)
                years[0]="Select Year";
            years[i]=cursor.getString(cursor.getColumnIndex(Income_Year));
            i++;
        }
        cursor.close();
        database.close();

        return years;
    }



    public ArrayList<Integer> getAllBudget(){
        SQLiteDatabase database=getReadableDatabase();

        ArrayList<Integer> budgets=new ArrayList<>();

        Cursor cursor=database.query(Table_Name,new String[]{Budget},null,null,null,null,null);
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            budgets.add(cursor.getInt(cursor.getColumnIndex(Budget)));


        }


        cursor.close();
        database.close();
        return budgets;

    }



    public ArrayList<Integer> getBudgetsByMonth(String  month){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<Integer> budgets=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=database.query(DataBase2.Table_Name,new String[]{DataBase2.Budget},DataBase2.Month_Name+" =? ",new String[]{month},null,null,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                budgets.add(cursor.getInt(cursor.getColumnIndex(DataBase2.Budget)));
            }
        }catch (Exception e){

        }finally {
            cursor.close();
            database.close();
        }





        return budgets;

    }

    public ArrayList<String> getExpenseByMonth(String  month){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<String> expenses=new ArrayList<>();
        Cursor cursor=database.query(DataBase2.Table_Name,new String[]{DataBase2.List_Column},DataBase2.Month_Name+" =? ",new String[]{month},null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            expenses.add(cursor.getString(cursor.getColumnIndex(DataBase2.List_Column)));
        }



        database.close();
        cursor.close();

        return expenses;

    }


    public ArrayList<Integer> getExpenseAmountByMonth(String  month){

        SQLiteDatabase database=getReadableDatabase();
        ArrayList<Integer> amounts=new ArrayList<>();
        Cursor cursor=database.query(DataBase2.Table_Name,new String[]{DataBase2.Column_Name},DataBase2.Month_Name+" =? ",new String[]{month},null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            amounts.add(cursor.getInt(cursor.getColumnIndex(DataBase2.Column_Name)));
        }


        database.close();
        cursor.close();


        return amounts;

    }


    boolean checkExpense(String expense_Title){

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=null;
        try{

            cursor=database.query(Table_Name,new String[]{Expense_Title},Expense_Title+" =? ",new String[]{expense_Title},null,null,null,null);

            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext())
                if (cursor.getString(cursor.getColumnIndex(Expense_Title)).equals(expense_Title)) {
                    cursor.close();

                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            database.close();
        }

        return false;
    }


    public boolean isTableEmpty(){
        SQLiteDatabase database=getReadableDatabase();
        String count = "SELECT count(*) FROM "+DataBase2.Table_Name;
        Cursor mcursor = database.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount <= 0){
            return true;
        }
        return false;
    }


    public boolean executeSqlForUD(String date){

        boolean b=true;
        SQLiteDatabase database=getWritableDatabase();



        try{
            database.delete(Income_Table,Income_Date+" =? ",new String[]{date});
        }
        catch (SQLException s){
            s.printStackTrace();
            b=false;
        }
        finally {
            database.close();
        }
        return b;
    }


}

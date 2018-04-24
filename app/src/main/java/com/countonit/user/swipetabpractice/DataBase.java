package com.countonit.user.swipetabpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 6/17/2017.
 */

public class DataBase {

    Context context;
    DBHelper helper;

    public DataBase(Context context){
        helper=new DBHelper(context);
        this.context=context;
    }


    public long insertData(String content){

        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(helper.List_Column,content);
        contentValues.put(helper.Column_Name,"0");
        long id=db.insert(helper.Table_Name,null,contentValues);
        return id;
    }

    public ArrayList<String> getAllData(){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] columns={helper.List_Column,helper.Column_Name};
        Cursor cursor=db.query(helper.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            int index=cursor.getColumnIndex(helper.List_Column);
            String expense=cursor.getString(index);
            //String amount=cursor.getString(index+1);
            list.add(expense);
            // Toast.makeText(context," "+list.get(i),Toast.LENGTH_SHORT).show();
        }

        return list;
    }

    public ArrayList<String> getAllData(int i){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] columns={helper.List_Column,helper.Column_Name};
        Cursor cursor=db.query(helper.Table_Name,columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            int index=cursor.getColumnIndex(helper.Column_Name);
            String amn=cursor.getString(index);
            //String amount=cursor.getString(index+1);
            list.add(amn);
        }

        return list;
    }



    public static class DBHelper extends SQLiteOpenHelper {

        private Context context;
        private static final String DB_Name="database1";
        private static final String Table_Name="EXPENSE_LIST";
        private static final int DB_Version=1;
        private static final String ID="_id";
        private static final String List_Column="LIST_COLUMN";
        private static final String Column_Name="AMOUNT";
        //private static final String Column_Income="INCOME";
        private static final String Create_Table="CREATE TABLE "+Table_Name+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+List_Column+" VARCHAR(100), "+Column_Name+" INTEGER);";
        private static final String query="DROP TABLE IF EXISTS "+Table_Name;


        public DBHelper(Context context){
            super(context,DB_Name,null,DB_Version);
            this.context=context;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(Create_Table);
            }catch (SQLException s){
                Toast.makeText(context,""+s,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(query);
            onCreate(db);
        }

    }

    public int update(String old,String New){

        SQLiteDatabase db=helper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(helper.List_Column,New);
        String[] whreArgs={old};
        int count= db.update(helper.Table_Name,contentValues,helper.List_Column+" =? ",whreArgs);
        return count;
    }


    public int update(String New,int id){

        SQLiteDatabase db=helper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(helper.Column_Name,New);
        contentValues.put(helper.ID,String.valueOf(id));
        String[] whreArgs={String.valueOf(id)};
        int count= db.update(helper.Table_Name,contentValues,helper.ID+" =? ",whreArgs);
       /* if (count > 0)
            Toast.makeText(context,"Inserted amount to db lol",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"couldn't Insert amount to db lol"+String.valueOf(id),Toast.LENGTH_SHORT).show();*/
        db.close();
        return count;
    }


    public int getIdBy(String name){

        SQLiteDatabase db=helper.getWritableDatabase();
        String[] columns={helper.ID};
        String[] selectionArgs={name};
        int id=0;
        Cursor cursor=db.query(helper.Table_Name,columns,helper.List_Column+" =? ",selectionArgs,null,null,null);

        while(cursor.moveToNext()){
            int index=cursor.getColumnIndex(helper.ID);
            id=cursor.getInt(index);
        }
        return id;
    }


    public void resetAmounts(String ramn){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(helper.Column_Name,"0");
        String[] whereArgs={ramn};
        int count=db.update(helper.Table_Name,contentValues,helper.Column_Name+ " =? ",whereArgs);
       /* if (count > 0)
            Toast.makeText(context,"Successfully Resetted All Amounts",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Please Check Your Memory Resource",Toast.LENGTH_SHORT).show();*/
        db.close();
    }


    public int delete(String current){

        SQLiteDatabase db=helper.getWritableDatabase();
        String[] whereArgs={current};
        int count=db.delete(helper.Table_Name,helper.List_Column+" =? ",whereArgs);
        return count;
    }

}

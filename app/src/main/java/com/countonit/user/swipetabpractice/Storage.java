package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 6/23/2017.
 */

public class Storage extends AppCompatActivity {

    Context context;
    public Storage(Context context){
        this.context=context;
    }


    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    public boolean saveData(String income) {
        FileOutputStream fileOutputStream=null;
        //file=getDir("Total_Expense.txt",Context.MODE_PRIVATE);

        try {
            fileOutputStream = context.openFileOutput("Total_Expense.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(income.getBytes());

        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }

        finally {
            try{
                fileOutputStream.close();
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        return true;
    }

    public String loadData(){
        FileInputStream fileInputStream=null;
        //file=getDir("Total_Expense.txt",Context.MODE_PRIVATE);
        StringBuffer buffer=null;
        try{
            fileInputStream=context.openFileInput("Total_Expense.txt");
            int read;
            buffer=new StringBuffer();

            while ((read=fileInputStream.read()) != -1){
                buffer.append((char) read);
            }

        }catch (FileNotFoundException f){
            Toast.makeText(context,""+f,Toast.LENGTH_SHORT).show();
            f.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }finally {
            try{
                fileInputStream.close();
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        String income=buffer.toString();
        if (income == null || income.isEmpty()){
            income="0";
        }
        return income;
    }
}

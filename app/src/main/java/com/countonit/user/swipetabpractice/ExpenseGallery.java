package com.countonit.user.swipetabpractice;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.countonit.user.swipetabpractice.adapter.GaleryAdapter;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;


/**
 * Created by User on 11/19/2017.
 */

public class ExpenseGallery extends AppCompatActivity{




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grid_gallery);
        Slidr.attach(this);


        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Expense Gallery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));*/

        new UpdateGallery().execute();


    }



        ArrayList<String> getAllImages(){
        MainDB mainDB;
        mainDB=new MainDB(this);
        ArrayList<String> expList=new ArrayList<>();

        expList=mainDB.getAllExpenseOfced();

        return expList;
    }

    private class UpdateGallery extends AsyncTask<Integer,Integer,Boolean>{

        RecyclerView recyclerView;
        ArrayList<String> images;
        ProgressDialog dialog=new ProgressDialog(ExpenseGallery.this);

        @Override
        protected void onPreExecute() {
            recyclerView=(RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            images=getAllImages();
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (dialog.isShowing())
                dialog.dismiss();

            GaleryAdapter galeryAdapter=new GaleryAdapter(ExpenseGallery.this,images);
            recyclerView.setAdapter(galeryAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }


        return true;
    }

}

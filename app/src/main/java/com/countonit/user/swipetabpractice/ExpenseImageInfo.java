package com.countonit.user.swipetabpractice;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by User on 11/20/2017.
 */

public class ExpenseImageInfo {


    private ArrayList<String> expList;
    private MainDB mainDB;



    PhotoViewAttacher photoViewAttacher;



    ArrayList<String> getAllImages(){

        ArrayList<String> expList=new ArrayList<>();

        expList=mainDB.getAllExpenseOfced();

        return expList;
    }

    public AlertDialog makeDialog(Context context,String expenseName,int position){


        mainDB=new MainDB(context);

        AlertDialog.Builder aler=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.image_info_dialog,null);

        ImageView image=view.findViewById(R.id.expense_image_info);
        TextView date=view.findViewById(R.id.dateOfthisCOst);
        TextView cost=view.findViewById(R.id.costOfthis);

        aler.setTitle(expenseName);
        aler.setView(view);
        AlertDialog dialog=aler.create();

        String cat=mainDB.getCategoryByExpense(expenseName);
        expList=getAllImages();


        date.setText("You Have Purchase This on "+mainDB.getTime(cat,expenseName));
        cost.setText("This has been cost "+mainDB.getCosts(cat,expenseName));

        byte[] img=mainDB.getExpenseImage(mainDB.getIdBy(expList.get(position)));
        image.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length, new BitmapFactory.Options()));

        photoViewAttacher=new PhotoViewAttacher(image);
        photoViewAttacher.update();

        return dialog;

    }




}

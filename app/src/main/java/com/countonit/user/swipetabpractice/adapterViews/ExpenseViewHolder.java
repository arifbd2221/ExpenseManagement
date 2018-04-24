package com.countonit.user.swipetabpractice.adapterViews;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.countonit.user.swipetabpractice.R;
import com.countonit.user.swipetabpractice.TakeExpenseInput;
import com.countonit.user.swipetabpractice.database.MainDB;

/**
 * Created by User on 8/25/2017.
 */

public class ExpenseViewHolder extends ChildViewHolder {

    public TextView expText,spend,aBudget,leftMoney;
    String ex;
    Context context;
    public ProgressBar progressBar;
    MainDB mainDB;
    SharedPreferences sharedPreferences;

    public ExpenseViewHolder(final View itemView) {
        super(itemView);


        context=itemView.getContext();

        sharedPreferences=context.getSharedPreferences("PayeeAsExpense",Context.MODE_PRIVATE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initDialog(ex);



                if ( !sharedPreferences.getString(ex,"").equals("Payee") ){

                    Intent intent=new Intent(itemView.getContext(), TakeExpenseInput.class);
                    intent.putExtra("data",ex);
                    itemView.getContext().startActivity(intent);
                }
            }
        });
        spend=(TextView) itemView.findViewById(R.id.spending);
        aBudget=(TextView) itemView.findViewById(R.id.aBudget);
        leftMoney=(TextView) itemView.findViewById(R.id.leftmoney);
        expText=(TextView) itemView.findViewById(R.id.exp_view_id);
        progressBar=(ProgressBar) itemView.findViewById(R.id.child_progressbar);
        progressBar.setMax(100);
    }

    public void bind(Expenses expenses){
        mainDB=new MainDB(context);

        SharedPreferences sp=context.getSharedPreferences("CurrencyPosition",Context.MODE_PRIVATE);
        String cat=mainDB.getCategoryByExpense(expenses.getName());
        int s=mainDB.getCosts(cat,expenses.getName());
        int b=mainDB.getBudgetByExpense(cat,expenses.getName());
        spend.setText(""+s+" "+context.getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);
        aBudget.setText(""+b+" "+context.getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);
        if ((b-s) < 0){
            leftMoney.setText(""+(b-s)+" "+context.getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);
            leftMoney.setTextColor(Color.RED);
            Toast.makeText(context,expenses.getName()+",Exceeded The Target Budget",Toast.LENGTH_LONG).show();
        }else
        leftMoney.setText(""+(b-s)+" "+context.getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);
        ex=expenses.getName();
        expText.setText(ex.substring(0,1).toUpperCase()+ex.substring(1));
        float prgs=(Float.parseFloat(s+"") / Float.parseFloat(b+""))*100;
        progressBar.setProgress((int) prgs);

        if (sharedPreferences.getString(ex,"").equals("Payee")){
            spend.setText(""+b);
            progressBar.setProgress(100);
        }


    }

}

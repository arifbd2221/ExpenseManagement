package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;

import java.util.ArrayList;

/**
 * Created by User on 7/22/2017.
 */

public class AddExpenseAdapter extends RecyclerView.Adapter<AddExpenseAdapter.ViewHolder> {



    ArrayList<String> list;
    LayoutInflater layoutInflater;
    Context context;

    public AddExpenseAdapter(Context context,ArrayList<String> list){
            layoutInflater=LayoutInflater.from(context);
            this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.add_expense,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddExpenseAdapter.ViewHolder holder, int position) {
        String str=list.get(position);
        holder.expense.setText(str.substring(0,1).toUpperCase()+str.substring(1));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView expense;
        public ViewHolder(View itemView) {
            super(itemView);
            expense=(TextView) itemView.findViewById(R.id.expense_demo);
        }
    }


    public void remove(int position) {
        new MainDB(context).delete(list.get(position));
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());
    }


    public void addItem(String item,String cat,int budget){
        list.add(item);
        //new DataBase(context).insertData(item);
        new MainDB(context).createCategoryExpense(cat,item,budget);
        notifyItemInserted(list.size());
    }

}

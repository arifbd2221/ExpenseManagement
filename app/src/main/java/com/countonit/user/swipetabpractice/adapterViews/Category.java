package com.countonit.user.swipetabpractice.adapterViews;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by User on 8/25/2017.
 */

public class Category implements Parent<Expenses> {

    String catName;
    List<Expenses> expenseList;

    public Category(String name,List<Expenses> allExpense){
                catName=name;
                expenseList=allExpense;
    }

    @Override
    public List<Expenses> getChildList() {
        return expenseList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }


   public String getName(){
        return catName;
    }

}

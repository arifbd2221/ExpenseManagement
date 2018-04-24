package com.countonit.user.swipetabpractice.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.countonit.user.swipetabpractice.R;
import com.countonit.user.swipetabpractice.adapterViews.Category;
import com.countonit.user.swipetabpractice.adapterViews.CategoryViewHolder;
import com.countonit.user.swipetabpractice.adapterViews.ExpenseViewHolder;
import com.countonit.user.swipetabpractice.adapterViews.Expenses;


import java.util.List;

/**
 * Created by User on 8/25/2017.
 */

public class ExpandableRecycler extends ExpandableRecyclerAdapter<Category,Expenses,CategoryViewHolder,ExpenseViewHolder>{


    LayoutInflater inflater;
    Context context;


    public ExpandableRecycler(Context context,@NonNull List<Category> parentList) {
        super(parentList);
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

            View v=inflater.inflate(R.layout.category_view,parentViewGroup,false);
        return new CategoryViewHolder(v);
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.expense_view,childViewGroup,false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindParentViewHolder(@NonNull CategoryViewHolder parentViewHolder, int parentPosition, @NonNull Category parent) {
            parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ExpenseViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Expenses child) {
        childViewHolder.bind(child);
    }


}

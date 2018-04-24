package com.countonit.user.swipetabpractice.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 8/25/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;



    public ExpandableListAdapter(Context context, List<String> laptops,
                                 HashMap<String, List<String>> laptopCollections)
    {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
    }



    @Override
    public int getGroupCount() {
        return laptops.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String laptopName=(String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.head,null);
        }
        TextView item=(TextView) convertView.findViewById(R.id.head_view);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop=(String) getChild(groupPosition,childPosition);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView=inflater.inflate(R.layout.child,null);
        }
        TextView childText=(TextView) convertView.findViewById(R.id.child_text);
        childText.setText(laptop);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

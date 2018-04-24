package com.countonit.user.swipetabpractice.statistics;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.R;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 8/28/2017.
 */

public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    private String[] cat;
    private DecimalFormat format;
    Context con;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter,String[] cat) {
        super(context, R.layout.custom_marker_view);
        con=context;
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
        this.cat=cat;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        //expense[Integer.parseInt(xAxisValueFormatter.getFormattedValue(e.getX(), null))-1]
        String string=new MainDB(con).getAllExpensesByCategory(cat[Integer.parseInt(xAxisValueFormatter.getFormattedValue(e.getX(), null))],dateFormat.format(new Date()));
        tvContent.setText(string);


       // tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        tvContent.setTextColor(Color.RED);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}

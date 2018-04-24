package com.countonit.user.swipetabpractice.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.R;

/**
 * Created by User on 8/27/2017.
 */

public class HelpViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    int[] layouts;

    public HelpViewPagerAdapter(Context context,int[] layout){
            this.context=context;
            this.layouts=layout;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);

        if (position == 2){
            TextView textView=(TextView) view.findViewById(R.id.videoLink);

                SpannableString content = new SpannableString(textView.getText().toString());
                content.setSpan(new UnderlineSpan(), 0, textView.getText().toString().length(), 0);
                textView.setText(content);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
                    Log.i("Video", "Video Playing....");
                }
            });
        }

        return view;
    }




    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}

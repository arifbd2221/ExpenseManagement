package com.countonit.user.swipetabpractice.adapterViews;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.countonit.user.swipetabpractice.R;

import static android.R.attr.animationDuration;

/**
 * Created by User on 8/25/2017.
 */

public class CategoryViewHolder extends ParentViewHolder {
    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */

    //SharedPreferences sharedPreferences;
    //Integer[] icons={R.drawable.bus,R.drawable.grocery};
    TextView catText;
    ImageView arrow;
    //ImageView catIcon;
    RelativeLayout relativeLayout;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        catText=(TextView) itemView.findViewById(R.id.catView_id);
        arrow=(ImageView) itemView.findViewById(R.id.dArrow);
        //catIcon=(ImageView) itemView.findViewById(R.id.catIcon);
        //sharedPreferences=itemView.getContext().getSharedPreferences("CatIcon", Context.MODE_PRIVATE);
        relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    RotateAnimation rotateAnimation = new RotateAnimation(0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setInterpolator(new DecelerateInterpolator());
                    rotateAnimation.setRepeatCount(0);
                    rotateAnimation.setDuration(animationDuration);
                    rotateAnimation.setFillAfter(true);
                    arrow.startAnimation(rotateAnimation);


                    collapseView();
                } else {
                    RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setInterpolator(new DecelerateInterpolator());
                    rotateAnimation.setRepeatCount(0);
                    rotateAnimation.setDuration(animationDuration);
                    rotateAnimation.setFillAfter(true);
                    arrow.startAnimation(rotateAnimation);

                    expandView();
                }
            }
        });
        //arrow=(ImageButton) itemView.findViewById(R.id.arrow);
    }


    public void bind(Category category){

        String temp=category.getName();


        catText.setText(temp.substring(0,1).toUpperCase()+temp.substring(1));
        //catIcon.setImageResource(icons[sharedPreferences.getInt(category.getName(),0)]);
    }
}

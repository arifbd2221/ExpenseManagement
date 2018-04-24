package com.countonit.user.swipetabpractice;



import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;
import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.r0adkll.slidr.Slidr;


public class MainActivity extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;
    //private int[] tabIcons={R.drawable.coinstack,R.drawable.budget,R.drawable.save};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_preferences = getSharedPreferences("com.example.user.swipetabpractice_preferences",MODE_PRIVATE);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 0){
            setTheme(Constant.theme);
        }else if (appTheme == 0){
            setTheme(Constant.theme);
        }else{
            setTheme(appTheme);
        }


        setContentView(R.layout.activity_main);
        Slidr.attach(this);


        viewPager=(ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        PagerSlidingTabStrip pagerSlidingTabStrip=(PagerSlidingTabStrip) findViewById(R.id.pagerSlider);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setTextColor(Color.WHITE);
        pagerSlidingTabStrip.setIndicatorColor(Color.GREEN);


               /* tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        //setupTabIcons();
    }

   /* private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "Today's Report");
        adapter.addFragment(new Fragment3(), "Monthly Report");
        adapter.addFragment(new BudgetVsExpense(),"Budget Vs Expense");
        adapter.addFragment(new IncomeExpenseCompare(), "Income Vs Expense");
        adapter.addFragment(new IncomeHistory(),"Income History");
        viewPager.setAdapter(adapter);
    }

}

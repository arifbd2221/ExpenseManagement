package com.countonit.user.swipetabpractice;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by User on 7/22/2017.
 */

public class Create_Expense extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<String> list;
    AddExpenseAdapter adapter;
    ImageButton mic;
    View view;
    Context context=this;
    boolean add=false;
    Paint p=new Paint();
    DataBase db;
    MainDB mainDB=new MainDB(context);
    CoordinatorLayout coordinatorLayout;
    private final int REQ_CODE_SPEECH_INPUT=100;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        app_preferences = getSharedPreferences("com.countonit.user.swipetabpractice_preferences",MODE_PRIVATE);
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

        super.onCreate(savedInstanceState);
        //overridePendingTransition(0,R.anim.fadin);
        setContentView(R.layout.create_expense);
        //Slidr.attach(this);


        initViews();



    }

    private void initViews() {
        db=new DataBase(this);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator) ;
        mic=(ImageButton) findViewById(R.id.mic);

        /*Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-ThinItalic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");*/


        //rotatingTextWrapper.setTypeface(typeface2);



        //rotatingTextWrapper.setTypeface(typeface);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promtSpeech();
            }
        });
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar4);
        toolbar.setTitle("Expense List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView) findViewById(R.id.expense_recycler);
        list=new ArrayList<String>();

        list=mainDB.getExpenseTitle();            //new DataBase(this).getAllData();

        adapter=new AddExpenseAdapter(this,list);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        intiSwipe();

    }

    private void intiSwipe() {

        ItemTouchHelper.SimpleCallback simpleTouchHelper= new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){


                    AlertDialog.Builder aler=new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=layoutInflater.inflate(R.layout.alert_message,null);
                    TextView textView=(TextView) view.findViewById(R.id.textView6) ;
                    aler.setTitle("Attention !!");
                    textView.setText("Are You Sure!! To Proceed");
                    aler.setView(view);
                    final AlertDialog dialog=aler.create();
                    Button yes=(Button) view.findViewById(R.id.yes);
                    Button no=(Button) view.findViewById(R.id.no);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            try{
                                adapter.remove(position);
                                mainDB.delete3(adapter.list.get(position));
                            }catch (IndexOutOfBoundsException ae){
                                ae.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if (direction == ItemTouchHelper.RIGHT){

                    removeView();
                    update(position);
                    //Toast.makeText(getApplicationContext(),"Right Swiped",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                Bitmap bitmap;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#eceff1"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.editable);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(bitmap,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.deletable);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(bitmap,null,icon_dest,p);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void update(final int position) {
        int i=1;
        AlertDialog.Builder aler=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.add_expense_dialog,null);


        final Spinner spinner=(Spinner) view.findViewById(R.id.spinner);
        final EditText editText=(EditText) view.findViewById(R.id.update_expense_dialog);

        Button yes=(Button) view.findViewById(R.id.yes);
        Button no=(Button) view.findViewById(R.id.no);
        aler.setTitle("Edit Expense !!");
        aler.setView(view);

        final AlertDialog dialog=aler.create();

        dialog.show();
        MainDB db=new MainDB(this);
        ArrayList<String> cat=new ArrayList<>();
        cat=db.getAllCategory();
        String[] str=new String[cat.size()+1];
        str[0]="Pick Category";
        for (String st : cat){
            str[i]=st.substring(0,1).toUpperCase()+st.substring(1);
            i++;
        }
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context,editText.getText().toString(),Toast.LENGTH_LONG).show();

                if (editText.getText().toString().trim().isEmpty() && spinner.getSelectedItem().toString().equals("Pick Category") ){
                    Snackbar.make(coordinatorLayout,"Please Do not Leave Fields Blank Also Select a Category!",Snackbar.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else if(editText.getText().toString().trim().isEmpty()){
                    Snackbar.make(coordinatorLayout,"Please Do not Leave Fields Blank!",Snackbar.LENGTH_SHORT).show();

                }
                else if(spinner.getSelectedItem().toString().equals("Pick Category")){
                    Snackbar.make(coordinatorLayout,"Please Select a Category!",Snackbar.LENGTH_SHORT).show();
                }

                else {

                    mainDB.updateExpense(list.get(position),editText.getText().toString(),spinner.getSelectedItem().toString());
                    mainDB.updateExpenseTitle3(spinner.getSelectedItem().toString(),list.get(position),editText.getText().toString());
                    list.set(position, editText.getText().toString());
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();

    }

    private void removeView() {
        try{
            if(view.getParent()!=null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }catch (NullPointerException np){
            np.printStackTrace();
        }

    }

    private void promtSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Intent intent=new Intent(context,CreateExpenseInput.class);
                    intent.putExtra("expName",result.get(0));
                    Toast.makeText(context,result.get(0).toString(),Toast.LENGTH_LONG).show();

                    startActivity(intent);
                    finish();
                }
                break;
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(this,RootScreen.class));
            finish();
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            startActivity(new Intent(this,RootScreen.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }
}

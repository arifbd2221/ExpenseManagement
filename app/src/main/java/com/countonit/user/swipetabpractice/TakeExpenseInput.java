package com.countonit.user.swipetabpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.r0adkll.slidr.Slidr;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by User on 9/4/2017.
 */

public class TakeExpenseInput extends AppCompatActivity {

    TextView dateAndTime,expenseTitle;

    EditText textField,budget,deductBudget;
    ImageView setImage;
    String ex;
    Button save;
    MainDB mainDB;
    FloatingActionButton cam,gall;
    final int cameraReq=100,reqCode=200;
    PhotoViewAttacher photoViewAttacher;

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense);
        //Slidr.attach(this);


        dateAndTime=(TextView) findViewById(R.id.time);

        textField=(EditText) findViewById(R.id.edit_cost);
        budget=(EditText) findViewById(R.id.edit_budget);
        deductBudget=findViewById(R.id.deduct_budget);

        expenseTitle=(TextView) findViewById(R.id.expense_title);
        save=(Button) findViewById(R.id.save);



        setImage=(ImageView) findViewById(R.id.setImage);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Expense");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cam=(FloatingActionButton) findViewById(R.id.action1);
        gall=(FloatingActionButton) findViewById(R.id.action2);






        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Camera",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent,cameraReq);
            }
        });

        gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                Intent galleryIntent =new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,reqCode);
            }
        });

        Intent intent=getIntent();


        try{
            ex=intent.getStringExtra("expName");
            expenseTitle.setText(ex.substring(0,1).toUpperCase()+ex.substring(1));
        }catch (NullPointerException np){
            ex=intent.getStringExtra("data");
            try{
                expenseTitle.setText(ex.substring(0,1).toUpperCase()+ex.substring(1));
            }catch (NullPointerException npe){
                Toast.makeText(this,"Sorry No Text Found From Voice,Try Again!",Toast.LENGTH_SHORT).show();
                npe.printStackTrace();
            }

            np.printStackTrace();
        }



        init();

        byte[] image=mainDB.getExpenseImage(mainDB.getIdBy(ex));
        BitmapFactory.Options options=new BitmapFactory.Options();
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
            setImage.setImageBitmap(bitmap);
        }catch (NullPointerException np){
            np.printStackTrace();
        }
        photoViewAttacher=new PhotoViewAttacher(setImage);
        photoViewAttacher.update();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(TakeExpenseInput.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
// Call displayInterstitial() function
                displayInterstitial();
            }
        });



    }




    private void init() {


        mainDB=new MainDB(this);

        final int b=mainDB.getBudgetByExpense(mainDB.getCategoryByExpense(ex),ex);
        budget.setText(b+"");
        dateAndTime.setText(mainDB.getTime(mainDB.getCategoryByExpense(ex),ex));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amn = textField.getText().toString();
                String bdg=budget.getText().toString();
                String dBudget=deductBudget.getText().toString();


               if ( !dBudget.isEmpty() ){
                    mainDB.updateBudget( ex, mainDB.getCategoryByExpense(ex), b - (Integer.parseInt(dBudget)) );
                    Toast.makeText(getApplicationContext(), "Budget has been Reduced", Toast.LENGTH_SHORT).show();
                    deductBudget.setText("");
                }

               else if (amn.isEmpty() || amn.equals(" ") || bdg.isEmpty() || bdg.equals(" ") || bdg.equals("0") || bdg.equals("00")) {
                    textField.setError("Value Needed");
                    budget.setError("Value Needed");
                    Toast.makeText(getApplicationContext(), "Please Provide Valid Input", Toast.LENGTH_SHORT).show();
                }



                else {
                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                    DateFormat dateFormat2 = new SimpleDateFormat("HH:mm a");
                    Date date = new Date();
                    mainDB.updateCost(mainDB.getCategoryByExpense(ex), ex, Integer.parseInt(amn));

                    if (b != Integer.parseInt(budget.getText().toString()))
                        mainDB.updateBudget(ex, mainDB.getCategoryByExpense(ex), b + (Integer.parseInt(budget.getText().toString())));

                    mainDB.updateTime(mainDB.getCategoryByExpense(ex), ex, dateFormat1.format(date)+", "+dateFormat2.format(date));
                    mainDB.upDateMonthYear(mainDB.getCategoryByExpense(ex), ex);
                    mainDB.insertData(dateFormat1.format(date), ex, amn,getResources().getStringArray(R.array.month)[Calendar.getInstance().get(Calendar.MONTH)+1],Calendar.getInstance().get(Calendar.YEAR)+"");
                    mainDB.close();

                    Toast.makeText(getApplicationContext(),"Amount Successfully Inserted",Toast.LENGTH_SHORT).show();

                    textField.setText("");
                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == reqCode && resultCode == RESULT_OK && data != null) {
            //Uri uri = data.getData();

            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri mImageUri = result.getUri();


            //Toast.makeText(this,"Before Try Block",Toast.LENGTH_SHORT).show();

            try {
                InputStream stream = getContentResolver().openInputStream(mImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                setImage.setImageBitmap(bitmap);
                mainDB.insertImage(mainDB.getIdBy(ex), imageViewToByte(setImage));
                mainDB.close();
                //Toast.makeText(this,"Inside Try Block",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            }

            //Toast.makeText(this,"After Try Block",Toast.LENGTH_SHORT).show();

            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }


        }




            else if (requestCode == cameraReq && resultCode == RESULT_OK) {

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){


                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setImage.setImageBitmap(bitmap);

                    mainDB.insertImage(mainDB.getIdBy(ex), imageViewToByte(setImage));
                    mainDB.close();
                    Toast.makeText(getApplicationContext(), "After set up image", Toast.LENGTH_SHORT).show();

                }


            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_bottom_toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();

                startActivity(new Intent(this,RootScreen.class));
                finish();
                break;

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



    private byte[] imageViewToByte(ImageView image) {

        BitmapDrawable drawable=(BitmapDrawable) image.getDrawable();
        Bitmap bitmap=drawable.getBitmap();

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();

        return byteArray;

    }


    private void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    }


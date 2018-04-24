package com.countonit.user.swipetabpractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by User on 11/22/2017.
 */

public class Privacy extends AppCompatActivity{

    EditText password,retype,mail;
    Button save;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    Vibrator vibrator;
    public static Pattern VALID_EMAIL_ADDRESS_REGEX = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.set_privacy);

        Slidr.attach(this);

        sharedPreferences=getSharedPreferences("Loggin_Credential",MODE_PRIVATE);


        password=(EditText) findViewById(R.id.password);
        retype=(EditText) findViewById(R.id.retypedPassword);
        mail=(EditText) findViewById(R.id.recoveryEmail);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Protect Your Finances");

        save=(Button) findViewById(R.id.save);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (password.getText().toString().isEmpty() && retype.getText().toString().isEmpty()){
                    password.setError("Value Needed");
                    retype.setError("Value Needed");
                }
                else if (password.getText().toString().equals(retype.getText().toString())){


                    VALID_EMAIL_ADDRESS_REGEX =
                            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    if (validate(mail.getText().toString().trim())){

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("password",password.getText().toString());
                        editor.putString("email",mail.getText().toString());
                        editor.putBoolean("checker",true);
                        editor.commit();

                        Toast.makeText(getApplicationContext(),"Password Setup Successfully",Toast.LENGTH_LONG).show();
                    }
                    else {
                        mail.setError("Please Enter A Valid Email");
                    }


                    /*mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Password Successfully Created",
                                                Toast.LENGTH_SHORT).show();


                                        progressDialog.dismiss();
                                    }
                                    else{

                                        Toast.makeText(getApplicationContext(),"Password Creation Failed ,Please Try Again",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                    // ...
                                }
                            });
*/




                    //createCreadentials(mail.getText().toString(),password.getText().toString());

                }else if ( !password.getText().toString().equals(retype.getText().toString())){
                    vibrator.vibrate(150);
                    Toast.makeText(getApplicationContext(),"You Given Password Does Not Matched,Please try Another !!",Toast.LENGTH_LONG).show();
                    password.setText("");
                    retype.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Provide All The Information",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return true;
    }




    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}

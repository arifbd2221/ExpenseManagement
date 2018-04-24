package com.countonit.user.swipetabpractice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 11/22/2017.
 */

public class Loggin_Activity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText password;
    Button login;
    Vibrator vibrator;
    TextView tPassword;
    TextView forgotPassword;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggin_activity);

        password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        forgotPassword=(TextView) findViewById(R.id.forgotPassword);

        String s=forgotPassword.getText().toString();
        SpannableString content = new SpannableString(s);
        content.setSpan(new UnderlineSpan(), 0, s.length(), 0);
        forgotPassword.setText(content);

        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        preferences= getSharedPreferences("Loggin_Credential",MODE_PRIVATE);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();


                /*FirebaseUser user=mFireBase.getCurrentUser();

                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        Toast.makeText(getApplicationContext(),"The Reset Password Credentials has been sent to your mail",Toast.LENGTH_LONG).show();

                        else
                            Toast.makeText(getApplicationContext(),"An Error Occurs, Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });*/

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password.getText().toString().isEmpty()){
                    password.setError("Enter PassWord");
                }
                else if (password.getText().toString().equals(preferences.getString("password","lol"))){
                    startActivity(new Intent(getApplicationContext(),RootScreen.class));
                }
                else {
                    vibrator.vibrate(500);
                    Toast.makeText(getApplicationContext(),"Password doesn't matched, Try Again",Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });




    }


    public void buildDialog(){

        int[] cap={R.drawable.captcha1,R.drawable.captcha2};
        final int a= (int) (Math.random() * ( 1 - 0 ));
        final String[] strings=new String[2];
        strings[0]="GphJ"; strings[1]="6hodFq";
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        final SharedPreferences preferences=getSharedPreferences("Loggin_Credential",MODE_PRIVATE);
        View v=getLayoutInflater().inflate(R.layout.expense_dialog,(ViewGroup) findViewById(R.id.id),false);
        ImageView imageView=(ImageView) v.findViewById(R.id.image);
        final EditText email=(EditText) v.findViewById(R.id.email);
        final EditText captcha=(EditText) v.findViewById(R.id.captcha);
        Button submit=(Button) v.findViewById(R.id.submit);
        tPassword=(TextView) v.findViewById(R.id.password);



        email.setHint(preferences.getString("email","").substring(0,5));
        imageView.setImageResource(cap[a]);
        tPassword.setVisibility(View.GONE);
        alert.setView(v);
        alert.setTitle("Recovery Form");
        AlertDialog dialog=alert.create();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (captcha.getText().toString().equals(strings[a]) && email.getText().toString().equals(preferences.getString("email",""))){
                    tPassword.setText(preferences.getString("password",""));
                    tPassword.setVisibility(View.VISIBLE);


                }
                else {
                    Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    Toast.makeText(getApplicationContext(),"You Must Need To Fill All The Feilds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();






    }


}

package com.countonit.user.swipetabpractice;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.r0adkll.slidr.Slidr;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by User on 7/28/2017.
 */

public class Tools extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final int REQUEST_CODE_SIGN_IN = 2;
    TextView restore,backup,Drestore,Resset,driveBackup;
    Switch aSwitch;
    NotificationReceiver alarm;


    DriveResourceClient mDriceResourceClient;
    Task<DriveFolder> rootFolderTask;

    private static final String TAG = "Google Drive Activity";
    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;

    private GoogleApiClient mGoogleApiClient;
    private Task<DriveContents> createContentsTask;

    private String driveID;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        //overridePendingTransition(0,R.anim.fadin);
        setContentView(R.layout.tools);
        Slidr.attach(this);

        signIn();

        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .build();
        }


        mGoogleApiClient.connect();




        alarm=new NotificationReceiver();

        backup=(TextView) findViewById(R.id.backup);
        restore=(TextView) findViewById(R.id.restore);
        aSwitch=(Switch) findViewById(R.id.switch1);
        driveBackup=(TextView) findViewById(R.id.driveBackup);
        Drestore=(TextView) findViewById(R.id.Drestore);
        Resset=(TextView) findViewById(R.id.resset);

        String check;
        SharedPreferences sharedPreferences=getSharedPreferences("switchState",MODE_PRIVATE);
        check=sharedPreferences.getString("check",null);
        aSwitch.setChecked(sharedPreferences.getBoolean(check,true));



        driveBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });



        Resset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllAmount();
            }
        });




        Drestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    app_preferences=getSharedPreferences("DriveId",MODE_PRIVATE);
                    String id=app_preferences.getString("driveid","");

                    if ( !id.isEmpty() ) {

                        DriveFile file = DriveId.decodeFromString(id).asDriveFile();

                        retrieveContents(file);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Take Backup First...",Toast.LENGTH_LONG).show();

                    }

            }
        });


        aSwitch.setChecked(false);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "This Feature Is Under Progress...", Toast.LENGTH_SHORT).show();


                /*if (aSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("switchState", MODE_PRIVATE).edit();
                    editor.putBoolean("on", true);
                    editor.putString("check","on");
                    editor.commit();
                    getNotificationTime();
                    Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_SHORT).show();

                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("switchState", MODE_PRIVATE).edit();
                    editor.putBoolean("off", false);
                    editor.putString("check","off");
                    editor.commit();
                    if(alarm != null){
                        alarm.CancelAlarm(getApplicationContext());
                    }else{
                        Toast.makeText(getApplicationContext(), "Alarm is null", Toast.LENGTH_SHORT).show();
                    }
                    }
*/

            }
        });
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    File sd = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Expense Management/");
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "//data//com.countonit.user.swipetabpractice//databases//Count_on_it";
                        String backupDBPath = "Count_on_it.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(backupDB).getChannel();
                            FileChannel dst = new FileOutputStream(currentDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    File sd = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Expense Management/");
                    sd.mkdir();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {

                        //SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
                        //String dbName=simpleDateFormat.format(new Date()).toString();

                        String currentDBPath = "//data//com.countonit.user.swipetabpractice//databases//Count_on_it";
                        String backupDBPath = "Count_on_it.db";

                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        Log.d("backupDB path", "" + backupDB.getAbsolutePath());

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getNotificationTime() {

        Calendar calendar=Calendar.getInstance();
        TimePickerDialog timePickerDialog=TimePickerDialog.newInstance(this,calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);   // true means 24 hours format else 12 hours


        timePickerDialog.setTitle("Set Your Preffered Time");
        timePickerDialog.show(getFragmentManager(),"Time Picker");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {



        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,second);
        alarm.SetAlarm(this,calendar);
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    public void resetAllAmount() {


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {



                        MainDB mainDB=new MainDB(getApplicationContext());
                        //mainDB.resetAllWhenMonthEnd();
                        mainDB.deleteAllTables();
                        resetIncome();


                        sDialog.dismissWithAnimation();

                        Toast.makeText(getApplicationContext(),"Successfully Cleaned All Saved Records From DataBase",Toast.LENGTH_SHORT).show();

                    }
                })
                .show();

    }

    public void resetIncome(){
        Storage storage=new Storage(this);

        storage.saveData("0");
    }


    private void signIn() {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            mDriceResourceClient=Drive.getDriveResourceClient(this,signInAccount);
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }



    private void createFile() {
        // [START create_file]
        rootFolderTask=null;
        try {
            rootFolderTask= mDriceResourceClient.getRootFolder();

        }catch (NullPointerException np){
            Toast.makeText(getApplicationContext(), "Unable to create file", Toast.LENGTH_SHORT).show();
        }
        createContentsTask = mDriceResourceClient.createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception {
                        DriveFolder parent = rootFolderTask.getResult();
                        DriveContents contents = createContentsTask.getResult();

                        //writting data into DriveStream


                        String dbPath=getApplicationContext().getDatabasePath("Count_on_it").getPath();

                        FileInputStream fis=new FileInputStream(dbPath);
                        BufferedInputStream bis=new BufferedInputStream(fis);

                        byte[] buffer = new byte[8 * 1024];

                        BufferedOutputStream bos=new BufferedOutputStream(contents.getOutputStream());

                        int n=0;

                        while((n= bis.read(buffer)) > 0){
                            bos.write(buffer, 0, n);
                        }

                        bis.close();

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("Count_on_it.db")
                                .setMimeType("application/x-sqlite3")
                                .setStarred(true)
                                .build();

                        return mDriceResourceClient.createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                //showMessage(getString(R.string.file_created,
                                app_preferences=getSharedPreferences("DriveId",MODE_PRIVATE);
                                editor=app_preferences.edit();
                                Toast.makeText(getApplicationContext(),"Suucessfully created file",Toast.LENGTH_SHORT).show();
                                driveID=driveFile.getDriveId().encodeToString();

                                editor.putString("driveid",driveID);
                                editor.commit();
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        Toast.makeText(getApplicationContext(),"Unable to create file",Toast.LENGTH_SHORT).show();
                        //showMessage(getString(R.string.file_create_error));
                        finish();
                    }
                });
        // [END create_file]
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }



    private void retrieveContents(DriveFile file) {
        // [START open_file]
        Task<DriveContents> openFileTask =
                mDriceResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);
        // [END open_file]
        // [START read_contents]
        openFileTask
                .continueWithTask(new Continuation<DriveContents, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                        DriveContents contents = task.getResult();

                        InputStream inputStream=null;
                        try {
                            inputStream = contents.getInputStream();
                            OutputStream outputStream = new FileOutputStream(new File(getApplicationContext().getDatabasePath("Count_on_it").getPath()));


                            try {
                                byte[] buffer = new byte[8 * 1024]; // or other buffer size
                                int read;

                                while ((read = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, read);
                                }
                                outputStream.flush();
                            } finally {
                                outputStream.close();
                            }


                        }catch (FileNotFoundException fe){
                            fe.printStackTrace();
                        }finally {
                            try {
                                if (inputStream != null)
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        /*try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(contents.getInputStream()))) {
                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line).append("\n");
                            }
                        }*/

                        Task<Void> discardTask = mDriceResourceClient.discardContents(contents);
                        // [END discard_contents]
                        return discardTask;
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Database Successfully Restored",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        // [START_EXCLUDE]
                        Log.e(TAG, "Unable to read contents", e);
                        Toast.makeText(getApplicationContext(),"Unable to read contents",Toast.LENGTH_LONG).show();
                        finish();
                        // [END_EXCLUDE]
                    }
                });
        // [END read_contents]
    }




}

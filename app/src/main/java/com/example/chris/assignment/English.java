package com.example.chris.assignment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;



public class English extends ActionBarActivity {
    private static  String TITLE_ENG ;
    private static  String NOTES_ENG;
    private static String TITLE_FRA;
    private static String NOTES_FRA;
    private static String ZIP_ENG;
    private static String ZIP_FRA;
    private static final String UNZIP_ENG = Environment.getExternalStorageDirectory() + "/Download/unzipped_eng/";
    private static final String ZIP_FILE_ENG = Environment.getExternalStorageDirectory() +"/Download/633_light_duty_vehicle_fuel_efficiency_improvement_1990_2010_human_activities.zip";
    private static final String MAP_ENG = UNZIP_ENG + "light_duty_vehicle_fuel_efficiency_improvement_1990_2010_human_activities_map.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english);
        // get all the data from main or French activity
        Intent intent = getIntent();
        TITLE_ENG = intent.getStringExtra("TITLE_ENG");
        NOTES_ENG = intent.getStringExtra("NOTES_ENG");
        TITLE_FRA = intent.getStringExtra("TITLE_FRA");
        NOTES_FRA = intent.getStringExtra("NOTES_FRA");
        ZIP_ENG = intent.getStringExtra("ZIP_ENG");
        ZIP_FRA = intent.getStringExtra("ZIP_FRA");
        TextView title = (TextView)findViewById(R.id.title_eng);
        TextView notes = (TextView)findViewById(R.id.notes_eng);
        title.setText(TITLE_ENG);
        notes.setText(NOTES_ENG);
        //enable to scroll the long text which is note
        notes.setMovementMethod(new ScrollingMovementMethod());
        // pop-up dialog - step of use
        Help();


    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_english, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            // when users choose another language, pop-up the dialog
           if (id == R.id.action_fra) {
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setTitle(R.string.pressed_title);
               builder.setMessage(R.string.change_lan_message);
               builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //once users click yes, it passes title and note in both language
                       Intent intent = new Intent(English.this, French.class);
                       intent.putExtra("TITLE_FRA", TITLE_FRA);
                       intent.putExtra("NOTES_FRA", NOTES_FRA);
                       intent.putExtra("TITLE_ENG", TITLE_ENG);
                       intent.putExtra("NOTES_ENG", NOTES_ENG);
                       intent.putExtra("ZIP_ENG", ZIP_ENG);
                       intent.putExtra("ZIP_FRA", ZIP_FRA);
                       startActivity(intent);
                       finish();// destroy activity, but users can select language on menu
                   }
               });
               builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        //do nothing if users click no
                   }
               });
               builder.show();

            }
            if(id == R.id.help){
                Help();// when users click on the menu, it pops-up the step of use
            }
            return super.onOptionsItemSelected(item);
        }
    // once users click Download button it downloads via website
    public void Download (View v) {
        Uri uri = Uri.parse(ZIP_ENG);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    // in order to unzip ,users must have downloaded the zip file.
    // if they have downloaded, it unzips in the same path and create unzipped folder
    // if they have already unzipped, gives an message
    public void Unzip (View v) {

        File unzip = new File(UNZIP_ENG);
        File zip = new File(ZIP_FILE_ENG);
        if (zip.exists()) {
            if (!unzip.isDirectory()) {
                unZip d = new unZip(ZIP_FILE_ENG, UNZIP_ENG);
                d.unzip();
            } else {
                Toast.makeText(this, "You have already unzipped", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Download Zip File please",Toast.LENGTH_SHORT).show();
        }
    }
    //users must have unzipped file in unzipped folder
    //it finds file and open with jpg type
    public void Map(View v) {
        File file = new File(MAP_ENG);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);

           intent.setDataAndType(path, "image/jpg");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

           try {startActivity(intent);}catch (ActivityNotFoundException e) {
                Toast.makeText(English.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Download or Un-Zip please",Toast.LENGTH_SHORT).show();
        }
    }
    //because the main activity is destroyed, when users click back button, it will exit the app
    //this function give warning for exiting the app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pressed_title);
            builder.setMessage(R.string.back_pressed_message);
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //pop-up step of use
    public void Help(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How to Use it ?");
        builder.setMessage(getString(R.string.step1)+"\n"+getString(R.string.step2)+"\n"+getString(R.string.step3));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}

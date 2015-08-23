package com.example.chris.assignment;

import android.app.AlertDialog;
;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//References
//json data : http://open.canada.ca/data/api/action/package_show?id=d8a42140-8893-11e0-a9c8-6cf049291510
//http://www.androidhive.info/2012/01/android-json-parsing-tutorial/ - for read json
//http://stackoverflow.com/questions/3450839/blinking-text-in-android-view - for blinking text
//http://stackoverflow.com/questions/2115758/how-to-display-alert-dialog-in-android - for alertDialog
//http://www.jondev.net/articles/Unzipping_Files_with_Android_%28Programmatically%29 - unzip

public class MainActivity extends ActionBarActivity {

    private static String url = "http://open.canada.ca/data/api/action/package_show?id=d8a42140-8893-11e0-a9c8-6cf049291510";
    private static final String RESULT = "result";
    private static final String RESOURCES = "resources";
    private static final String TITLE_FRA = "title_fra";
    private static final String NOTES_FRA = "notes_fra";
    private static final String TITLE = "title";
    private static final String NOTES = "notes";
    private static final String URL_FRA = "url_fra";// lisence
    private static final String ZIP_ENG = "url";
    private static final String ZIP_FRA="url";
    private static Animation anim = null;
    private static TextView blinking;
    private static String title_fra;
    private static String notes_fra;
    private static String title_eng;
    private static String notes_eng;
    private static String zip_eng;
    private static String zip_fra;
    JSONArray resources = null;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetContacts().execute();

        context = this;

        blinking= (TextView) findViewById(R.id.textView);
        // animation that is blinking text in main page
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        blinking.startAnimation(anim);


        RelativeLayout start_screen = (RelativeLayout) findViewById(R.id.main_layout);

        start_screen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //once user click the screen it stop
                blinking.getAnimation().cancel();

                new AlertDialog.Builder(context)
                        //.setTitle("Language")
                        .setMessage("Language")
                        .setNegativeButton(R.string.English, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //when user click the English, it passes title , note and download link.
                                //in order to pass french data when user selects in the English activity, also passes french data
                                Intent intent = new Intent(MainActivity.this, English.class);
                                intent.putExtra("TITLE_ENG", title_eng);
                                intent.putExtra("NOTES_ENG", notes_eng);
                                intent.putExtra("TITLE_FRA", title_fra);
                                intent.putExtra("NOTES_FRA", notes_fra);
                                intent.putExtra("ZIP_ENG", zip_eng);
                                intent.putExtra("ZIP_FRA", zip_fra);
                                startActivity(intent);
                                finish(); //once users select the language, they are not able to go back to main because the main activity is destroyed. However, they can choose language on menu
                            }
                        })
                        .setPositiveButton(R.string.French, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //same as English
                                Intent intent = new Intent(MainActivity.this, French.class);
                                intent.putExtra("TITLE_FRA", title_fra);
                                intent.putExtra("NOTES_FRA", notes_fra);
                                intent.putExtra("TITLE_ENG", title_eng);
                                intent.putExtra("NOTES_ENG", notes_eng);
                                intent.putExtra("ZIP_ENG", zip_eng);
                                intent.putExtra("ZIP_FRA", zip_fra);
                                startActivity(intent);
                                finish();// same as English
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .show();

            }
        });


    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.cancel();

        }*/


        //thread reads information that i use in this project in json format
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

           // Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                       JSONObject result = jsonObj.getJSONObject(RESULT);
                        title_fra = result.getString(TITLE_FRA);
                        notes_fra = result.getString(NOTES_FRA);
                        title_eng = result.getString(TITLE);
                        notes_eng = result.getString(NOTES);
                        url = result.getString(URL_FRA);
                        resources = result.getJSONArray(RESOURCES);
                        JSONObject eng_url = resources.getJSONObject(1);
                        JSONObject fra_url = resources.getJSONObject(3);
                        zip_eng = eng_url.getString(ZIP_ENG);
                        zip_fra = fra_url.getString(ZIP_FRA);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }
    }
}

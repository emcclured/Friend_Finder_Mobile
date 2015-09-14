// Name: Drea McClure
// Class: CS496
// Due Date: 14 Aug 2015
// Description:  This is the Main Screen for the Friend Finder Android App
//
// The Following Tutorials and References were used in the development of this App:
//
// http://androidexample.com/Restful_Webservice_Call_And_Get_And_Parse_JSON_Data-_Android_Example/index.php?view=article_discription&aid=101&aaid=123
// http://www.tutorialspoint.com/android/android_google_maps.htm
// http://www.vogella.com/tutorials/AndroidGoogleMaps/article.html#maps_device
// http://start-jandroid.blogspot.com/2011/01/android-multiple-screen-example.html
// http://developer.android.com/samples/BorderlessButtons/index.html
// https://developers.google.com/maps/documentation/android/start

package com.example.android.friendfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainFriendFinderActivity extends Activity {
    String user_login_id = null;
    String user_name = null;

    AddFriendActivity ica = new AddFriendActivity();

    DisplayMapActivity dm = new DisplayMapActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_finder_main);

        // grab the passed in user_login_id

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_login_id = extras.getString("user_login_id");
            user_name = extras.getString("user_name");
        }

        Button anfb = (Button) findViewById(R.id.AddNewFriend);
        anfb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // here intent_cb call new screen;
                Intent intent_cb = new Intent(MainFriendFinderActivity.this, AddFriendActivity.class);

                // pass the user id to AddFriendActivity

                intent_cb.putExtra("user_login_id", user_login_id);
                intent_cb.putExtra("user_name", user_name);

                startActivity(intent_cb);
            }
        });

        Button udcb = (Button) findViewById(R.id.UpdateDeleteFriendButton);
        udcb.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0){
                    // here intent_cb call new screen;
                    Intent intent_udcb = new Intent(MainFriendFinderActivity.this, UpdateDeleteFriendActivity.class);

                    // pass the user id to UpdateDeleteFriendActivity

                    intent_udcb.putExtra("user_login_id", user_login_id);
                    intent_udcb.putExtra("user_name", user_name);

                    startActivity(intent_udcb);
                }
        });

        Button dcb = (Button) findViewById(R.id.DisplayFriends);
        dcb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // here intent_dcb call new screen;
               Intent intent_dcb = new Intent(MainFriendFinderActivity.this, DisplayMapActivity.class);

                // pass the user id to DisplayMapActivity

                intent_dcb.putExtra("user_login_id", user_login_id);
                intent_dcb.putExtra("user_name", user_name);

               startActivity(intent_dcb);
            }
        });


        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button GetServerData = (Button) findViewById(R.id.GetFriends);

        GetServerData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // WebServer Request URL
                String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friends/list";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);
            }
        });

    }

    /////////////////////////////////////////////////////////
    // private internal classes for use with activity
    /////////////////////////////////////////////////////////

    // Class with extends AsyncTask class

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainFriendFinderActivity.this);
        String data ="";

        TextView jsonParsed = (TextView) findViewById(R.id.OutputText);

        int sizeData = 0;
        // EditText serverText = (EditText) findViewById(R.id.OutputText);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/

            BufferedReader reader=null;

            // Send data

            try
            {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/

            return null;
        }

        protected void onPostExecute(Void unused) {
            // Close progress dialog
            Dialog.dismiss();

            //System.out.println(Content);

            jsonParsed.setMovementMethod(new ScrollingMovementMethod());

            if (Error != null) {

                jsonParsed.setText("Output : " + Error);

            } else {

                // Show Response Json On Screen (activity)

                jsonParsed.setText(Content);

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;

                try {

                    // Creates a new JSONObject with name/value mappings from the JSON string.

                    jsonResponse = new JSONObject(Content);

                    // Returns the value mapped by name if it exists and is a JSONArray.
                    // Returns null if not.

                    JSONArray jsonMainNode = jsonResponse.optJSONArray("items");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        //Get Object for each JSON node.

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        // Fetch node values

                        String phone_number = jsonChildNode.optString("phone_number").toString();
                        String user_id      = jsonChildNode.optString("user_id").toString();
                        String name         = jsonChildNode.optString("name").toString();
                        String email        = jsonChildNode.optString("email").toString();
                        String signup_time  = jsonChildNode.optString("signup_time").toString();
                        String address      = jsonChildNode.optString("address").toString();
                        String id           = jsonChildNode.optString("id").toString();
                        String kind         = jsonChildNode.optString("kind").toString();

                        // only want the friends of the user who is logged in

                        if (user_id.equals(user_login_id)) {
                            OutputData += "Id          :" + id + "\n"
                                    + "Email       : " + email + "\n"
                                    + "Name        : " + name + "\n"
                                    + "Address     : " + address + "\n"
                                    + "Phone Number: " + phone_number + "\n"
                                    + "Signup Time : " + signup_time + "\n"
                                    + "--------------------------------------------------" + "\n";
                        }

                    }
                    /****************** End Parse Response JSON Data *************/

                    // Show Parsed Output on screen (activity)

                    jsonParsed.setText(OutputData);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }

    }
}

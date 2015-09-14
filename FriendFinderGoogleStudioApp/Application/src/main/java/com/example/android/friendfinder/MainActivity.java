// Name: Drea McClure
// Class: CS496
// Due Date: 14 Aug 2015
// Description:  This Login Screen for the Friend Finder Android App
//
// The Following Tutorials and References were used in the development of this App:
//
// http://androidexample.com/Restful_Webservice_Call_And_Get_And_Parse_JSON_Data-_Android_Example/index.php?view=article_discription&aid=101&aaid=123
// http://www.tutorialspoint.com/android/android_google_maps.htm
// http://www.vogella.com/tutorials/AndroidGoogleMaps/article.html#maps_device
// http://start-jandroid.blogspot.com/2011/01/android-multiple-screen-example.html
// http://developer.android.com/samples/BorderlessButtons/index.html
// https://developers.google.com/maps/documentation/android/start
// http://stackoverflow.com/questions/1051004/how-to-send-put-delete-http-request-in-httpurlconnection

package com.example.android.friendfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {
    MainFriendFinderActivity mfa = new MainFriendFinderActivity();

    AddUserActivity aua = new AddUserActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        Button lacb = (Button) findViewById(R.id.loginButton);

        lacb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // call web services API to login

                // WebServer Request URL
                String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/login/login";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);

            }

        });

        Button eacb = (Button) findViewById(R.id.exitAppButton);

        eacb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // exit application

                finish();
            }

        });

        Button aub = (Button) findViewById(R.id.createNewUserButton);

        aub.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                    // here intent_cb call new screen;
                    Intent intent_aucb = new Intent(MainActivity.this, AddUserActivity.class);
                    startActivity(intent_aucb);
            }

        });

    }

    /////////////////////////////////////////////////////////
    // private internal classes for use with activity
    /////////////////////////////////////////////////////////

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        String user_cname = null;
        String user_cpassword = null;
        String user_login_id = null;
        String user_logged_in = null;

        String outData = "";

        int sizeData = 0;

        // set up put parameters

        EditText loginNameText = (EditText) findViewById(R.id.loginNameEditText);
        EditText loginPasswordText = (EditText) findViewById(R.id.loginPasswordEditText);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

            // Set the JSON Request parameters

            outData += "{" + "\"" + "id" + "\"" + ":" + "\"" + "1" + "\"" + ",";
            outData += "\"" + "user_cname" + "\"" + ":" + "\"" + loginNameText.getText() + "\"" + ",";
            outData += "\"" + "user_cpassword" + "\"" + ":" + "\"" + loginPasswordText.getText() + "\"" + "}";

            //System.out.println(outData);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/

            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                //byte[] putData = outData.getBytes(Charset.forName("UTF-8"));
                //int putDataLength = putData.length;

                String putData = outData;

                conn.setDoOutput(true);

                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                //conn.setRequestProperty("charset", "UTF-8");
                //conn.setRequestProperty("Content-Length", Integer.toString(putDataLength));
                //conn.setUseCaches(false);

                // DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(putData);
                wr.flush();
                wr.close();

                //System.out.println(url);
                //System.out.println(conn.getResponseCode());
                //System.out.println(conn.getResponseMessage());

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String

                Content = sb.toString();

                //System.out.println(Content);
            } catch (Exception ex) {
                Error = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            /*****************************************************/

            return null;
        }

        protected void onPostExecute(Void unused) {
            // Close progress dialog
            Dialog.dismiss();

            // System.out.println("Content = " + Content);

            if (Content!=null) {
                try {
                    JSONObject jsonObj = new JSONObject(Content);

                    /*********** Process JSON Node ************/

                    // Fetch node values

                    user_cname = jsonObj.optString("user_cname").toString();
                    user_cpassword = jsonObj.optString("user_cpassword").toString();
                    user_login_id = jsonObj.optString("user_login_id").toString();
                    user_logged_in = jsonObj.optString("user_logged_in").toString();

                    // System.out.println(user_login_id);

                    /****************** End Parse Response JSON Data *************/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (Error != null) {
                // do error

                System.out.println("HERE!");

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Login Error");
                alert.setMessage("The User and/or Password does not match!");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                      // do nothing will dismiss on click
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do nothing will dismiss on click
                            }
                        });

                alert.show();

            } else {
                // no error so goto next screen

                // here intent_cb call new screen;


                Intent intent_cb = new Intent(MainActivity.this, MainFriendFinderActivity.class);

                // pass the user id to MainFriendFinderActivity

                intent_cb.putExtra("user_login_id", user_login_id);
                intent_cb.putExtra("user_name", user_cname);

                startActivity(intent_cb);

                // exit out of activity
                finish();
            }
        }
    }

}

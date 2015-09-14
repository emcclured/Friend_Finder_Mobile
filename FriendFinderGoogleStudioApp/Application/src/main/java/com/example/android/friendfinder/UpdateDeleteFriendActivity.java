// Name: Drea McClure
// Class: CS496
// Due Date: 14 Aug 2015
// Description:  This is the Add Friend Screen for the Friend Finder Android App
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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class UpdateDeleteFriendActivity extends Activity {
    String user_login_id = null;
    String user_name = null;

    MainFriendFinderActivity ma;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_friend);

        // grab the passed in user_login_id

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_login_id = extras.getString("user_login_id");
            user_name = extras.getString("user_name");
        }

        Button tmb = (Button) findViewById(R.id.uToMain);
        tmb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        Button updateb = (Button) findViewById(R.id.ubutton);
        updateb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // WebServer Request URL
                String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/update";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation1().execute(serverURL);
            }
        });

        Button deleteb = (Button) findViewById(R.id.uDeleteFriend);
        deleteb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // WebServer Request URL
                String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/delete";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation2().execute(serverURL);
            }
        });

    }

    ///////////////////////////////////////////////////////////////////
    // private internal classes for use with input customer activity
    ///////////////////////////////////////////////////////////////////

    // Class with extends AsyncTask class

    private class LongOperation1  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(UpdateDeleteFriendActivity.this);
        String outData = "";

        TextView uFriendResultText = (TextView) findViewById(R.id.uFriendResultText);

        int sizeData = 0;

        // set up read parameters

        EditText idText = (EditText) findViewById(R.id.uidText);
        EditText nameText = (EditText) findViewById(R.id.uNameEditText);
        EditText emailText = (EditText) findViewById(R.id.uEmailEditText);
        EditText addressText = (EditText) findViewById(R.id.uAddressEditText);
        EditText phoneText = (EditText) findViewById(R.id.uPhoneEditText);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

            // Set the JSON Request parameters

            outData += "{\"" + "id" + "\"" + ":" + "\"" + idText.getText() + "\""+ ",";
            outData += "\"" + "email"+ "\"" +":" + "\"" + emailText.getText() + "\"" + ",";
            outData += "\"" + "name"+ "\"" +":" + "\"" + nameText.getText() + "\"" + ",";
            outData += "\"" + "address"+  "\"" + ":" + "\"" + addressText.getText() + "\"" + ",";
            outData += "\"" + "phone_number" + "\"" + ":" + "\"" + phoneText.getText() + "\""+ ",";
            outData += "\"" + "user_id" + "\"" + ":" + "\"" + user_login_id + "\""+ "}";

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            //System.out.println(outData);

            /************ Make Post Call To Web Server ***********/

            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                //byte[] postData       = outData.getBytes(Charset.forName("UTF-8"));
                //int    postDataLength = postData.length;

                String putData = outData;

                conn.setDoOutput(true);

                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                //conn.setRequestProperty( "charset", "UTF-8");
                //conn.setRequestProperty("Content-Length", Integer.toString( postDataLength ));
                //conn.setUseCaches(false);

                //DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(putData);
                wr.flush();
                wr.close();

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
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            uFriendResultText.setMovementMethod(new ScrollingMovementMethod());

            if (Error != null) {

                uFriendResultText.setText("Output : " + Error);

            } else {

                // Show Response Json On Screen (activity)

                uFriendResultText.setText(Content);

            }
        }
    }

    ///////////////////////////////////////////////////////////////////
    // private internal classes for use with input customer activity
    ///////////////////////////////////////////////////////////////////

    // Class with extends AsyncTask class

    private class LongOperation2  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(UpdateDeleteFriendActivity.this);
        String outData = "";

        TextView uFriendResultText = (TextView) findViewById(R.id.uFriendResultText);

        int sizeData = 0;

        // set up read parameters

        EditText userIdText = (EditText) findViewById(R.id.uidText);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

            // Set the JSON Request parameters

            outData += userIdText.getText();

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            //System.out.println(outData);

            /************ Make Post Call To Web Server ***********/

            BufferedReader reader = null;

            // Send data
            try {

                String deleteUrl = urls[0] + "/" + outData;

                // Defined URL  where to send data

                URL url = new URL(deleteUrl);

                // Send POST data request

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.connect();

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
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            uFriendResultText.setMovementMethod(new ScrollingMovementMethod());

            if (Error != null) {

                uFriendResultText.setText("Output : " + Error);

            } else {

                // Show Response Json On Screen (activity)

                uFriendResultText.setText(Content);

            }
        }
    }
}

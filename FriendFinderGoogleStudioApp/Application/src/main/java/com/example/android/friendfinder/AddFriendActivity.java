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
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class AddFriendActivity extends Activity {
    String user_login_id = null;
    String user_name = null;

    MainFriendFinderActivity ma;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        // grab the passed in user_login_id

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_login_id = extras.getString("user_login_id");
            user_name = extras.getString("user_name");
        }

        Button tmb = (Button) findViewById(R.id.ToMain);
        tmb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });


        Button acb = (Button) findViewById(R.id.addFriendButton);
        acb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // WebServer Request URL
                String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/insert";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);
            }
        });
    }

    public void setMa( MainFriendFinderActivity mA){
        this.ma=mA;
    }

    ///////////////////////////////////////////////////////////////////
    // private internal classes for use with input customer activity
    ///////////////////////////////////////////////////////////////////

    // Class with extends AsyncTask class

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(AddFriendActivity.this);
        String outData = "";

        TextView friendResultText = (TextView) findViewById(R.id.FriendResultText);

        int sizeData = 0;

        // set up read parameters

        EditText idText = (EditText) findViewById(R.id.newFriendIdText);
        EditText nameText = (EditText) findViewById(R.id.NameEditText);
        EditText emailText = (EditText) findViewById(R.id.EmailEditText);
        EditText addressText = (EditText) findViewById(R.id.AddressEditText);
        EditText phoneText = (EditText) findViewById(R.id.PhoneEditText);

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

            System.out.println(outData);

            /************ Make Post Call To Web Server ***********/

            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                byte[] postData       = outData.getBytes(Charset.forName("UTF-8"));
                int    postDataLength = postData.length;

                conn.setDoOutput( true );

                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/json");
                conn.setRequestProperty( "charset", "UTF-8");
                conn.setRequestProperty("Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
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

            friendResultText.setMovementMethod(new ScrollingMovementMethod());

            if (Error != null) {

                friendResultText.setText("Output : " + Error);

            } else {

                // Show Response Json On Screen (activity)

                friendResultText.setText(Content);

            }
        }
    }
}

// Name: Drea McClure
// Class: CS496
// Due Date: 14 Aug 2015
// Description:  This is the Google Map Display functionality for the Friend Finder Android App
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
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DisplayMapActivity extends Activity {
    String user_login_id = null;
    String user_name = null;

    MainFriendFinderActivity ma;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_map);

        // grab the passed in user_login_id

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_login_id = extras.getString("user_login_id");
            user_name = extras.getString("user_name");
            // System.out.println(user_login_id);
        }

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            // Set the Video Store Location

            LatLng VideoStorePoint = new LatLng(47.608013 , -122.335167);

            Marker TP = googleMap.addMarker(new MarkerOptions().position(VideoStorePoint).title(user_name));

            // Move the camera to Video Store with a zoom of 10

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VideoStorePoint, 10));

            // Now go get the customers

            // WebServer Request URL
            String serverURL = "https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friends/list";

            // Use AsyncTask execute Method To Prevent ANR Problem
            new LongOperation().execute(serverURL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Button btmb = (Button) findViewById(R.id.BackToMain);
        btmb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    public void setMa( MainFriendFinderActivity mA){
        this.ma=mA;
    }

    ///////////////////////////////////////////////////////////////
    // private internal classes for use with display map activity
    ///////////////////////////////////////////////////////////////

    // Class with extends AsyncTask class

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(DisplayMapActivity.this);
        String data ="";

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
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            //System.out.println(Content);

            if (Error != null) {

                // don't do anything else if error

            } else {

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;

                try {

                    // Creates a new JSONObject with name/value mappings from the JSON string.

                    jsonResponse = new JSONObject(Content);

                    // Returns the value mapped by name if it exists and is a JSONArray.
                    // Returns null if not.

                    // JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

                    JSONArray jsonMainNode = jsonResponse.optJSONArray("items");

                    // Process each JSON Node

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String phone_number = jsonChildNode.optString("phone_number").toString();
                        String user_id      = jsonChildNode.optString("user_id").toString();
                        String name         = jsonChildNode.optString("name").toString();
                        String email        = jsonChildNode.optString("email").toString();
                        String signup_time  = jsonChildNode.optString("signup_time").toString();
                        String address      = jsonChildNode.optString("address").toString();
                        String id           = jsonChildNode.optString("id").toString();
                        String kind         = jsonChildNode.optString("kind").toString();

                        String[] addressResults = address.split(" ");

                        // only plot the logged in user friends

                        if (user_id.equals(user_login_id)) {
                            // try to extract out lat lon from address
                            if (addressResults.length == 2) {
                                // see if the two results are numbers
                                try {
                                    Double lat = Double.parseDouble(addressResults[0]);
                                    Double lon = Double.parseDouble(addressResults[1]);

                                    // only try to put on map if valid lat lon
                                    if (lat <= 90.0 && lat >= -90.0 && lon <= 180.0 && lon >= -180.0) {
                                        LatLng CustomerPoint = new LatLng(lat, lon);

                                        // Creating a new marpker and put it on map

                                        googleMap.addMarker(new MarkerOptions().position(CustomerPoint).title(name));
                                    }
                                } catch (Exception e) {
                                    // don't do anything if you can't get lat lon
                                }
                            }
                        }

                    }
                    /****************** End Parse Response JSON Data *************/

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }

}

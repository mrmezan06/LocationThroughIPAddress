package com.mezan.location;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private TextView IPField;
    private TextView LatLonField;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IPField = findViewById(R.id.TextView02);
        LatLonField = findViewById(R.id.TextView04);

        /*This api provide Ip address of internet real ip*/
        //https://api.ipify.org/?format=json
        /*This api give result of lat lon from ip address*/
        /*My Key is a441e249-af1d-4ae0-b08c-9bf7ad9432d8 */
        //https://api.ipfind.com/?ip=103.80.70.149&auth=API_KEY

        new IPFINDER().execute("https://api.ipify.org/?format=json");


    }

    private class IPFINDER extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String result) {
            // Res.setText(result);
            /*Response
            * {"ip":"103.80.70.149"}
            * */
            try {
                JSONObject object=new JSONObject(result);
               String ip= object.getString("ip");
                Log.d("IP",ip);
                IPField.setText(ip);
                /*Location finding */
                new IPTOLATLONFINDER().execute("https://api.ipfind.com/?ip="+ip+"&auth=a441e249-af1d-4ae0-b08c-9bf7ad9432d8");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result=new StringBuilder();
            HttpURLConnection urlConnection=null;
            try{
                URL url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=reader.readLine())!=null){
                    result.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }
    }
    private class IPTOLATLONFINDER extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String result) {
            // Res.setText(result);
            /*Response api
            * {"ip_address":"103.80.70.149","country":"Bangladesh","country_code":"BD",
            * "continent":"Asia","continent_code":"AS","city":"Khulna","county":"Khulna","region":"Khulna",
            * "region_code":"82","timezone":"Asia\/Dhaka","owner":null,
            * "longitude":89.5648,"latitude":22.8174,
            * "currency":"BDT","languages":["bn-BD","en"]}
            * */

            try {
                JSONObject object = new JSONObject(result);
                String city = object.getString("city");
                String lat = object.getString("latitude");
                String lon = object.getString("longitude");

                LatLonField.setText("City :"+city+"\nLatitude :"+lat+"\nLongitude :"+lon);
                Log.d("Location","City :"+city+"\nLatitude :"+lat+"\nLongitude :"+lon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result=new StringBuilder();
            HttpURLConnection urlConnection=null;
            try{
                URL url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=reader.readLine())!=null){
                    result.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }
    }
}

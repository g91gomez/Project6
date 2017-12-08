package com.example.g91go.whatsfordinner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.g91go.whatsfordinner.MainActivity.EXTRA_MESSAGE;
import static com.example.g91go.whatsfordinner.R.id.url;

public class LocalSearchActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private final String ACCESS = "8VS7RT3JKGxL4O2Z0iUm0LUAHp1OGGu99hWVr71lSckP_hQe0N_pPCBTMc5SHHn957rFRTf-HB1UsXunBFTMuEFYDNrnwp5WuzTi2VpCJThnDySUtBX46d0Usrj3WXYx";
    private String longitude, latitude;
    private ListView lv;
    private LocationManager locationManager;
    private Location loc;
    private String message;
    private String sPreference;


    ArrayList<HashMap<String, String>> businessList;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);




        businessList = new ArrayList<>();
        Intent intent = getIntent();
        lv = (ListView) findViewById(R.id.list);

        // Get the Intent that started this activity and extract the string
        Bundle extras = intent.getExtras();
        this.message = extras.getString(EXTRA_MESSAGE);
        this.sPreference = extras.getString("Sort");



        //String message = intent.getStringExtra(EXTRA_MESSAGE);

        String sCPreference = "";
        TextView Sort = (TextView) findViewById(R.id.textView3);
        if (sPreference.equals("distance")){
            sCPreference = "Distance";
        }
        else if (sPreference.equals("rating")){
            sCPreference = "Rating";
        }
        else if(sPreference.equals("price")){
            sCPreference = "Most Popular/Reviewed";
        }
        String sorted = ("Sorted By: ");

        Sort.setText(sorted + sCPreference);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                String url = ((TextView) view.findViewById(R.id.url)).getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        new YelpBusinessSearch().execute(message);
    }





    public class YelpBusinessSearch extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {


            latitude = Double.toString(loc.getLatitude());
            longitude = Double.toString(loc.getLongitude());
        }
        @Override
        protected Void doInBackground(String... arg0) {


            HTTPHandler sh = new HTTPHandler();
            String  endpoint = "https://api.yelp.com/v3/businesses/search?sort_by=distance&term=" + arg0[0] + "&latitude=" + latitude + "&longitude=" + longitude + "&Authorization=Bearer " + ACCESS;
            // String endpoint = "https://api.yelp.com/v3/businesses/search?term=" + arg0[0] + "&latitude=33.841584&longitude=-118.147974";

            //Toast.makeText(getApplicationContext(), arg0[0], Toast.LENGTH_SHORT).show();
            if(arg0[0].equals("American")){
                arg0[0] = "american (new)+american (traditional)";
            }
            else if (arg0[0].equals("Greek")){
                arg0[0] = "greek";
            }

            if(sPreference.equals("rating")) {

                endpoint = "https://api.yelp.com/v3/businesses/search?sort_by=rating&term=" + arg0[0] + "&latitude=" + latitude + "&longitude=" + longitude + "&Authorization=Bearer " + ACCESS;
                // String endpoint = "https://api.yelp.com/v3/businesses/search?term=" + arg0[0] + "&latitude=33.841584&longitude=-118.147974";
            }
            else if(sPreference.equals("price")){
                endpoint = "https://api.yelp.com/v3/businesses/search?sort_by=review_count&term=" + arg0[0] + "&latitude=" + latitude + "&longitude=" + longitude + "&Authorization=Bearer " + ACCESS;
                // String endpoint = "https://api.yelp.com/v3/businesses/search?term=" + arg0[0] + "&latitude=33.841584&longitude=-118.147974";

            }
            else{
                endpoint = "https://api.yelp.com/v3/businesses/search?sort_by=distance&term=" + arg0[0] + "&latitude=" + latitude + "&longitude=" + longitude + "&Authorization=Bearer " + ACCESS;
                // String endpoint = "https://api.yelp.com/v3/businesses/search?term=" + arg0[0] + "&latitude=33.841584&longitude=-118.147974";
            }

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(endpoint);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray businesses = jsonObj.getJSONArray("businesses");

                    // looping through All businesses
                    for (int i = 0; i < businesses.length(); i++) {
                        JSONObject b = businesses.getJSONObject(i);
                        double metersDistance = Double.parseDouble(b.getString("distance"));
                        double milesDistance = metersDistance / 1609.344;
                        double roundedMilesDistance = (double) Math.round(milesDistance *100) / 100;

                        String name =  b.getString("name");
                        String rating =  b.getString("rating") + " Stars";
                        String url =  b.getString("url");
                        String image_url = b.getString("image_url");
                        String phone = "Phone Number: " + b.getString("phone");
                        String distance = "Distance: " + Double.toString(roundedMilesDistance) + " Miles Away";

                        JSONObject l= b.getJSONObject("location");
                        String address1 = l.getString("address1");
                        String zipCode = l.getString("zip_code");

                        HashMap<String, String> business = new HashMap<>();

                        business.put("name", name);
                        business.put("rating", rating);
                        business.put("phone",phone);
                        business.put("url", url);
                        business.put("image_url", image_url);
                        business.put("distance", distance);
                        business.put("address1", address1);
                        business.put("zip_code", zipCode);

                       // addUrls(business.get("url"));

                        businessList.add(business);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            ListAdapter adapter = new MyAdapter(
                    LocalSearchActivity.this, businessList,
                    R.layout.list_item, new String[]{"name", "rating",
                    "url", "address1", "zip_code","distance", "image_url"}, new int[]{R.id.name,
                    R.id.rating, url, R.id.address1, R.id.zip_code, R.id.distance, R.id.img_url});
            lv.setAdapter(adapter);



        }

    }
    public class MyAdapter extends SimpleAdapter {
        private Context context;

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int     resource, String[] from, int[] to){
            super(context, data, resource, from, to);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // here you let SimpleAdapter built the view normally.
            View v = super.getView(position, convertView, parent);

            // Then we get reference for Picasso
            ImageView img = (ImageView) v.getTag();
            if(img == null){
                img = (ImageView) v.findViewById(R.id.imageView3);
                v.setTag(img); // <<< THIS LINE !!!!
            }
            // get the url from the data you passed to the `Map`
            String url = (String)((Map)getItem(position)).get("image_url");
            // do Picasso
            // maybe you could do that by using many ways to start
            if(!url.isEmpty()) {


                Picasso.with(context).load(url)
                        .resize(500, 500).into(img);

            }
            // return the view
            return v;
        }
    }
}



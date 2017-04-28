package qr.sample.com.makeitsoon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,com.google.android.gms.location.LocationListener  {
    ArrayList<mobAboutMetaData> list;
    RecyclerView listView;
    private static final int PERMISSION_Location = 1;

    public double latytude;
    public double longitude;
    static final Integer LOCATION = 200;
    static final Integer phone = 400;
    LocationRequest mLocationRequest;
    Location locationManager;
    GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    boolean mStopHandler = false;
    long lll = 5000;
    JSONObject jsonObject = null;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 4000; /* 2 sec */
     String adress;
    ImageView map;
    String deviceid;
    TextView checkid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        listView = (RecyclerView) findViewById(R.id.listid);
        checkid=(TextView) findViewById(R.id.checkid);
      // askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
       // askForPermission(Manifest.permission.READ_PHONE_STATE, phone);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        //mGoogleApiClient.connect();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_Location);
        }
        map=(ImageView) findViewById(R.id.map);
        list = new ArrayList<mobAboutMetaData>();
        final Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        checkid.setText(dateFormat.format(date));
// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @SuppressLint("ServiceCast")
            public void run() {

                try {
                   if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                  //  mGoogleApiClient.connect();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startLocationUpdates();
                        }
                    });

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                      deviceid = telephonyManager.getDeviceId();
                    if ((latytude != 00) && (longitude != 00)) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                          adress=getCompleteAddressString(latytude,longitude);
                        jsonObject = new JSONObject();
                        jsonObject.put("DeviceId", deviceid);
                        jsonObject.put("Latitude", latytude);
                        jsonObject.put("Longitude", longitude);
                        jsonObject.put("City", adress);
                        jsonObject.put("State", adress);
                        jsonObject.put("pincode", adress);
                        jsonObject.put("DistanceInMeter", "0.0");

                        //String value={"DeviceId":" hhghjgsds ","Latitude":" 78 ","Longitude":" 56 ","City":" gurgaon ","State":" haryana ","pincode":" 122001 "\}";
                        //String values = jsonObject.toString();
                        // String url = "http://demo.janayitri.com/AddLocation";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                String values = jsonObject.toString();
                                String url = "http://demo.janayitri.com/AddLocation";
                                new SenddataAsync(MainActivity.this, url, values).execute();
                           }
                                });

                        Thread.sleep(20000);
                    }
                    mGoogleApiClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 0, 2, TimeUnit.SECONDS);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(MainActivity.this,MapsActivity.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("list",list);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile_edit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.e("Connected failed", String.valueOf(mGoogleApiClient.isConnected()));
    }
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1 * 1000);
        mLocationRequest.setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result;
        //LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, request);

        ////Edit by rajeev for get current location 27-06-2016/////////
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
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
        locationManager = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (locationManager != null) {
            latytude = locationManager.getLatitude();
            longitude = locationManager.getLongitude();

        }

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
        //startLocationUpdates();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(MainActivity.this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
               // mGoogleApiClient.connect();
                break;
            /*case 400:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // All good!
                        mGoogleApiClient.disconnect();
                    } else {
                        Toast.makeText(MainActivity.this, "Need your location!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }*/
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    protected void onStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mGoogleApiClient.connect();
            startLocationUpdates();
        } else {
            mGoogleApiClient.connect();
            startLocationUpdates();
        }
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(MainActivity.this,"changeed the location",Toast.LENGTH_LONG).show();
        latytude = location.getLatitude();
        longitude = location.getLongitude();

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
            //mGoogleApiClient.connect();
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            //mGoogleApiClient.connect();
        }
    }


    class LocationService extends Service {
        public static final String BROADCAST_ACTION = "Hello World";
        private static final int TWO_MINUTES = 1000 * 60 * 2;
        public LocationManager locationManager;
        public MyLocationListener listener;
        public Location previousBestLocation = null;

        Intent intent;
        int counter = 0;

        @Override
        public void onCreate() {
            super.onCreate();
            intent = new Intent(BROADCAST_ACTION);
        }

        @Override
        public void onStart(Intent intent, int startId) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            listener = new MyLocationListener();
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        protected boolean isBetterLocation(Location location, Location currentBestLocation) {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true;
            }

            // Check whether the new location fix is newer or older
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;

            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true;
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false;
            }

            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(),
                    currentBestLocation.getProvider());

            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
            return false;
        }


        /**
         * Checks whether two providers are the same
         */
        private boolean isSameProvider(String provider1, String provider2) {
            if (provider1 == null) {
                return provider2 == null;
            }
            return provider1.equals(provider2);
        }


        @Override
        public void onDestroy() {
            // handler.removeCallbacks(sendUpdatesToUI);
            super.onDestroy();
            Log.v("STOP_SERVICE", "DONE");
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
            locationManager.removeUpdates(listener);
        }

        public  Thread performOnBackgroundThread(final Runnable runnable) {
            final Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } finally {

                    }
                }
            };
            t.start();
            return t;
        }


        class MyLocationListener implements LocationListener {

            public void onLocationChanged(final Location loc) {
                Log.i("*******************", "Location changed");
                if (isBetterLocation(loc, previousBestLocation)) {
                    latytude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    intent.putExtra("Latitude", loc.getLatitude());
                    intent.putExtra("Longitude", loc.getLongitude());
                    intent.putExtra("Provider", loc.getProvider());
                    sendBroadcast(intent);

                }
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
            }


            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        try {
            LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e){
                e.printStackTrace();
            }
       if(mGoogleApiClient.isConnected()==true)
       {
          /* if( gps_enabled==true){
               mGoogleApiClient.disconnect();
               mGoogleApiClient.connect();
           }*/

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
       else
        {
        mGoogleApiClient.connect();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("reque", "--->>>>");
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("  loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("  loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(" loction address", "Canont get Address!");
        }
        return strAdd;
    }
    public class SenddataAsync extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;
        private Context context;
        private String url = "";
       // String list;
        String value;

        public SenddataAsync(Context context, String url, String value) {
            this.context = context;
            this.url = url;
            this.value = value;

        }

        @Override
        protected void onPreExecute() {
            try {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                super.onPreExecute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... arg0) {
            rating(url, value);
            return null;
        }

        @SuppressWarnings("static-access")
        private void rating(String url, String value) {
            try {
                mobAboutMetaData data = new mobAboutMetaData();
                data.setLat(""+latytude);
                data.setLon("" + longitude);

                data.setCity(adress);
                data.setState(adress);
                data.setPin(adress);
                data.setDeviceid(deviceid);
                data.setDis("");
                data.setDate("NA");
                Sendbackparser parser = new Sendbackparser();
                list = parser.parseVector(url, value,data);
            }catch (Exception E){
                E.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try{
                progressDialog.dismiss();
            }catch (Exception E){
                E.printStackTrace();
            }

            // "Status":"Success","StatusCode":"0","Message":"Successful Submit Feedback.
            if ((list != null)&&(list.size()>0))
            {
                SharedPreferences prefs = context.getSharedPreferences("productlist", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putString("list", ObjectSerializer.serialize(list));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
                try {
                    AboutsAdapter adapter = new AboutsAdapter(list);
                    listView.setHasFixedSize(true);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    listView.setLayoutManager(mLayoutManager);
                    listView.setAdapter(adapter);
                    JSONObject object = new JSONObject(list.toString());
                    if(object.getString("Status").equalsIgnoreCase("Success")) {
                        //wrrite Your Code here
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }


    }
}
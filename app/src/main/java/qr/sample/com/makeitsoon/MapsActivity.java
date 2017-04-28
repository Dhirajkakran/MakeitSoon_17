package qr.sample.com.makeitsoon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<mobAboutMetaData> list;
    mobAboutMetaData metaData;
    double lat, longe,dis;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try{
              prefs = getSharedPreferences("productlist", Context.MODE_PRIVATE);
            // list = TabFragment.listdlt;//(ArrayList<WarrentyMatadata>)getArguments().getSerializable("list");

        }catch(Exception e)

        {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if(googleMap!=null){
            try {
                googleMap.clear();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
            ScheduledExecutorService scheduler =
                    Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            // call service

                            try {


                                mMap = googleMap;
                                ArrayList<mobAboutMetaData>alll= new ArrayList<mobAboutMetaData>();
                                addMarkers(alll);
                                list = (ArrayList<mobAboutMetaData>) ObjectSerializer.deserialize(prefs.getString("list", ObjectSerializer.serialize(new ArrayList<mobAboutMetaData>())));

                                //mMap.clear();
                                // NetWorkCheck netWorkCheck = new NetWorkCheck();
                                // Add a marker in Sydney and move the camera
                                if ((list != null) && (list.size() > 0)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            addMarkers(list);
                                        }
                                    });

                                }

                                Thread.sleep(5000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 10, TimeUnit.SECONDS);



       /* ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run()
            {
                try {
                    list = (ArrayList<mobAboutMetaData>) ObjectSerializer.deserialize(prefs.getString("list", ObjectSerializer.serialize(new ArrayList<mobAboutMetaData>())));
                    mMap = googleMap;
                    //mMap.clear();
                    // NetWorkCheck netWorkCheck = new NetWorkCheck();
                    // Add a marker in Sydney and move the camera
                    if ((list != null) && (list.size() > 0)) {
                        addMarkers(list);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }, 0, 2, TimeUnit.SECONDS);*/



    }


    private void addMarkers(final List<mobAboutMetaData> list) {
        if (mMap != null) {

            if ((list != null) && (list.size() > 0)) {
                for (int i = 0; i < list.size(); i++) {
                    metaData = list.get(i);
                    try {
                        lat = Double.parseDouble(metaData.getLat());
                        longe = Double.parseDouble(metaData.getLon());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LatLng lt = new LatLng(lat, longe);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String deviceid = telephonyManager.getDeviceId();
                    try {
                      dis=Double.parseDouble(metaData.getDis());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                        if (deviceid.equalsIgnoreCase(metaData.getDeviceid())) {
                            mMap.addMarker(new MarkerOptions().position(lt).snippet("" + i).infoWindowAnchor(5f, 0f)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            ;
                        } else if (dis < 2000.0) {
                            mMap.addMarker(new MarkerOptions().position(lt).snippet("" + i).infoWindowAnchor(5f, 0f));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(lt).snippet("" + i).infoWindowAnchor(5f, 0f)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            ;
                        }

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lt, 11);
                    mMap.animateCamera(cameraUpdate);

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        // Use default InfoWindow frame
                        @Override
                        public View getInfoWindow(Marker arg0) {

                            View v = null;
                            try {
                                int position = Integer.parseInt(arg0.getSnippet());
                                // Getting view from the layout file info_window_layout
                                v = getLayoutInflater().inflate(R.layout.custom_map_address, null);
                                metaData = list.get(position);

                                // Getting reference to the TextView to set latitude
                                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                                // Getting reference to the TextView to set longitude
                                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                                TextView open = (TextView) v.findViewById(R.id.open);

                                // Setting the latitude
                                tvLng.setText("");

                                // Setting the longitude
                                tvLat.setText( metaData.getCity() );
                                open.setText("Distance:-" + metaData.getDis()+"   ");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            // Returning the view containing InfoWindow contents
                            return v;
                        }

                        // Defines the contents of the InfoWindow
                        @Override
                        public View getInfoContents(Marker arg0) {

                            return null;
                        }
                    });

                }
            }
        }
    }
}

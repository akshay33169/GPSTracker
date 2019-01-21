package com.example.a10008881.gpstrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView t1 , addr, distview , t2, addr2, distview2;
    private LocationManager myLoc;
    private LocationListener myLis;
    private Location prevloc = null;
    private float totdis = 0;
    int orient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        orient = getResources().getConfiguration().orientation;
        switch (orient)
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d("ORIENTATION IS","ORIENTATION_LANDSCAPE");
                setContentView(R.layout.gpslandscape);
                t2 = (TextView) findViewById(R.id.textView5);
                t2.setMaxLines(15);
                addr2 = (TextView) findViewById(R.id.textView6);
                addr2.setMaxLines(15);
                distview2 = (TextView) findViewById(R.id.textView7);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("ORIENTATION IS", "ORIENTATION_PORTRAIT");
                setContentView(R.layout.activity_main);
                t1 = (TextView) findViewById(R.id.textView_id);
                t1.setMaxLines(10);
                addr = (TextView) findViewById(R.id.textView);
                addr.setMaxLines(10);
                distview = (TextView) findViewById(R.id.textView2);
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                Log.d("ORIENTATION IS", "ORIENTATION_UNDEFINED");
                break;
            default:
                Log.d("ORIENTATION IS", "ORIENTATION_ERROR");
                break;
        }



        myLoc = (LocationManager) getSystemService(LOCATION_SERVICE);

        myLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("Height1", "Main-Longitude: " + location.getLongitude());
                Log.d("Width1", "Main-Latitude: " + location.getLatitude());
                if (orient == Configuration.ORIENTATION_PORTRAIT)

                    t1.append("\n " +location.getLongitude() + " , "  + location.getLatitude());
                else
                    t2.append("\n " +location.getLongitude() + " , "  + location.getLatitude());

                Log.d("AFT", "After Textview");
                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                Log.d("AF1", "After Geocode");
                List<Address>  addresses;
                try {
                    Log.d("AF2", "Befoer gcd1");
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Log.d("AF3", "After gcd");
                    if (addresses.size() > 0) {
                        Log.d("AF4", "Befoe getlocality");
                     //   Log.d("LOCA", addresses.get(0).getLocality());
                        Log.d("AF5", "After geetlocalitw");
                        cityName = addresses.get(0).getLocality();
                        if (cityName == null)
                            cityName = addresses.get(0).getSubLocality();
                        Log.d("AF6", "After citynae");
                        String address = addresses.get(0).getAddressLine(0);
                        Log.d("AF7", "After address");
                        String state = addresses.get(0).getAdminArea();
                        Log.d("AF8", "After state");
                      //  String country = addresses.get(0).getCountryName();
                        Log.d("AF9", "After country");
                    //    String postal = addresses.get(0).getPostalCode();
                        Log.d("AF7", "After postal");
                        if (orient == Configuration.ORIENTATION_PORTRAIT)
                         addr.append("\n "+ address + " , " + cityName + " , " + state);
                        else
                            addr2.append("\n "+ address + " , " + cityName + " , " + state);
                    }
                }
                catch (Exception e) {
                    Log.d("BAD", "EXCEPTION");
                    if (orient == Configuration.ORIENTATION_PORTRAIT)
                        addr.append("\n "+ "where r u" + " ");
                    else
                        addr2.append("\n "+ "where r u" + " ");
                }

                if (prevloc != null){
                    float distance = location.distanceTo(prevloc)/1000;
                    Log.d("DIS"," " + distance + " ");
                    totdis+=distance;
                    if (orient == Configuration.ORIENTATION_PORTRAIT)
                     distview.setText("Total distance (in km)  " + totdis);
                    else
                        distview2.setText("Total distance (in km)  " + totdis);

                }
                prevloc = location;


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("INCHK", "BEFORE REQ PERM");
            ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 10 );
            return;
        }
        else {
             myLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, myLis);
        }



    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("INREQ", "BEFORE GRANT");
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    myLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, myLis);


                } else {
                    Log.d("NOPERM", "PERMISSION DENIED");
                }
                return;
            }

        }
    }







}

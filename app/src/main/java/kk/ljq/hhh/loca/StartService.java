package kk.ljq.hhh.loca;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static kk.ljq.hhh.loca.MainActivity.MESSAGES_CHILD;

/**
 * Created by Elhadedy on 2/5/2017.
 */

public class StartService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private String E_mail;
    private String name;
    private String photo;
    private DatabaseReference mFirebaseDatabaseReference;
    private static boolean sends_check=false;
    private static int WT=0;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service starts",Toast.LENGTH_SHORT).show();


        E_mail=intent.getStringExtra("E_mail");
        name=intent.getStringExtra("name");
        photo=intent.getStringExtra("photo");

        get_location();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }
    public void get_location()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
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

    /** Checks whether two providers are the same */
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
        Toast.makeText(getApplicationContext(),"Service destory",Toast.LENGTH_SHORT).show();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public static Thread performOnBackgroundThread(final Runnable runnable) {
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
    public void  send_to_db(DriverInfo driverInfo){
        Toast.makeText(this,"Service check"+sends_check,Toast.LENGTH_SHORT).show();
        if(sends_check==false){
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            // Users users=new Users(lat,lon);
            DriverInfo dInfo=new DriverInfo(driverInfo);

            mFirebaseDatabaseReference.child(MESSAGES_CHILD).push()
                    .setValue(dInfo);
            sends_check=true;



        }
        else {Update_from_db(driverInfo);

        }




    }
    public void Update_from_db(final DriverInfo driverInfo){





        mFirebaseDatabaseReference =FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);

        mFirebaseDatabaseReference.orderByChild("d_Email").equalTo(E_mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                  //  Toast.makeText(getApplicationContext(),"update location",Toast.LENGTH_SHORT).show();
                    snapshot.getRef().child("d_lat").setValue(driverInfo.getD_lat());
                    snapshot.getRef().child("d_lon").setValue(driverInfo.getD_lon());
                    snapshot.getRef().child("d_loc_name").setValue(driverInfo.getD_loc_name());
                    snapshot.getRef().child("d_provider_name").setValue(driverInfo.getD_provider_name());
                    snapshot.getRef().child("d_spead").setValue(driverInfo.getD_spead());
                    snapshot.getRef().child("d_time").setValue(driverInfo.getD_time());


                    //Toast.makeText(getApplicationContext(),"update location",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"update cancelled",Toast.LENGTH_SHORT).show();

            }
        });






    }



    public class MyLocationListener  implements LocationListener {






        public void onLocationChanged(final Location loc)
        {
            float x=loc.getAccuracy();


            Log.i("**************************************", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {
                //send_state();
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                Toast.makeText( getApplicationContext(), "Location polled to server", Toast.LENGTH_SHORT).show();
                sendBroadcast(intent);

            final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            int waiting_timehour = 0;
            int waiting_timemin=0;
                int shour = 0;
                int smin=0;
                int emin=0;
                int ehour=0;
            try {
                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if(addresses!=null&&addresses.size()>0){


                   if(loc.getSpeed()==0.0&&WT==0) {
                       Calendar c = Calendar.getInstance();

                       shour = c.get(Calendar.HOUR_OF_DAY);
                       smin = c.get(Calendar.MINUTE);
                       WT=1;


                   }
                    if (loc.getSpeed()>0&&WT==1){
                        Calendar c = Calendar.getInstance();
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                        ehour = c.get(Calendar.HOUR_OF_DAY);
                        emin = c.get(Calendar.MINUTE);

                        WT=0;
                        waiting_timehour=ehour-shour;
                        waiting_timemin=emin-smin; if (waiting_timemin<0){waiting_timemin=waiting_timemin*-1;}

                        send_waiting(waiting_timehour,waiting_timemin,addresses.get(0).getAddressLine(0),""+ehour+":"+emin,currentDateTimeString);




                    }

                    send_to_db(new DriverInfo(loc.getLatitude(),loc.getLongitude(),name,"01063039159",E_mail,""+waiting_timehour,addresses.get(0).getFeatureName()+" / "+addresses.get(0).getAddressLine(0),photo,loc.getTime() / (1000*60*60) % 24,loc.getSpeed(),loc.getProvider(),addresses.get(0).getUrl()) );
                    //  delete_from_db(new DriverInfo(location.getLatitude(),location.getLongitude(),mUsername,"01063039159",mE_mail,"30",addressList.get(0).getFeatureName(),mPhotoUrl,location.getTime() / (1000*60*60) % 24,location.getSpeed(),location.getProvider(),addressList.get(0).getUrl()));//333

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              //  Text = "My current location is: " +"Latitude = " + loc.getLatitude() + ", Longitude = " + loc.getLongitude();
            }

                //Toast.makeText( getApplicationContext(), "Location polled to server", Toast.LENGTH_SHORT).show();
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();

        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }

    private void send_waiting(int waiting_timehour, int waiting_timemin,String st,String time_go,String DATE_GO)  {


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.keepSynced(true);
        DriverInfo d=new DriverInfo(E_mail,name,st,""+waiting_timehour+":"+waiting_timemin,time_go,DATE_GO);
        mFirebaseDatabaseReference.child("waiting feild").push().setValue(d);

    }
    private void send_state() {
        // since I can connect from multiple devices, we store each connection instance separately
// any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("users/joe/connections");

// stores the timestamp of my last disconnect (the last time I was seen online)
        final DatabaseReference lastOnlineRef = database.getReference("/users/joe/lastOnline");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    DatabaseReference con = myConnectionsRef.push();
                    con.setValue(Boolean.TRUE);

                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();

                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }


}


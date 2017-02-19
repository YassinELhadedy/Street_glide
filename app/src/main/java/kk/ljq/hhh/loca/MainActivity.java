package kk.ljq.hhh.loca;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yayandroid.locationmanager.LocationBaseActivity;
import com.yayandroid.locationmanager.LocationConfiguration;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.LogType;
import com.yayandroid.locationmanager.constants.ProviderType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends LocationBaseActivity  {
   // public static int RC_SIGN_IN =0;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private DatabaseReference mFirebaseDatabaseReference;//new 2222
    public static final String MESSAGES_CHILD = "delivery_location5";//new 222
    private FirebaseUser mFirebaseUser;//new 3333
    private String mUsername ; //new 3333
    private String mE_mail;//3333
    private String mPhotoUrl;//3333333
    private static  boolean send_check=false;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAG = MainActivity.class.getSimpleName();//let’s add that while we’re here:
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    private FirebaseRecyclerAdapter<DriverInfo, AdminView.MessageViewHolder> mFirebaseAdapter;
    private static ArrayList<DriverInfo> driverInfoArrayList = new ArrayList<DriverInfo>();
    private RecyclerView listView;




    @Override
    public LocationConfiguration getLocationConfiguration() {
     return    new LocationConfiguration()
                .keepTracking(true)
                .useOnlyGPServices(false)
                .askForGooglePlayServices(true)
                .askForSettingsApi(true)
                .failOnConnectionSuspended(true)
                .failOnSettingsApiSuspended(false)
                .doNotUseGooglePlayServices(false)
                .askForEnableGPS(true)
                .setMinAccuracy(200.0f)
                .setWithinTimePeriod(60 * 1000)
                .setTimeInterval(10 * 1000)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 15 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 10 * 1000)
                .setLocationRequest(mLocationRequest)
                .setGPSMessage("Would you mind to turn GPS on?")
                .setRationalMessage("Gimme the permission!");
    }

    @Override
    public void onLocationChanged(Location location) {
      //  delete_from_db();
        Toast.makeText(this, ""+ location.getLongitude(),Toast.LENGTH_SHORT).show();
        dismissProgress();
        setText(location);
    }

    @Override
    public void onLocationFailed(int failType) {
        dismissProgress();

        switch (failType) {
            case FailType.PERMISSION_DENIED: {
                Toast.makeText(this,"Couldn't get location, because user didn't give permission!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.GP_SERVICES_NOT_AVAILABLE:
            case FailType.GP_SERVICES_CONNECTION_FAIL: {
                Toast.makeText(this,"Couldn't get location, because Google Play Services not available!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                Toast.makeText(this,"Couldn't get location, because network is not accessible!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.TIMEOUT: {
                Toast.makeText(this,"Couldn't get location, and timeout!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DENIED: {
                Toast.makeText(this,"Couldn't get location, because user didn't activate providers via settingsApi!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DIALOG: {
                Toast.makeText(this,"Couldn't display settingsApi dialog!",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_from_sharedpref();
        Toast.makeText(this,""+send_check,Toast.LENGTH_SHORT).show();
     //   mGoogleApiClient.connect();


        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        save_in_sharedpref();


    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
        save_in_sharedpref();
        Toast.makeText(this,""+send_check,Toast.LENGTH_SHORT).show();
     //   mGoogleApiClient.connect();// new lang

    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setText(Location location) {


        //locationText.setText(newValue);
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null&&addressList.size()>0){
                Time now = new Time();
                now.setToNow();
                Toast.makeText(getApplicationContext(),now.toString(),Toast.LENGTH_SHORT).show();


               Update_from_db(new DriverInfo(location.getLatitude(),location.getLongitude(),mUsername,"01063039159",mE_mail,"30",addressList.get(0).getFeatureName()+"/"+addressList.get(0).getAddressLine(0),mPhotoUrl,location.getTime() / (1000*60*60) % 24,location.getSpeed(),location.getProvider(),addressList.get(0).getUrl()) );
              //  delete_from_db(new DriverInfo(location.getLatitude(),location.getLongitude(),mUsername,"01063039159",mE_mail,"30",addressList.get(0).getFeatureName(),mPhotoUrl,location.getTime() / (1000*60*60) % 24,location.getSpeed(),location.getProvider(),addressList.get(0).getUrl()));//333

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            get_from_sharedpref();
            progressDialog=new ProgressDialog(this);


            Log.d("Auth",firebaseAuth.getCurrentUser().getEmail());


            if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
                mPhotoUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();


            }
            get_order(firebaseAuth.getCurrentUser().getEmail());//hererrrrrrrrrrrr

            //startActivity(new Intent(this,Loginpage.class));
            Toast.makeText(this,"iam here",Toast.LENGTH_SHORT).show();//new 111111


            mE_mail=firebaseAuth.getCurrentUser().getEmail();//3333333333
            mUsername=firebaseAuth.getCurrentUser().getDisplayName();//3333333333
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString("E_mail", mE_mail).apply();
            prefs.edit().putString("name",mUsername).apply();
            prefs.edit().putString("photo", String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl())).apply();





            startService(new Intent(getApplicationContext(),StartService.class).putExtra("E_mail",mE_mail).putExtra("name",mUsername).putExtra("photo",mPhotoUrl));








            get_location();
           // Intent intent = new Intent(this, StartService.class);

            //startService(intent);

            // Create the LocationRequest object
                     mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(2 * 1000); // 1 second, in milliseconds


        }
        else {
            startActivity(new Intent(this,Loginpage.class));//new  11111111

          /*  startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(AuthUI.FACEBOOK_PROVIDER,AuthUI.EMAIL_PROVIDER,AuthUI.GOOGLE_PROVIDER)
                    .build(),RC_SIGN_IN);*/}

    }
    public  void get_location(){
        LocationManager.setLogType(LogType.GENERAL);
        getLocation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"sign out",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
}
    public void  send_to_db(DriverInfo driverInfo){
        if(send_check==false){
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            // Users users=new Users(lat,lon);
            DriverInfo dInfo=new DriverInfo(driverInfo);

            mFirebaseDatabaseReference.child(MESSAGES_CHILD).push()
                    .setValue(dInfo);
            send_check=true;



        }
        else {Update_from_db(driverInfo);

        }




    }
    public void Update_from_db(final DriverInfo driverInfo){





        mFirebaseDatabaseReference =FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);

        mFirebaseDatabaseReference.orderByChild("d_Email").equalTo(mE_mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                   // Toast.makeText(getApplicationContext(),"update location",Toast.LENGTH_SHORT).show();
                    snapshot.getRef().child("d_lat").setValue(driverInfo.getD_lat());
                    snapshot.getRef().child("d_lon").setValue(driverInfo.getD_lon());
                    snapshot.getRef().child("d_loc_name").setValue(driverInfo.getD_loc_name());
                    snapshot.getRef().child("d_provider_name").setValue(driverInfo.getD_provider_name());
                    snapshot.getRef().child("d_spead").setValue(driverInfo.getD_spead());


                    //Toast.makeText(getApplicationContext(),"update location",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"update cancelled",Toast.LENGTH_SHORT).show();

            }
        });






    }
    public void save_in_sharedpref(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("locked", send_check).apply();

    }
    public void get_from_sharedpref(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        send_check = prefs.getBoolean("locked", false);
    }
    public void delete_specific_item(final DriverInfo driverInfo){
        mFirebaseDatabaseReference=FirebaseDatabase.getInstance().getReference();
        Query queryRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).orderByChild("d_Email").equalTo(mE_mail);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(),"data changed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"data removed",Toast.LENGTH_SHORT).show();
               // send_to_db(driverInfo);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"data cancelled",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void update_firebase(){

        mFirebaseDatabaseReference=FirebaseDatabase.getInstance().getReference();
        Query queryRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).orderByChild("d_Email").equalTo(mE_mail);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().child("Status").setValue("COMPLETED");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void get_order(String email){

        listView = (RecyclerView) findViewById(R.id.list);
        driverInfoArrayList.clear();
        progressDialog.setMessage("loading");
        progressDialog.show();

        mFirebaseDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter=new FirebaseRecyclerAdapter<DriverInfo, AdminView.MessageViewHolder>( DriverInfo.class,
                R.layout.list_row,
                AdminView.MessageViewHolder.class,
                mFirebaseDatabaseReference.child("order feild").orderByChild("d_Email").equalTo(email)) {
            @Override
            protected void populateViewHolder(AdminView.MessageViewHolder viewHolder, final DriverInfo model, final int position) {


                viewHolder.title.setText(model.getAdd_time_order());
                viewHolder. author.setText(model.getAdd_location_order());
               viewHolder.pages.setText(model.getAdd_rating_order()+"star");

                viewHolder.image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_account_circle_black_36dp));



                progressDialog.dismiss();

            }
        };


        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(mFirebaseAdapter);

    }



}

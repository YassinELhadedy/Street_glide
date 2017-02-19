package kk.ljq.hhh.loca;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static kk.ljq.hhh.loca.MainActivity.MESSAGES_CHILD;

public class AdminMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mFirebaseDatabaseReference;
    private DriverInfo driverInfoArrayLis;
    private String e_mail;
    BitmapDescriptor icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        e_mail=getIntent().getStringExtra("E_mail");
        get_db();

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        // Add a marker in Sydney and move the camera

    }
    public void Add_marker(DriverInfo driverInfo){
        mMap.clear();
        LatLng sydney = new LatLng(driverInfo.getD_lat(), driverInfo.getD_lon());
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(sydney).title(driverInfo.getD_name())).setIcon(icon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomDialog dialogFragment = CustomDialog.newInstance(
                        driverInfoArrayLis.getD_name(),driverInfoArrayLis.getD_loc_name(),driverInfoArrayLis.getD_spead());
                dialogFragment.show(getSupportFragmentManager(),"dialog");
                return true;
            }
        });



    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    private void loadMarkerIcon(String url) {
        Glide.with(getApplication()).load(url)
                .asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                 icon = BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(bitmap));
                //marker.setIcon(icon);
            }
        });
    }


    public void get_db(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Read from the database
        mFirebaseDatabaseReference.child(MESSAGES_CHILD).orderByChild("d_Email").equalTo(e_mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DriverInfo post = dataSnapshot.getValue(DriverInfo.class);
                Toast.makeText(getApplicationContext(),post.getD_name(),Toast.LENGTH_SHORT).show();

                Add_marker(post);
                driverInfoArrayLis=new DriverInfo();
                driverInfoArrayLis=post;
                loadMarkerIcon(post.getD_photo());




            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DriverInfo post = dataSnapshot.getValue(DriverInfo.class);
                Toast.makeText(getApplicationContext(),"LOCATION CHANGED",Toast.LENGTH_SHORT).show();
                Add_marker(post);
                Toast.makeText(getApplicationContext(),"LOCATION CHANGED",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}

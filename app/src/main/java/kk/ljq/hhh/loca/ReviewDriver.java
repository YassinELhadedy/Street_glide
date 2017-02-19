package kk.ljq.hhh.loca;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewDriver extends AppCompatActivity {
    ListView lv_spc_table;
    SimpleAdapter sim_adapt_table;
    ArrayList< HashMap<String, String> > arr_list = new ArrayList< HashMap<String, String> >();
    private DatabaseReference mFirebaseDatabaseReference;
    static  int counter=0;
    String e_mail;
    String name;
    String photo;
    Drawable drawImage;
     ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_driver);
        name=getIntent().getStringExtra("name");
        photo=getIntent().getStringExtra("photo");
        ab=getSupportActionBar();


        ab.setTitle(name);
        Picasso.with(this)
                .load(photo)
                .into(new com.squareup.picasso.Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable d = new BitmapDrawable(getResources(), getCroppedBitmap(bitmap));
                        ab.setIcon(d);
                        ab.setDisplayShowHomeEnabled(true);
                        ab.setDisplayHomeAsUpEnabled(true);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        arr_list.clear();


        lv_spc_table = (ListView) findViewById(R.id.lv_spc_table);
        e_mail=getIntent().getStringExtra("email");
        mFirebaseDatabaseReference= FirebaseDatabase.getInstance().getReference().child("waiting feild");
        sim_adapt_table = new SimpleAdapter
                (getApplicationContext(),
                        arr_list,
                        R.layout.row_review,
                        new String[]{"id", "name", "waiting", "timego", "date"},
                        new int[]{R.id.cell_No, R.id.cell_st_name, R.id.cell_waiting, R.id.cell_timego, R.id.cell_date});
        lv_spc_table.setAdapter(sim_adapt_table);
        mFirebaseDatabaseReference.orderByChild("d_Email").equalTo(e_mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DriverInfo driverInfo=dataSnapshot.getValue(DriverInfo.class);


                HashMap <String, String> map = new HashMap <String, String>();
                map.put("id",""+counter );
                map.put("name", driverInfo.getD_loc_name());
                map.put("waiting", driverInfo.getD_loc_wait());
                map.put("timego", driverInfo.getTime_go());
                map.put("date", driverInfo.getDate_go());

                arr_list.add(map);
                counter++;
                sim_adapt_table.notifyDataSetChanged();




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    public void appendToMessageHistory(String photo) {
        if (photo != null) {

            ImageView image = new ImageView(getApplicationContext());

            Picasso.with(getBaseContext()).load(photo).into(new com.squareup.picasso.Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                     drawImage = new BitmapDrawable(getBaseContext().getResources(),bitmap);


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });







        }
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
}

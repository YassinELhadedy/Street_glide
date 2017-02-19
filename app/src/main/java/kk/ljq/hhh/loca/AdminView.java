package kk.ljq.hhh.loca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static kk.ljq.hhh.loca.MainActivity.MESSAGES_CHILD;

public class AdminView extends AppCompatActivity {
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
         public  TextView title;
         public TextView author;
         public TextView pages;
         public  CircleImageView image;



        public MessageViewHolder(final View v) {
            super(v);
             title = (TextView) v.findViewById(R.id.title);
             author = (TextView) v.findViewById(R.id.author);
             pages = (TextView) v.findViewById(R.id.pages);
             image = (CircleImageView) v.findViewById(R.id.list_image);
            v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    final int pos=getAdapterPosition();
                    contextMenu.setHeaderTitle("Select The Action");
                    contextMenu.add(0, v.getId(), 0, "Call").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Toast.makeText(v.getContext(), "calling code", Toast.LENGTH_LONG).show();
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+driverInfoArrayList.get(pos).getD_number()));
                          v.getContext().  startActivity(callIntent);

                            return true;
                        }
                    });//groupId, itemId, order, title
                    contextMenu.add(0, v.getId(), 0, "SMS").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Toast.makeText(v.getContext(),"sending sms code",Toast.LENGTH_LONG).show();
                            String number = driverInfoArrayList.get(pos).getD_number();  // The number on which you want to send SMS
                            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                            return true;
                        }
                    });
                    contextMenu.add(0, v.getId(), 0, "Order").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Toast.makeText(v.getContext(),"Adding Order",Toast.LENGTH_LONG).show();
                            v.getContext().startActivity(new Intent(v.getContext(),OrderDriver.class).putExtra("name",driverInfoArrayList.get(pos).getD_name()).putExtra("email",driverInfoArrayList.get(pos).getD_Email()).putExtra("photo",driverInfoArrayList.get(pos).getD_photo()));
                            return true;
                        }
                    });
                    contextMenu.add(0, v.getId(), 0, "review").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Toast.makeText(v.getContext(),"review driver",Toast.LENGTH_LONG).show();
                            v.getContext().startActivity(new Intent(v.getContext(),ReviewDriver.class).putExtra("email",driverInfoArrayList.get(pos).getD_Email()).putExtra("name",driverInfoArrayList.get(pos).getD_name()).putExtra("photo",driverInfoArrayList.get(pos).getD_photo()));
                            return true;
                        }
                    });
                }
            });



            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    Toast.makeText(view.getContext(), "Id Clicked: " + pos, Toast.LENGTH_LONG).show();
                    view.getContext().startActivity(new Intent(view.getContext(), AdminMaps.class).putExtra("E_mail", driverInfoArrayList.get(pos).getD_Email()));

                }
            });








        }
    }

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<DriverInfo, MessageViewHolder> mFirebaseAdapter;
    private static ArrayList<DriverInfo> driverInfoArrayList = new ArrayList<DriverInfo>();
    private RecyclerView listView;
    private CustomListViewAdapter customListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (RecyclerView) findViewById(R.id.list);
        driverInfoArrayList.clear();

        mFirebaseDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter=new FirebaseRecyclerAdapter<DriverInfo, MessageViewHolder>( DriverInfo.class,
                R.layout.list_row,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, final DriverInfo model, final int position) {


               viewHolder.title.setText(model.getD_name());
               viewHolder. author.setText(model.getD_loc_name());
                if (model.isD_state()){ viewHolder. pages.setText("online");}

                else {double offset=model.getLast_seen();
                    double estimatedServerTimeMs = System.currentTimeMillis() + offset;
                    SimpleDateFormat sfd = new SimpleDateFormat(" hh:mm:ss a");






                    viewHolder. pages.setText("last seen "+sfd.format(new Date((long) estimatedServerTimeMs)) );
                }


                if(model.getD_photo()!=null){

                    Picasso.with(getApplicationContext()).load(model.getD_photo()).into(viewHolder.image);}
                else {
                    viewHolder.image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_account_circle_black_36dp));


                }
                driverInfoArrayList.add(model);





            }
        };


        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(mFirebaseAdapter);




    }



}

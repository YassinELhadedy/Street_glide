package kk.ljq.hhh.loca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import static kk.ljq.hhh.loca.MainActivity.MESSAGES_CHILD;

/**
 * Created by Elhadedy on 2/12/2017.
 */

public class NetwokReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String e_mail = prefs.getString("E_mail","Anonymous");
        String name= prefs.getString("name","Anaonmous");
        String photo=prefs.getString("photo","Anonmous");
         send_state(e_mail);



        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo()!=null){
         //   delete_state();
            Toast.makeText(context, "Connected to Internet", Toast.LENGTH_LONG).show();
           // send_state();


            Toast.makeText(context,"connect Service",Toast.LENGTH_SHORT).show();
            context.startService(new Intent(context,StartService.class).putExtra("E_mail",e_mail).putExtra("name",name).putExtra("photo",photo));

            Toast.makeText(context,"connect Service",Toast.LENGTH_SHORT).show();
        }
        else{ Log.i("INTERNET","---------------------> Internet Disconnected. ");
           // delete_state();

            Toast.makeText(context,"disconnect Service",Toast.LENGTH_SHORT).show();
       // send_state();
            }

        context.startService(new Intent(context,StartService.class).putExtra("E_mail",e_mail).putExtra("name",name).putExtra("photo",photo));

    }

    private void send_state(final String name) {
       // delete_state();
        // since I can connect from multiple devices, we store each connection instance separately
// any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myConnectionsRef = database.getReference().child(MESSAGES_CHILD);

// stores the timestamp of my last disconnect (the last time I was seen online)
        final DatabaseReference lastOnlineRef = database.getReference().child(MESSAGES_CHILD);

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    myConnectionsRef.orderByChild("d_Email").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                          //  DatabaseReference con = myConnectionsRef.push();
                            snapshot.getRef().child("d_state").setValue(Boolean.TRUE);

                            // when this device disconnects, remove it
                            snapshot.getRef().child("d_state").onDisconnect().removeValue();

                            // when I disconnect, update the last time I was seen online
                            snapshot.getRef().child("last_seen").onDisconnect().setValue(ServerValue.TIMESTAMP);

                        }}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }

    public void delete_state(){
        DatabaseReference mFirebaseDatabaseReference=FirebaseDatabase.getInstance().getReference("users/");
        mFirebaseDatabaseReference.setValue(null);

    }
    }



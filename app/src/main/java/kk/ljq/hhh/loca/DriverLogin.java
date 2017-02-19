package kk.ljq.hhh.loca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class DriverLogin extends AppCompatActivity {
    public static int RC_SIGN_IN =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(AuthUI.FACEBOOK_PROVIDER,AuthUI.EMAIL_PROVIDER,AuthUI.GOOGLE_PROVIDER)
                    .build(),RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if (resultCode==RESULT_OK){


               // Log.d("Auth",firebaseAuth.getCurrentUser().getEmail());
                //  startActivity(new Intent(this,MapsActivity.class));
                Toast.makeText(getApplicationContext(),"sucess login",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                finish();

            }
            else {Log.d("Auth","Not Auth");
                Toast.makeText(getApplicationContext(),"failed login",Toast.LENGTH_SHORT).show();}
        }
    }

}

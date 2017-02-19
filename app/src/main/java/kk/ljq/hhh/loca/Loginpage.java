package kk.ljq.hhh.loca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import static kk.ljq.hhh.loca.R.id.riderOrDriverSwitch;

public class Loginpage extends AppCompatActivity {
   private Switch AdminOrDriverSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        getSupportActionBar().hide();
        AdminOrDriverSwitch= (Switch) findViewById(riderOrDriverSwitch);


    }
    public void getStarted(View view) {

        String AdminOrDriver = "Admin";


        if (AdminOrDriverSwitch.isChecked()) {

            AdminOrDriver = "driver";
            Toast.makeText(getApplicationContext(),"driver",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,DriverLogin.class));


        }
        else { Toast.makeText(getApplicationContext(),"Admin",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Adminlogin.class));}







    }
}

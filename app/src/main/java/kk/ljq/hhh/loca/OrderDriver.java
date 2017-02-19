package kk.ljq.hhh.loca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDriver extends AppCompatActivity {
    private String name;
    private String e_mail;
    private String photo;
    private EditText us_name;
    private EditText add_location;
    private EditText add_time;
    private EditText rating;
    private Button send;
    private TextView check;
    private DatabaseReference mFirebaseDatabaseReference;
    private CircleImageView pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_driver);
        name=getIntent().getStringExtra("name");
        e_mail=getIntent().getStringExtra("email");
        photo=getIntent().getStringExtra("photo");
        us_name=(EditText)findViewById(R.id.et_username);
        add_location=(EditText)findViewById(R.id.et_email);
        add_time=(EditText)findViewById(R.id.et_password);
        rating=(EditText)findViewById(R.id.et_repeat_password);
        check=(TextView)findViewById(R.id.tv_already_have_account);
        send=(Button)findViewById(R.id.btn_signup);
        pic=(CircleImageView)findViewById(R.id.img_header_logo);
        if (photo!=null){
        Picasso.with(this).load(photo).into(pic);}
        else {
            pic.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_account_circle_black_36dp));

        }

        us_name.setText(name);
        us_name.setFocusable(false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(add_location.getText()).isEmpty()&&String.valueOf(add_time.getText()).isEmpty()&&String.valueOf(rating.getText()).isEmpty()){

                    check.setText("failed send");


                }

                else {
                    mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

                    DriverInfo d=new DriverInfo(String.valueOf(add_location.getText()),String.valueOf(rating.getText()),String.valueOf(add_time.getText()),e_mail);
                    mFirebaseDatabaseReference.child("order feild").push().setValue(d);
                    check.setText("successfully send");
                    add_time.setText("");
                    add_location.setText("");
                    rating.setText("");


                }


            }
        });



    }
}

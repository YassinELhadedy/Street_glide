package kk.ljq.hhh.loca;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Adminlogin extends AppCompatActivity {
    private EditText user_name;
    private EditText user_pass;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        user_name=(EditText) findViewById(R.id.input_email);
        user_pass=(EditText)findViewById(R.id.input_password);
        login=(Button)findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            Toast.makeText(this,firebaseAuth.getCurrentUser().getDisplayName().toString(),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,AdminMaps.class));
            finish();


        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=user_name.getText().toString();
                String pass=user_pass.getText().toString();
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sucess login",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),firebaseAuth.getCurrentUser().getDisplayName().toString(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),AdminView.class));
                            finish();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Faild login",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });

            }
        });

    }
}

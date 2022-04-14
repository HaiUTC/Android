package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private RelativeLayout login, register;
    private TextView forgotpassword;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBarMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        forgotpassword = findViewById(R.id.gotoforgotpassword);
        register = findViewById(R.id.gotosignup);

        //Returns an instance of this class corresponding to the default FirebaseApp instance.
        //Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        //Get current user in your app
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //Spinner
        progressBarMain = findViewById(R.id.progressbarofmainactivity);

        //Check if have user, app will ignore main activity and start with note activity
        //check to see if the user is currently signed in.
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,Note.class));
        }
        //Redirect to register page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        //Redirect to Forgot Password page
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get email + password
                String ema = email.getText().toString().trim();
                String pass= password.getText().toString().trim();
                // Check empty
                if(ema.isEmpty()|| pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Email and password are required",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Start spinner
                    progressBarMain.setVisibility(View.VISIBLE);
                    //Async firebase auth login
                    firebaseAuth.signInWithEmailAndPassword(ema, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Check verify email
                                checkmailverfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Email or password incorrect.",Toast.LENGTH_SHORT).show();
                                progressBarMain.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
            private void checkmailverfication()
            {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                if(firebaseUser.isEmailVerified()==true)
                {
                    Toast.makeText(getApplicationContext(),"Log In Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this,Note.class));
                }
                else
                {
                    progressBarMain.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Please verify your email",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                }
            }
        });
    }
}
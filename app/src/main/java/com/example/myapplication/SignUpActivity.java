package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public EditText nemail;
    public EditText npass;
    public EditText mname;
    public EditText mphone;
    public ProgressBar progressBar;
    public String email;
    private String passw;
    private String phoneno;
    private String namen;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.sign_up);


        mAuth = FirebaseAuth.getInstance();
        nemail = (EditText) findViewById(R.id.email);
        npass = (EditText) findViewById(R.id.passw);
        mname = (EditText) findViewById(R.id.name);
        mphone = (EditText) findViewById(R.id.phone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        Button signup = findViewById(R.id.signup);
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(
                    SignUpActivity.this, MainActivity.class));
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=nemail.getText().toString();
                passw= npass.getText().toString();
                phoneno = mphone.getText().toString();
                namen = mname.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    nemail.setError("Required.");
                    return;
                } else {
                    nemail.setError(null);
                }

                if (TextUtils.isEmpty(namen)) {
                    mname.setError("Required.");
                    return;
                } else {
                    mname.setError(null);
                }

                if (TextUtils.isEmpty(phoneno)) {
                    mphone.setError("Required.");
                    return;
                } else {
                    mphone.setError(null);
                }

                if (TextUtils.isEmpty(passw) ) {
                    npass.setError("Required.");
                    return;
                }
                else if(passw.length()<=6)
                {
                    npass.setError("Password should be more than 6 characters");
                    return;
                }
                else {
                    npass.setError(null);
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(
                                    SignUpActivity.this, activity_login.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error ocurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tv = (TextView) findViewById(R.id.signIn_text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        SignUpActivity.this, activity_login.class));
            }
        });

    }
}

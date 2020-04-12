package com.example.myapplication;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_login  extends AppCompatActivity {
    TextView tv;
    private FirebaseAuth mAuth;
    public EditText memail;
    public EditText mpass;
    public ProgressBar pb;
    String email;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        Button signin = findViewById(R.id.signin);
        memail = (EditText) findViewById(R.id.siemail);
        mpass = (EditText) findViewById(R.id.pass);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(
                    activity_login.this, MainActivity.class));
        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = memail.getText().toString();
                pass = mpass.getText().toString();
              if (TextUtils.isEmpty(email)) {
                    memail.setError("Required.");
                    return;
                } else {
                    memail.setError(null);
                }

                if (TextUtils.isEmpty(pass) ) {
                    mpass.setError("Required.");
                    return;
                }
                else {
                    mpass.setError(null);
                }

                pb.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(
                                    activity_login.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error ocurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        tv = (TextView) findViewById(R.id.signUp_text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        activity_login.this, SignUpActivity.class));
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}

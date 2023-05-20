package com.MP23_T11.luckycalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";

    EditText ID, password, psAagin;
    ImageView signUpButton;
    TextView returnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ID = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        psAagin = (EditText) findViewById(R.id.passwordInputAgain);
        signUpButton = (ImageView) findViewById(R.id.signUp);
        returnLogin = (TextView) findViewById(R.id.returnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick start");
                progressBar.setVisibility(View.VISIBLE);
                String email, ps, psA;
                email = String.valueOf(ID.getText());
                ps = String.valueOf(password.getText());
                psA = String.valueOf(psAagin.getText());
                Log.d(TAG, "getString");

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(ps)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!TextUtils.equals(ps, psA)) {
                    Toast.makeText(Register.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                Log.d(TAG, "Defensive coding");

                mAuth.createUserWithEmailAndPassword(email, ps)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "onComplete");
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "true");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d(TAG, "get Auth");
                                    Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT);
                                    ID.setText("");
                                    password.setText("");
                                    psAagin.setText("");
                                }
                                else {
                                    Log.d(TAG, "false");
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT);
                                }
                            }
                        });
            }
        });
    }
}
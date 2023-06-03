package com.MP23_T11.luckycalendar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 9001;

    FirebaseAuth mAuth;
    EditText ID;
    EditText password;
    TextView create_account;
    ImageView loginButton;
    ProgressBar progressBar;
    SignInButton google_login;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount gsa;
    private ActivityResultLauncher<Intent> signInLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        선언한 View들을 activity_main.xml과 연결
        mAuth: Firebase에서 사용할 사용자 정보 instance
        loginButton: 이메일로 로그인할 경우 이용하는 버튼
        progressBar: 로그인 진행시 화면에 띄우는 로딩창
        google_login: google 계정으로 로그인할 경우 누르는 버튼
         */
        mAuth = FirebaseAuth.getInstance();
        ID = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        create_account = (TextView) findViewById(R.id.createAccountButton);
        loginButton = (ImageView) findViewById(R.id.login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        google_login = (SignInButton) findViewById(R.id.google_login);


        /*
        Create Account를 눌렀을 때 발생하는 Event
        Register.class로 화면 이동
         */
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });


        /*
        Login 버튼을 눌렀을 때 발생하는 Event
        Email입력칸과 Password입력칸의 문자열을 받아서 Firebase에 접속

        Defensive coding: Email이나 Password가 입력되지 않았을 경우 로그인 event X

        mAuth 객체를 이용해 Firebase로그인
        로그인 성공시 -> MainActivity.class로 이동
        로그인 실패시 -> Toast 메시지 출력
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, ps;
                email = String.valueOf(ID.getText());
                ps = String.valueOf(password.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(ps)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, ps)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        /*
        여기서부터는 쭉 구글 로그인
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsa = GoogleSignIn.getLastSignedInAccount(Login.this);

                // 이미 로그인 되어있는 경우
                if (gsa != null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                    signIn();
            }
        });

        // StartActivityForResult -> google Login
        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        Log.d(TAG, "onActivityResult start");
                        IdpResponse response = result.getIdpResponse();
                        if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                            onSignInResult(result);
                            Log.d(TAG, "onActivityResult end");
                        } else {
                            if (response == null) {
                                // User has cancelled the sign-in request
                                Log.d(TAG, "Sign-in cancelled");
                            } else {
                                // Some error occurred
                                Log.d(TAG, "Sign-in error: ", response.getError());
                            }
                        }
                    }
                }
        );
    }

    private void signIn(){
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent(); // Error
        //getSignInIntent가 제대로 동작하지 않는듯?
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        Log.d(TAG, "signInEnd");
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Log.d(TAG, "signInWithCredential:success");
            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                        Log.d(TAG, "onActivityResult");
                    }
                }
            });

    // 사용자 정보 가져오기
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                firebaseAuthWithGoogle(acct.getIdToken());

                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName "+personName);
                Log.d(TAG, "handleSignInResult:personGivenName "+personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail "+personEmail);
                Log.d(TAG, "handleSignInResult:personId "+personId);
                Log.d(TAG, "handleSignInResult:personFamilyName "+personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto "+personPhoto);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }
                });
    }
     */

}
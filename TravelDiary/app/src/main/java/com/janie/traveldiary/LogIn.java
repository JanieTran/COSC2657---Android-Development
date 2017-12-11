package com.janie.traveldiary;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogIn extends AppCompatActivity {
    // PROPERTIES

    // Logcat tag
    private final String TAG = LogIn.class.getSimpleName();

    // Authentication
    private FirebaseAuth auth;
    private GoogleSignInOptions gso;
    public static GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    public static Uri userProfilePic;

    // Layout elements
    private SignInButton btnSignIn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Log.i(TAG, "App created");

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build GoogleSignInClient with gso options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Link to layout
        btnSignIn = findViewById(R.id.sign_in_button);
        progressBar = findViewById(R.id.progressBar);

        // Sign in
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent itnSignIn = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(itnSignIn, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w("tag", "signInResult: failed code = " + e.getMessage());
        }
    }

    // Authenticate with Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        System.out.println("Firebase authen");
        AuthCredential credential
                = GoogleAuthProvider.getCredential(acc.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(".login", "signInWithCredential success");

                            // Get user profile pic
                            account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            userProfilePic = account.getPhotoUrl();

                            // Go to main activity
                            Intent toMain = new Intent(LogIn.this, MainActivity.class);
                            startActivity(toMain);
                            finish();
                        } else {
                            Log.w(".login", "signInWithCredential fail", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Google sign out
    public static void signOut() {
        mGoogleSignInClient.signOut();
    }

    // OTHERS

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "App started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "App paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "App resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "App stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "App restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "App destroyed");
    }
}

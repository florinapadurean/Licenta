package com.example.padurean.quizzgame.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.padurean.quizzgame.Errors.ErrorWifi;
import com.example.padurean.quizzgame.MainActivity;
import com.example.padurean.quizzgame.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity implements View.OnClickListener{

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG="login";
    private ProgressDialog progressDialog;
    private TextView signup_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check if wifi is connected
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork==null){
            Log.i(TAG,"null");
            startActivity(new Intent(Login.this,ErrorWifi.class));
            this.finish();
        }

        if (activeNetwork != null) { // connected to the internet
            if ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)) {
                Log.i(TAG, "connected");
            }
        }
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        progressDialog=new ProgressDialog(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        signup_text=(TextView)findViewById(R.id.textViewSignUp);
        signup_text.setOnClickListener(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Log.i(TAG,"Success login");
            }

            @Override
            public void onCancel() {
                Log.i(TAG,"Cancel login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG,"ERR login"+exception.toString());
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in with firebase
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i=new Intent(Login.this,MainActivity.class);
                    startActivity(i);

                } else {
                    //user is signed in with facebook
                    boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                    if(loggedIn){
                        Intent i=new Intent(Login.this,MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }

                }
            }
        };

        final Button button = (Button) findViewById(R.id.buttonSignin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reterives user inputs
                TextView email_text = (TextView)findViewById(R.id.email_login);
                TextView password_text = (TextView)findViewById(R.id.password_login);

                // trims the input
                String email = email_text.getText().toString().trim();
                String password = password_text.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Logging In Please Wait...");
                progressDialog.show();
                // When a user signs in to your app, pass the user's email address and password to signInWithEmailAndPassword
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "signInWithEmail", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent i=new Intent(Login.this,MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        });


            }
        });
        };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == signup_text){
            Log.i(TAG,"aici");
            LayoutInflater inflater = this.getLayoutInflater();
            View v=inflater.inflate(R.layout.dialog_signup, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText email_text_signup = (EditText) v.findViewById(R.id.email_signup);
            final EditText password_text_signup = (EditText) v.findViewById(R.id.password_signup);

            builder.setTitle("Sign Up")
                    .setView(v)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    // trims the input
                    String email = email_text_signup.getText().toString().trim();
                    String password = password_text_signup.getText().toString().trim();

                    //checking if email and passwords are empty
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //if the email and password are not empty
                    //displaying a progress dialog
                    progressDialog.setMessage("Registering Please Wait...");
                    progressDialog.show();

                    //creating a new user
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //checking if success
                                    progressDialog.dismiss();
                                    Log.i(TAG,"oncomplete");
                                    if (task.isSuccessful()) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Log.i(TAG,"oncomplete not successful");
                                        //display some message here
                                        Toast.makeText(Login.this, "Registration Error", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                }})
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }



}
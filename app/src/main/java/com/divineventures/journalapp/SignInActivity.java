package com.divineventures.journalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signIn;
    private GoogleApiClient googleApiClient;
    private Button register,login;
    private EditText userMail, userPwd;
    private ProgressDialog progressDialog;
    private static final int REQ_CODE = 1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Sign in");

        //signIn = findViewById(R.id.btn_signIn);
        //signIn.setOnClickListener(this);
        userMail = findViewById(R.id.Edit_mail);
        userPwd = findViewById(R.id.Edit_paswd);
        register = findViewById(R.id.btn_signUp);
        login = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null)
        {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        sendResultToActivity(currentUser);
        }
    }

    private void signInUser() {
        String email = userMail.getText().toString();
        String password = userPwd.getText().toString();
        if (email.isEmpty()  || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            userMail.setError("invalid Email Address ");
            return;
        }
        if (password.isEmpty())
        {
            userPwd.setError("Field cannot be empty");
            return;
        }
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                sendResultToActivity(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // ...
                        }
                        });

        }



    private void userSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void sendResultToActivity(FirebaseUser user) {
        //String name = user.getDisplayName();
        String email = user.getEmail();
        //String img_url = user.getPhotoUrl().toString();

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("userName", name);
        intent.putExtra("userMail", email);
        //intent.putExtra("userImage", img_url);
        startActivity(intent);
        finish();


    }

    /**
     * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     * super.onActivityResult(requestCode, resultCode, data);
     * <p>
     * if (requestCode == REQ_CODE) {
     * GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
     * sendResultToActivity(result);
     * }
     * }
     **/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_signIn:
                userSignIn();
                break;
            case R.id.btn_signUp :
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btn_login :
                signInUser();
                break;

        }
    }
}

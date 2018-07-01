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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText userMail, userPwd;
    private ProgressDialog progressDialog;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        if (getActionBar() != null)
        getActionBar().setDisplayHomeAsUpEnabled(true);

        userMail = findViewById(R.id.Edit_mail);
        userPwd = findViewById(R.id.Edit_paswd);
        register = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
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
            progressDialog.setMessage("Registering, Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
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
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                sendResultToActivity(null);
                            }
                        }
                        // ...
                    });
        }

    private void sendResultToActivity(FirebaseUser user) {
        //String name = user.getDisplayName();
        //String email = user.getEmail();
        //String img_url = user.getPhotoUrl().toString();

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("userName", name);
        //intent.putExtra("userMail", email);
        //intent.putExtra("userImage", img_url);
        startActivity(intent);
        finish();


    }
}

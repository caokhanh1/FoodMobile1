package com.example.foodproject.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email, pwd;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView view;
    private static final String TAG = "LoginActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextText);
        pwd = findViewById(R.id.editTextText1);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        view=findViewById(R.id.textView3);
view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class );
        startActivity(intent);
        finish();
    }
});
        //Login User
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = email.getText().toString();
                String textPwd = pwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
                    email.setError("Name is required");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter email", Toast.LENGTH_LONG).show();
                    email.setError("Valid email is required");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    pwd.setError("Password is required");
                    pwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        });
    }

    private void loginUser(String emai, String pass) {
        auth.signInWithEmailAndPassword(emai, pass).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "You are logged now", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class );
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                email.setError("User dose not exist");
                                email.requestFocus();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                email.setError("Invalid credentials, re-enter");
                                email.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private ProgressBar progressBar;
    private TextView view;
    private static final String TAG = "SignupActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressBar = findViewById(R.id.progressBar1);
        etName = findViewById(R.id.createname);
        etEmail = findViewById(R.id.createEmail);
        etPassword = findViewById(R.id.createPass);
        view=findViewById(R.id.textView34);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class );
                startActivity(intent);
                finish();
            }
        });
        Button button = findViewById(R.id.signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignupActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                    etName.setError("Name is required");
                    etName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Please re-enter email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Valid email is required");
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password should be least at 6 digits", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password to weak");
                    etPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    SignUpUser(name, email, password);
                }
            }


        });
    }

    private void SignUpUser(String name, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "User sign up successfull", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            firebaseUser.sendEmailVerification();

                            Intent intent = new Intent(SignupActivity.this,LoginActivity.class );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                etPassword.setError("Your password is too weak and requires additional special characters or numbers");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                etPassword.setError("Your email is invalid or already in use");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                etPassword.setError("User is already registered with this email email. Use another email");
                                etPassword.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    }
                });
    }
}

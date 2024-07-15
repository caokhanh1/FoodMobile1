package com.example.foodproject.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText emailEditText;
    Button resetPasswordButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    emailEditText.setError("Valid email is required");
                    emailEditText.requestFocus();
                } else {
                    checkEmailAndResetPassword(email);
                }
            }
        });
    }

    private void checkEmailAndResetPassword(String email) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    boolean isEmailRegistered = !task.getResult().getSignInMethods().isEmpty();
                    if (isEmailRegistered) {
                        resetPassword(email);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email does not exist", Toast.LENGTH_SHORT).show();
                        emailEditText.setError("Email not registered");
                        emailEditText.requestFocus();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to check email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

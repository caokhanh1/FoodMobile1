package com.example.foodproject.Activity.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    Button verifyCurrentPasswordButton, changePasswordButton, exitButtonFromVerify, exitButtonFromChange;
    LinearLayout verifyCurrentPassTitle, changePasswordLayout;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo FirebaseAuth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Liên kết các view với các biến
        currentPasswordEditText = findViewById(R.id.currentPas);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        verifyCurrentPasswordButton = findViewById(R.id.verifyCurrentPas);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        verifyCurrentPassTitle = findViewById(R.id.verifyChangePassLayout);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        exitButtonFromVerify = findViewById(R.id.exitButtonFromVerify);
        exitButtonFromChange = findViewById(R.id.exitButtonFromChange);

        exitButtonFromVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        exitButtonFromChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        verifyCurrentPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(currentPassword)) {
                    currentPasswordEditText.setError("Current password is required");
                    currentPasswordEditText.requestFocus();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            verifyCurrentPassTitle.setVisibility(View.GONE);
                            changePasswordLayout.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String currentPassword = currentPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(newPassword)) {
                    newPasswordEditText.setError("New Password is required");
                    newPasswordEditText.requestFocus();
                    return;
                }
                if (newPassword.length() < 6) {
                    newPasswordEditText.setError("Password should be at least 6 characters");
                    newPasswordEditText.requestFocus();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    confirmPasswordEditText.setError("Passwords do not match");
                    confirmPasswordEditText.requestFocus();
                    return;
                }
                if (newPassword.equals(currentPassword)) {
                    newPasswordEditText.setError("New password cannot be the same as the current password");
                    newPasswordEditText.requestFocus();
                    return;
                }
                changePassword(newPassword);
            }
        });
    }

    private void changePassword(String newPassword) {
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.foodproject.Activity.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

    private EditText verifyEmailEditText, newPasswordEditText, confirmPasswordEditText;
    private Button verifyEmailButton, changePasswordButton;
    private LinearLayout verifyEmailLayout, changePasswordLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo FirebaseAuth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Liên kết các view với các biến
        verifyEmailEditText = findViewById(R.id.verifyEmailEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        verifyEmailButton = findViewById(R.id.verifyEmailButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        verifyEmailLayout = findViewById(R.id.verifyEmailLayout);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = verifyEmailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    verifyEmailEditText.setError("Email is required");
                    verifyEmailEditText.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    verifyEmailEditText.setError("Valid email is required");
                    verifyEmailEditText.requestFocus();
                    return;
                }
                if (user != null && email.equals(user.getEmail())) {
                    verifyEmail();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Email does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

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
                changePassword(newPassword);
            }
        });
    }

    private void verifyEmail() {
        // Hiển thị phần đổi mật khẩu
        verifyEmailLayout.setVisibility(View.GONE);
        changePasswordLayout.setVisibility(View.VISIBLE);
    }

    private void changePassword(String newPassword) {
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.foodproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        view=findViewById(R.id.nameuser);
        user=auth.getCurrentUser();
        if (user==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class );
            startActivity(intent);
            finish();
        }else{
            view.setText(user.getEmail());
        }
    }
}
package com.example.foodproject.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodproject.Activity.Adapter.ListFavoriteAdapter;
import com.example.foodproject.Activity.Helper.ChangeNumberItemsListener;
import com.example.foodproject.Activity.Helper.ManagementListFavorite;
import com.example.foodproject.R;

import com.example.foodproject.databinding.ActivityListFoodFavoriteBinding;

public class ListFoodFavoriteActivity extends BaseActivity {
    ActivityListFoodFavoriteBinding binding;
    private ManagementListFavorite managementListFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListFoodFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managementListFavorite = new ManagementListFavorite(this);
        setVariable();
        initCartList();


    }

    private void initCartList() {
        if(managementListFavorite.getListFavorite().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scroviewFavorite.setVisibility(View.VISIBLE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scroviewFavorite.setVisibility(View.VISIBLE);
        }
        binding.listFavoView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.listFavoView.setAdapter(new ListFavoriteAdapter(managementListFavorite.getListFavorite(),managementListFavorite, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                initCartList();
            }
        }));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListFoodFavoriteActivity.this,MainActivity.class));

            }
        });
    }
}
package com.example.foodproject.Activity.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodproject.Activity.Adapter.FoodListAdapter;
import com.example.foodproject.Activity.Domain.Foods;
import com.example.foodproject.databinding.ActivityListFoodBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends BaseActivity {
    //liên ket voi layout cua activity thong qua viewbinding
    ActivityListFoodBinding binding;

    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        intitList();

    }

    private void intitList() {
        //Lấy tham chiếu đến nút "Foods" trong Firebase Realtime Database
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        // Tạo một truy vấn để lấy các món ăn có CategoryId bằng với categoryId đã lấy từ Intent
        Query query = myRef.orderByChild("CategoryId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue :
                            snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    // Nếu danh sách không rỗng, thiết lập RecyclerView
                    if (list.size() > 0){
                        // Thiết lập LayoutManager cho RecyclerView
                        binding.foodListView.setLayoutManager(new LinearLayoutManager(ListFoodActivity.this, LinearLayoutManager.VERTICAL, false));
                        // Thiết lập Adapter cho RecyclerView với danh sách món ăn
                        binding.foodListView.setAdapter(new FoodListAdapter(list));
                    }
                    // Ẩn thanh tiến trình sau khi tải dữ liệu xong
                    binding.progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
package com.example.foodproject.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.foodproject.Activity.Adapter.CategoryAdapter;
import com.example.foodproject.Activity.Adapter.FoodListAdapter;
import com.example.foodproject.Activity.Adapter.SliderAdapter;
import com.example.foodproject.Activity.Domain.Category;
import com.example.foodproject.Activity.Domain.Foods;
import com.example.foodproject.Activity.Domain.SliderItems;
import com.example.foodproject.R;
import com.example.foodproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import android.view.KeyEvent;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView view;
    ActivityMainBinding binding;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.nameuser);
        user = auth.getCurrentUser();
        Uri uri = user.getPhotoUrl();
        Picasso.get().load(uri).into(binding.imageView5);
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            view.setText(user.getDisplayName());
        }

        initCategory();
        initBanner();
        setupSearch();
        setVariable();

        binding.backBtn.setOnClickListener(v -> {
            binding.searchEditText.setText("");
            initCategory();
            binding.backBtn.setVisibility(View.GONE);
        });
    }
    private void setupSearch() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String titleKeyword = v.getText().toString().trim();
                if (!titleKeyword.isEmpty()) {
                    searchFoodsByTitle(titleKeyword);
                    binding.backBtn.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a title to search.", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
    }
    private void searchFoodsByTitle(String titleKeyword) {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null && food.getTitle() != null && food.getTitle().toLowerCase().contains(titleKeyword.toLowerCase())) {
                            list.add(food);
                        }
                    }
                    if (list.size() > 0) {
                        binding.categoryView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        binding.categoryView.setAdapter(new FoodListAdapter(list));
                    } else {
                        Toast.makeText(MainActivity.this, "No foods found with the given title.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No foods available.", Toast.LENGTH_SHORT).show();
                }
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpager2.setAdapter(new SliderAdapter(items, binding.viewpager2));
        binding.viewpager2.setClipChildren(false);
        binding.viewpager2.setClipToPadding(false);
        binding.viewpager2.setOffscreenPageLimit(3);
        binding.viewpager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpager2.setPageTransformer(compositePageTransformer);

        // Start the auto-slide
        sliderHandler.postDelayed(sliderRunnable, 10000);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = binding.viewpager2.getCurrentItem();
            int itemCount = binding.viewpager2.getAdapter().getItemCount();
            binding.viewpager2.setCurrentItem((currentItem + 1) % itemCount);
            sliderHandler.postDelayed(this, 1000);
        }
    };

    private void setVariable() {
        binding.bottomMenu.setItemSelected(R.id.home, true);
        binding.bottomMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.favorites) {
                    startActivity(new Intent(MainActivity.this, ListFoodFavoriteActivity.class));
                }
                if (i == R.id.cart) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                }
                if (i == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                }
                if (i == R.id.map) {
                    startActivity(new Intent(MainActivity.this, GoogleMapActivity.class));
                }
            }
        });

    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Category category = issue.getValue(Category.class);
                        if (category != null) {
                            list.add(category);
                        }
                    }
                    if (list.size() > 0) {
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                        binding.categoryView.setAdapter(new CategoryAdapter(list));

                    } else {
                        Toast.makeText(MainActivity.this, "No categories found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No categories available.", Toast.LENGTH_SHORT).show();
                }
                binding.backBtn.setVisibility(View.GONE);
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to avoid memory leaks
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}

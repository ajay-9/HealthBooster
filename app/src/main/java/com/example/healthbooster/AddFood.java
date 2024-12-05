package com.example.healthbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbooster.Activities.StepCounterActivity;
import com.example.healthbooster.databinding.ActivityAddFoodBinding;

public class AddFood extends AppCompatActivity {

    // Declare View Binding instance
    private ActivityAddFoodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set OnClickListener for the "Add Food" button
        binding.addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFood.this, BrowseFoodType.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the "Heart Rate" button
        binding.heartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFood.this, HeartRate.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the "Heart Rate" button
        binding.caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFood.this, StepCounterActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.healthbooster.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.healthbooster.Database.AppDatabase;
import com.example.healthbooster.Models.WaterEntry;
import com.example.healthbooster.R;
import com.example.healthbooster.databinding.ActivityWaterTrackerBinding;

import java.util.List;


public class WaterTrackerActivity extends AppCompatActivity {

    private ActivityWaterTrackerBinding binding;
    private int waterGoal = 2000; // Default daily goal
    private int waterConsumed = 0;
    private boolean halfwayNotificationSentFlag = false;

    private AppDatabase db;


    private static final String CHANNEL_ID = "WATER_TRACKER_NOTIFICATIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityWaterTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Display default goal and progress
        binding.waterGoalText.setText(String.format("Goal: %d ml", waterGoal));
        updateProgress();

        // Initialize Room database with main-thread query allowance
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "healthbooster-db")
                .allowMainThreadQueries() // Allows main-thread database queries
                .build();

        // Load previously consumed water
        loadWaterData();
//        // Initialize Room database
//        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "healthbooster-db").build();
//
//        // Initialize ExecutorService
//        executorService = Executors.newSingleThreadExecutor();

        // Load previously consumed water
//        executorService.execute(() -> { // Run on a separate thread to avoid blocking the UI thread while reading from the database
//            List<WaterEntry> waterEntries = db.waterDao().getAllWaterEntries();
//            for (WaterEntry entry : waterEntries) {
//                waterConsumed += entry.amount;
//            }
//            runOnUiThread(this::updateProgress); // Update UI on the main thread
//        });

        // Add Water button
        binding.addWaterButton.setOnClickListener(v -> addWater(250)); // Add 250 ml by default

        // Change Goal button
        binding.changeGoalButton.setOnClickListener(v -> changeGoal());

        // Reset button for goal and consumed water
        binding.resetButton.setOnClickListener(v -> reset());


        // Create Notification Channel
        createNotificationChannel();
    }

    private void reset() {
        waterGoal = 2000;
        waterConsumed = 0;
        halfwayNotificationSentFlag = false;
        binding.waterGoalText.setText(String.format("Goal: %d ml", waterGoal));
        updateProgress();
        Toast.makeText(this, "Water tracker reset successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadWaterData() {
        // Load water entries directly on the main thread
        List<WaterEntry> waterEntries = db.waterDao().getAllWaterEntries();
        for (WaterEntry entry : waterEntries) {
            waterConsumed += entry.amount;
        }

        // Update progress immediately
        updateProgress();
    }

    // Change the daily water goal
    private void changeGoal() {
        String newGoalInput = binding.newGoalInput.getText().toString().trim();
        if (!newGoalInput.isEmpty()) {
            waterGoal = Integer.parseInt(newGoalInput);
            binding.waterGoalText.setText(String.format("Goal: %d ml", waterGoal));
            binding.newGoalInput.setText(""); // Clear input field
            binding.newGoalInput.setVisibility(android.view.View.GONE);
            Toast.makeText(this, "Goal updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            binding.newGoalInput.setVisibility(android.view.View.VISIBLE);
        }
    }


    // Add water to the daily total
    private void addWater(int amount) {
        // Update water consumed
        waterConsumed += amount;

//        // Save new water entry in the database
//        executorService.execute(() -> {
//            WaterEntry entry = new WaterEntry();
//            entry.timestamp = System.currentTimeMillis();
//            entry.amount = amount;
//            db.waterDao().insertWaterEntry(entry);
//        });

        // Save new water entry in the database
        WaterEntry entry = new WaterEntry();
        entry.timestamp = System.currentTimeMillis();
        entry.amount = amount;
        db.waterDao().insertWaterEntry(entry);

        // Update UI and check for notifications
        updateProgress();
        // Check for notifications
        checkNotifications();
    }

    // Update the progress bar and text
    private void updateProgress() {
        binding.waterConsumedText.setText(String.format("Consumed: %d ml", waterConsumed));
        binding.waterProgressBar.setProgress((waterConsumed * 100) / waterGoal);
    }

    // Check if notifications need to be sent
    private void checkNotifications() {
        if (waterConsumed >= waterGoal / 2 && waterConsumed < waterGoal && !halfwayNotificationSentFlag) {
            sendNotification("Halfway There!", "You've reached half your water goal!");
            halfwayNotificationSentFlag = true;
        } else if (waterConsumed >= waterGoal) {
            sendNotification("Goal Achieved!", "Congratulations! You've reached your water goal!");
        }
    }

    // Send a notification
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.water)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    // Create a notification channel for the app
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Water Tracker Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for water tracking milestones");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

//    // Close the ExecutorService when the activity is destroyed
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (executorService != null && !executorService.isShutdown()) {
//            executorService.shutdown();
//        }
//    }
}

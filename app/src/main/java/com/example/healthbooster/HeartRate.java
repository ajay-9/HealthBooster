package com.example.healthbooster;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.healthbooster.Model.HeartBeatModel;
import com.example.healthbooster.databinding.ActivityHeartRateBinding;

public class HeartRate extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private ActivityHeartRateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityHeartRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(); // Initialize sensor and permissions
    }

    private void init() {
        // Check and request permission for BODY_SENSORS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BODY_SENSORS}, 5);
        }

        // Get SensorManager and Heart Rate Sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];

            // Update Heart Rate value in EditText
            binding.heartRateValue.setText(String.valueOf(heartRate));

            // Save Heart Rate details
            HeartBeatModel rate = createRateDetail(null, heartRate);
            writeRateDetails(rate);
        }
    }

    private HeartBeatModel createRateDetail(String id, float heartRate) {
        if (id == null) {
            id = String.valueOf(System.currentTimeMillis());
        }
        return new HeartBeatModel(id, heartRate);
    }

    private void writeRateDetails(HeartBeatModel rate) {
        if (rate != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("heart_rate", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(rate.getId(), String.valueOf(rate.getHeartRate()));
            editor.apply();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                showToast("High Accuracy");
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                showToast("Medium Accuracy");
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                showToast("Low Accuracy");
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                showToast("Unreliable Sensor");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

package com.example.healthbooster.Models;

public class HeartBeatModel {
    private String id;
    private float heartRate;

    public HeartBeatModel(String id, float heartRate) {
        this.id = id;
        this.heartRate = heartRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public String toString() {
        return "HeartBeatModel{" +
                "id='" + id + '\'' +
                ", heartRate=" + heartRate +
                '}';
    }
}

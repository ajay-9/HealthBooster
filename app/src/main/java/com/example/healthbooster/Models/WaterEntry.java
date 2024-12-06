package com.example.healthbooster.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WaterEntry {
    @PrimaryKey(autoGenerate = true)
    public int id; // Unique ID for each water entry

    public long timestamp; // Time when water was consumed
    public int amount; // Amount of water consumed (in milliliters)
}

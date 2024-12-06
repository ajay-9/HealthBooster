package com.example.healthbooster.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.healthbooster.Database.WaterDao;
import com.example.healthbooster.Models.WaterEntry;

@Database(entities = {WaterEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WaterDao waterDao();
}

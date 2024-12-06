package com.example.healthbooster.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthbooster.Models.WaterEntry;

import java.util.List;

@Dao
public interface WaterDao {
    @Insert
    void insertWaterEntry(WaterEntry entry);

    @Query("SELECT * FROM WaterEntry")
    List<WaterEntry> getAllWaterEntries();

    @Query("DELETE FROM WaterEntry WHERE id = :id")
    void deleteWaterEntry(int id);
}

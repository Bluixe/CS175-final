package com.bluixe.jani

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlanItem::class], version = 1, exportSchema = false)
abstract class PlanItemDatabase : RoomDatabase() {
    abstract fun planItemDao(): PlanItemDao
}
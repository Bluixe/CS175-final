package com.bluixe.jani

import androidx.room.*

@Dao
interface PlanItemDao {
    @Insert
    fun insertAll(vararg items: PlanItem)

    @Delete
    fun delete(item: PlanItem)

    @Query("SELECT * FROM planitem")
    fun getAll(): List<PlanItem>

    @Query("DELETE FROM planitem")
    fun deleteAll()

    @Update
    fun update(item: PlanItem)
}
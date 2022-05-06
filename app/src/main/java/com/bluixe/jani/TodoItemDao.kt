package com.bluixe.jani

import androidx.room.*

@Dao
interface TodoItemDao {
    @Insert
    fun insetAll(vararg items: TodoItem)

    @Delete
    fun delete(item: TodoItem)

    @Query("SELECT * FROM todoitem")
    fun getAll(): List<TodoItem>

    @Query("SELECT * FROM todoitem WHERE date/10000 == :date")
    fun queryDate(date: Long): List<TodoItem>

    @Query("SELECT * FROM todoitem WHERE date/10000 > :date and date/10000 < :date+40")
    fun queryMonth(date: Long): List<TodoItem>
    @Query("SELECT * FROM todoitem WHERE date == :date")
    fun queryTime(date:Long):List<TodoItem>
    @Query("DELETE FROM todoitem")
    fun deleteAll()

    @Update
    fun update(item: TodoItem)
}
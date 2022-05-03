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

    @Query("DELETE FROM todoitem")
    fun deleteAll()

    @Update
    fun update(item: TodoItem)
}
package com.bluixe.jani

import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room


class Calendar : AppCompatActivity() {
    lateinit var db: TodoItemDatabase
    lateinit var dao: TodoItemDao
    lateinit var adapter: CalendarAdapter
    lateinit var rv: RecyclerView
    var date: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        db = Room.databaseBuilder(
            applicationContext,
            TodoItemDatabase::class.java, "todoitem"
        ).build()
        dao = db.todoItemDao()
        rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = CalendarAdapter(this)
        var calenderView = findViewById<CalendarView>(R.id.calendar)
        calenderView.setOnDateChangeListener { calendarView, i, i2, i3 -> run{
            val mul:Long = 100
            date = (i * mul + i2+1)*mul + i3
            Thread {
                val data = dao.queryDate(date)
                runOnUiThread {
                    adapter.setContentList(data)
                    rv.adapter = adapter
                }
            }.start()
        }  }

    }
    fun deleteItem(item:TodoItem){
        Thread {
            dao.delete(item)
            val data = dao.queryDate(date)

            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }
    fun updateItem(item:TodoItem){
        Thread {
            dao.update(TodoItem(item.id, item.date, item.content, !(item.status)!!))
            val data = dao.queryDate(date)

            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }

}
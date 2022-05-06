package com.bluixe.jani

import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.util.Calendar


class Calendar : AppCompatActivity() {
    lateinit var db: TodoItemDatabase
    lateinit var dao: TodoItemDao
    lateinit var adapter: CalendarAdapter
    lateinit var adapter2: CalendarAdapter
    lateinit var rv: RecyclerView
    lateinit var rv2: RecyclerView
    var date: Long = 0
    var month: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        db = Room.databaseBuilder(
            applicationContext,
            TodoItemDatabase::class.java, "todoitem"
        ).build()
        dao = db.todoItemDao()
        rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv2 = findViewById<RecyclerView>(R.id.recycler_view2)
        rv.layoutManager = LinearLayoutManager(this)
        rv2.layoutManager = LinearLayoutManager(this)
        adapter = CalendarAdapter(this)
        adapter2 = CalendarAdapter(this)
        var calendar = Calendar.getInstance()
        month = (calendar.get(Calendar.YEAR)*100 + calendar.get(Calendar.MONTH)+1)*100.toLong()
        date = month + calendar.get(Calendar.DAY_OF_MONTH)
        Thread {
            val data = dao.queryDate(date)
            var monthdata = dao.queryMonth(month)
            monthdata = monthdata.sortedBy { it.date }
            runOnUiThread {
                adapter.setContentList(data)
                adapter2.setContentList(monthdata)
                rv.adapter = adapter
                rv2.adapter = adapter2
            }
        }.start()
        var calenderView = findViewById<CalendarView>(R.id.calendar)
        calenderView.setOnDateChangeListener { calendarView, i, i2, i3 -> run{
            val mul:Long = 100
            date = (i * mul + i2+1)*mul + i3
            month = (i * mul + i2+1)*mul
            Thread {
                val data = dao.queryDate(date)
                var monthdata = dao.queryMonth(month)
                monthdata = monthdata.sortedBy { it.date }
                runOnUiThread {
                    adapter.setContentList(data)
                    adapter2.setContentList(monthdata)
                    rv.adapter = adapter
                    rv2.adapter = adapter2
                }
            }.start()
        }  }

    }
    fun deleteItem(item:TodoItem){
        Thread {
            dao.delete(item)
            val data = dao.queryDate(date)
            var monthdata = dao.queryMonth(month)
            monthdata = monthdata.sortedBy { it.date }
            runOnUiThread {
                adapter.setContentList(data)
                adapter2.setContentList(monthdata)
                rv.adapter = adapter
                rv2.adapter = adapter2
            }
        }.start()
    }
    fun updateItem(item:TodoItem){
        Thread {
            dao.update(TodoItem(item.id, item.date, item.content, !(item.status)!!))
            val data = dao.queryDate(date)
            var monthdata = dao.queryMonth(month)
            monthdata = monthdata.sortedBy { it.date }
            runOnUiThread {
                adapter.setContentList(data)
                adapter2.setContentList(monthdata)
                rv.adapter = adapter
                rv2.adapter = adapter2
            }
        }.start()
    }

}
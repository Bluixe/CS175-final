package com.bluixe.jani

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DDL : AppCompatActivity() {
    lateinit var db: TodoItemDatabase
    lateinit var dao: TodoItemDao
    lateinit var adapter: TodoItemAdapter
    lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ddl)
        db = Room.databaseBuilder(
            applicationContext,
            TodoItemDatabase::class.java, "todoitem"
        ).build()
        dao = db.todoItemDao()
        rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        val handler = Handler(Looper.getMainLooper())
        adapter = TodoItemAdapter(this)
//        var data: List<TodoItem>
        Thread {
//            dao.deleteAll()
            var data = dao.getAll()
            data = data.sortedBy { it.date }
            adapter.setContentList(data)
            runOnUiThread {
                rv.adapter = adapter
                rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            }

        }.start()


        val addButton = findViewById<FloatingActionButton>(R.id.add_todo)
        addButton.setOnClickListener {
            val ddl_dialog = layoutInflater.inflate(R.layout.ddl_dialog, findViewById(R.id.ddl_dialog))
            var date: Long = 20200101
            ddl_dialog.findViewById<Button>(R.id.choose).setOnClickListener {
                val date_dialog = layoutInflater.inflate(R.layout.date_selector, findViewById(R.id.date_selector))
                val datePicker = date_dialog.findViewById<DatePicker>(R.id.date_picker)
                val selector = MaterialAlertDialogBuilder(this)
                    .setView(date_dialog)
                    .setPositiveButton("确定", DialogInterface.OnClickListener {
                            dialogInterface, i -> run {
                                val day = datePicker.dayOfMonth
                                val month = datePicker.month + 1
                                val year = datePicker.year
                                val mul:Long = 100
                                date = (year * mul + month)*mul + day
                                ddl_dialog.findViewById<TextView>(R.id.et).text=String.format("%04d/%02d/%02d",year,month,day)
                    }  })
                    .setNegativeButton("取消",null)
                    .create()
                selector.show()
            }
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setView(ddl_dialog)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", DialogInterface.OnClickListener {
                        dialogInterface, i -> run {
                            val content = ddl_dialog.findViewById<EditText>(R.id.ct).text.toString()
                            Thread {
                                dao.insetAll(TodoItem(0, date, content, true))
                                var data = dao.getAll()
                                data = data.sortedBy { it.date }

                                runOnUiThread {
                                    adapter.setContentList(data)
                                    rv.adapter = adapter
                                }
                            }.start()

                } })
                .create()
            dialog.show()
//                .show()
        }


    }
    fun deleteItem(item:TodoItem){
        Thread {
            dao.delete(item)
            var data = dao.getAll()
            data = data.sortedBy { it.date }

            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }
    fun updateItem(item:TodoItem){
        Thread {
            dao.update(TodoItem(item.id, item.date, item.content, !(item.status)!!))
            var data = dao.getAll()
            data = data.sortedBy { it.date }

            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }

}
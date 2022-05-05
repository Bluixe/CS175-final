package com.bluixe.jani

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Plan : AppCompatActivity() {
    lateinit var db: PlanItemDatabase
    lateinit var dao: PlanItemDao
    lateinit var adapter: PlanItemAdapter
    lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        db = Room.databaseBuilder(
            applicationContext,
            PlanItemDatabase::class.java, "planitem"
        ).build()
        dao = db.planItemDao()
        rv = findViewById(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = PlanItemAdapter(this)
        Thread {
            var data = dao.getAll()
            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
                rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            }
        }.start()
        val addButton = findViewById<FloatingActionButton>(R.id.add_plan)
        addButton.setOnClickListener {
            val plan_dialog = layoutInflater.inflate(R.layout.plan_dialog, findViewById(R.id.plan_dialog))
            plan_dialog.findViewById<Button>(R.id.clear).setOnClickListener {
                plan_dialog.findViewById<EditText>(R.id.pl_ct).setText("")
            }
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setView(plan_dialog)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", DialogInterface.OnClickListener {
                        dialogInterface, i -> run {
                            val title = plan_dialog.findViewById<EditText>(R.id.pl_tt).text.toString()
                            val content = plan_dialog.findViewById<EditText>(R.id.pl_ct).text.toString()
                            Thread {
                                dao.insertAll(PlanItem(0, title, content, true))
                                var data = dao.getAll()
                                runOnUiThread {
                                    adapter.setContentList(data)
                                    rv.adapter = adapter
                                }
                            }.start()
                }  })
                .create()
            dialog.show()
        }
    }





    fun updateItem(item: PlanItem){
        Thread {
            dao.update(PlanItem(item.id, item.title, item.content, !(item.status)!!))
            var data = dao.getAll()
            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }

    fun deleteItem(item:PlanItem){
        Thread {
            dao.delete(item)
            var data = dao.getAll()

            runOnUiThread {
                adapter.setContentList(data)
                rv.adapter = adapter
            }
        }.start()
    }

    fun changeTitle(item: PlanItem){
        val modify_dialog = layoutInflater.inflate(R.layout.modify_dialog, findViewById(R.id.modify_dialog))
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(modify_dialog)
            .setTitle("修改标题：")
            .setPositiveButton("确定", DialogInterface.OnClickListener {
                    dialogInterface, i -> run {
                        val title = modify_dialog.findViewById<EditText>(R.id.ct).text.toString()
        //                        val content = plan_dialog.findViewById<EditText>(R.id.pl_ct).text.toString()
                        Thread {
                            dao.update(PlanItem(item.id, title, item.content, item.status))
                            var data = dao.getAll()
                            runOnUiThread {
                                adapter.setContentList(data)
                                rv.adapter = adapter
                            }
                        }.start()
                    }
            })
            .setNegativeButton("取消", null)
            .create()
        dialog.show()
    }
    fun changeContent(item: PlanItem){
        val modify_dialog = layoutInflater.inflate(R.layout.modify_dialog, findViewById(R.id.modify_dialog))
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(modify_dialog)
            .setTitle("修改内容：")
            .setPositiveButton("确定", DialogInterface.OnClickListener {
                    dialogInterface, i -> run {
                val content = modify_dialog.findViewById<EditText>(R.id.ct).text.toString()
                //                        val content = plan_dialog.findViewById<EditText>(R.id.pl_ct).text.toString()
                Thread {
                    dao.update(PlanItem(item.id, item.title, content, item.status))
                    var data = dao.getAll()
                    runOnUiThread {
                        adapter.setContentList(data)
                        rv.adapter = adapter
                    }
                }.start()
            }
            })
            .setNegativeButton("取消", null)
            .create()
        dialog.show()
    }
}
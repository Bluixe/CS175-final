package com.bluixe.jani

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoItemAdapter(activity:DDL) : RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    private val contentList = mutableListOf<TodoItem>()
    private val ddlActivity = activity

    inner class TodoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.findViewById<TextView>(R.id.date)
        private val itemContent = view.findViewById<TextView>(R.id.content)
        private val status = view.findViewById<TextView>(R.id.status)
        private val delete = view.findViewById<Button>(R.id.delete)
        fun bind(position: Int, content: TodoItem ){
            var text = content.date.toString()
            text = text.substring(4, 6) + "-" + text.substring(6, 8) + " " + text.substring(8, 10) + ":" + text.substring(10,12)
            date.text = text
            itemContent.text = content.content
            if (content.status == true) {
                status.text = "未完成"
//                status.setTextColor(0xC62828)
            } else {
                status.text = "已完成"
//                status.setTextColor(0x1B5E20)
            }
            delete.setOnClickListener {
                ddlActivity.deleteItem(content)
            }
            status.setOnClickListener {
                ddlActivity.updateItem(content)
            }

        }
    }

    fun setContentList(list: List<TodoItem>){
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
//        val v = View.inflate(parent.context, R.layout.todo_item, null)
        return TodoItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(position, contentList[position])
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

}
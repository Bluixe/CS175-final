package com.bluixe.jani

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PlanItemAdapter(activity: Plan) : RecyclerView.Adapter<PlanItemAdapter.PlanItemViewHolder>() {

    private val contentList = mutableListOf<PlanItem>()
    private val plActivity = activity
    private var curPos: Int? = null

    inner class PlanItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val head = view.findViewById<LinearLayout>(R.id.pl_head)
        private val status = view.findViewById<TextView>(R.id.pl_status)
        private val title = view.findViewById<TextView>(R.id.pl_title)
        private val other = view.findViewById<LinearLayout>(R.id.pl_hidden)
        private val cont = view.findViewById<TextView>(R.id.pl_content)
        private val delete = view.findViewById<Button>(R.id.delete)
        private val mod_title = view.findViewById<TextView>(R.id.mod_title)
        private val mod_cont = view.findViewById<TextView>(R.id.mod_cont)
        fun bind(position: Int, content: PlanItem) {
            title.text = content.title
            cont.text = content.content
            if (content.status == true) {
                status.text = "未完成"
//                status.setTextColor(0xC62828)
            } else {
                status.text = "已完成"
//                status.setTextColor(0x1B5E20)
            }
            status.setOnClickListener {
                plActivity.updateItem(content)
            }
            mod_title.setOnClickListener {
                plActivity.changeTitle(content)
            }
            mod_cont.setOnClickListener {
                plActivity.changeContent(content)
            }
            delete.setOnClickListener {
                plActivity.deleteItem(content)
            }
            if(position==curPos){
                head.setBackgroundColor(ContextCompat.getColor(plActivity, R.color.grey_50))
                other.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            } else {
                head.setBackgroundColor(0x000000)
                other.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
            }
        }
    }

    fun setContentList(list: List<PlanItem>){
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanItemAdapter.PlanItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        return PlanItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanItemAdapter.PlanItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if(curPos != position.toInt()){
                curPos = position.toInt()
            } else {
                curPos = null
            }

            notifyDataSetChanged()
        }
        holder.bind(position, contentList[position])
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}
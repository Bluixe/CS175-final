package com.bluixe.jani

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlanItemAdapter(activity: Plan) : RecyclerView.Adapter<PlanItemAdapter.PlanItemViewHolder>() {

    private val contentList = mutableListOf<PlanItem>()
    private val plActivity = activity
    private var curPos: Int? = null

    inner class PlanItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val status = view.findViewById<TextView>(R.id.pl_status)
        private val title = view.findViewById<TextView>(R.id.pl_content)
        private val other = view.findViewById<LinearLayout>(R.id.pl_hidden)
        private val mod_title = view.findViewById<TextView>(R.id.mod_title)
        private val mod_cont = view.findViewById<TextView>(R.id.mod_cont)
        fun bind(position: Int, content: PlanItem) {
            title.text = content.title
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

            }
            if(position==curPos){
                other.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            } else {
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
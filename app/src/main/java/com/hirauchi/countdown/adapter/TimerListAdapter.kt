package com.hirauchi.countdown.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.countdown.R

class TimerListAdapter: RecyclerView.Adapter<TimerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_timer, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mDelete.setOnClickListener {
        }

        holder.mStart.setOnClickListener {
        }

        holder.mStop.setOnClickListener {
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTimerName: TextView = view.findViewById(R.id.timer_name)
        val mTimerTime: TextView = view.findViewById(R.id.timer_time)
        val mDelete: ImageView = view.findViewById(R.id.delete)
        val mStart: Button = view.findViewById(R.id.start)
        val mStop: Button = view.findViewById(R.id.stop)
    }
}
package com.hirauchi.countdown.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.countdown.R
import com.hirauchi.countdown.model.Timer
import java.text.SimpleDateFormat
import java.util.*

class TimerListAdapter(val mListener: OnTimerListener): RecyclerView.Adapter<TimerListAdapter.ViewHolder>() {

    lateinit var mTimerList: List<Timer>

    interface OnTimerListener {
        fun onDeleteClicked(timer: Timer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_timer, parent, false))
    }

    override fun getItemCount(): Int {
        return mTimerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timer = mTimerList.get(position)

        holder.mTimerName.text = timer.name

        val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        dataFormat.timeZone = TimeZone.getTimeZone("UTC")
        holder.mTimerTime.text = dataFormat.format(timer.time)

        holder.mDelete.setOnClickListener {
            mListener.onDeleteClicked(timer)
        }

        holder.mStart.setOnClickListener {
        }

        holder.mStop.setOnClickListener {
        }
    }

    fun setTimerList(timerList: List<Timer>) {
        mTimerList = timerList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTimerName: TextView = view.findViewById(R.id.timer_name)
        val mTimerTime: TextView = view.findViewById(R.id.timer_time)
        val mDelete: ImageView = view.findViewById(R.id.delete)
        val mStart: Button = view.findViewById(R.id.start)
        val mStop: Button = view.findViewById(R.id.stop)
    }
}
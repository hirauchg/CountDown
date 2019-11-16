package com.hirauchi.countdown.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.countdown.CountDown
import com.hirauchi.countdown.OnCountDownListener
import com.hirauchi.countdown.R
import com.hirauchi.countdown.model.Timer
import java.text.SimpleDateFormat
import java.util.*

class TimerListAdapter(val mContext: Context, val mListener: OnTimerListener): RecyclerView.Adapter<TimerListAdapter.ViewHolder>(), OnCountDownListener {

    private val COUNT_DOWN_INTERVAL: Long = 10

    lateinit var mTimerTime: TextView
    lateinit var mStart: Button
    lateinit var mTimerList: List<Timer>

    private val mDataFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    private var mIsStarting = false
    private var mCurrentTime: Long = 0

    interface OnTimerListener {
        fun onDeleteClicked(timer: Timer)
        fun onUpdateTimer(timer: Timer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_timer, parent, false))
    }

    override fun getItemCount(): Int {
        return mTimerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timer = mTimerList.get(position)

        var countDown = CountDown(timer.time, COUNT_DOWN_INTERVAL, this)

        holder.mTimerName.text = timer.name
        holder.mTimerName.setOnClickListener {
            showNameChangeDialog(timer)
        }

        mDataFormat.timeZone = TimeZone.getTimeZone("UTC")
        mTimerTime = holder.mTimerTime
        holder.mTimerTime.text = mDataFormat.format(timer.time)
        holder.mTimerTime.setOnClickListener {
            showTimerPickerDialog(timer)
        }

        holder.mDelete.setOnClickListener {
            mListener.onDeleteClicked(timer)
        }

        mStart = holder.mStart
        holder.mStart.setOnClickListener {
            if (!mIsStarting) {
                countDown.start()
                holder.mStart.setText(R.string.main_timer_stop)
            } else {
                countDown.cancel()
                countDown = CountDown(mCurrentTime, COUNT_DOWN_INTERVAL, this)
                holder.mStart.setText(R.string.main_timer_start)
            }

            mIsStarting = !mIsStarting
        }

        holder.mReset.setOnClickListener {
            countDown.cancel()
            countDown = CountDown(timer.time, COUNT_DOWN_INTERVAL, this)
            holder.mTimerTime.text = mDataFormat.format(timer.time)
            mStart.isEnabled = true
            holder.mStart.setText(R.string.main_timer_start)
            mIsStarting = false
        }
    }

    override fun onTick(millisUntilFinished: Long) {
        mCurrentTime = millisUntilFinished
        mTimerTime.setText(mDataFormat.format(millisUntilFinished))
    }

    override fun onFinish() {
        mTimerTime.setText(mDataFormat.format(0))
        mStart.isEnabled = false
    }

    fun showNameChangeDialog(timer: Timer) {
        val container = LinearLayout(mContext)
        val editText = EditText(mContext)
        editText.setText(timer.name)
        val editTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        editTextParams.setMargins(56, 56, 56, 0)
        container.addView(editText, editTextParams)

        AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.main_name_change_dialog_title))
                .setView(container)
                .setPositiveButton(mContext.getString(R.string.ok)) { _, _ ->
                    timer.name = editText.text.toString()
                    mListener.onUpdateTimer(timer)
                }
                .setNegativeButton(mContext.getString(R.string.cancel), null)
                .show()
    }

    fun showTimerPickerDialog(timer: Timer) {
        val container = LinearLayout(mContext)
        container.orientation = LinearLayout.HORIZONTAL
        container.setPadding(70, 30, 30, 0)

        val hourValue = timer.time / 60 / 60 / 1000
        val minuteValue = timer.time % (60 * 60 * 1000) / 60 / 1000
        val secondValue = timer.time % (60 * 1000) / 1000

        val valueList = arrayOf(hourValue, minuteValue, secondValue)
        val maxValueList = arrayOf(23, 59, 59)
        val unitTextList = arrayOf(mContext.getString(R.string.main_timer_hour_unit), mContext.getString(R.string.main_timer_minute_unit), mContext.getString(R.string.main_timer_second_unit))
        val unitWeightList = arrayOf(0.8F, 0.5F, 0.5F)
        val numberPickerList = arrayListOf<NumberPicker>()

        for (i in valueList.indices) {
            val picker = NumberPicker(mContext)
            picker.maxValue = maxValueList[i]
            picker.value = valueList[i].toInt()
            numberPickerList.add(picker)

            val unit = TextView(mContext)
            unit.text = unitTextList[i]
            unit.gravity = Gravity.CENTER
            unit.setTextColor(ContextCompat.getColor(mContext, R.color.textColor1))

            container.addView(picker, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F))
            container.addView(unit, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, unitWeightList[i]))
        }

        AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.main_timer_select_dialog_title))
                .setView(container)
                .setPositiveButton(mContext.getString(R.string.ok)) { _, _ ->
                    val hour = numberPickerList.get(0).value * 60 * 60 * 1000
                    val minute = numberPickerList.get(1).value * 60 * 1000
                    val second = numberPickerList.get(2).value * 1000

                    timer.time = (hour + minute + second).toLong()
                    mListener.onUpdateTimer(timer)
                }
                .setNegativeButton(mContext.getString(R.string.cancel), null)
                .show()
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
        val mReset: Button = view.findViewById(R.id.reset)
    }
}
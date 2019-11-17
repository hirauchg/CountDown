package com.hirauchi.countdown.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hirauchi.countdown.R
import com.hirauchi.countdown.model.Timer
import java.text.SimpleDateFormat
import java.util.*

class TimerFragment : Fragment() {

    private val COUNT_DOWN_INTERVAL: Long = 10
    private val CHANNEL_ID = "cannel_id_timer"
    private val CHANNEL_NAME = "cannel_name_timer"

    private lateinit var mContext: Context
    private lateinit var mTimer: Timer
    private lateinit var mListener: OnTimerListener

    lateinit var mTimerTime: TextView
    lateinit var mStart: Button

    private val mDataFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    private var mIsStarting = false
    private var mCurrentTime: Long = 0

    interface OnTimerListener {
        fun onDeleteClicked(timer: Timer)
        fun onUpdateTimer(timer: Timer)
    }

    companion object {
        fun newInstance(timer: Timer): TimerFragment {
            val fragment = TimerFragment()
            val bundle = Bundle()
            bundle.putSerializable("timer", timer)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        mTimer = args?.getSerializable("timer") as Timer
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onAttach(context: Context){
        mContext = context
        if (context is OnTimerListener) {
            mListener = context
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mTimerName: TextView = view.findViewById(R.id.timer_name)
        mTimerTime = view.findViewById(R.id.timer_time)
        val mDelete: ImageView = view.findViewById(R.id.delete)
        mStart = view.findViewById(R.id.start)
        val mReset: Button = view.findViewById(R.id.reset)

        var countDown = CountDown(mTimer.time, COUNT_DOWN_INTERVAL)

        mTimerName.text = mTimer.name
        mTimerName.setOnClickListener {
            if (!mIsStarting) {
                showNameChangeDialog(mTimer)
            }
        }

        mDataFormat.timeZone = TimeZone.getTimeZone("UTC")
        mTimerTime.text = mDataFormat.format(mTimer.time)
        mTimerTime.setOnClickListener {
            if (!mIsStarting) {
                showTimerPickerDialog(mTimer)
            }
        }

        mDelete.setOnClickListener {
            mListener.onDeleteClicked(mTimer)
        }

        mStart.setOnClickListener {
            if (!mIsStarting) {
                countDown.start()
                mStart.setText(R.string.main_timer_stop)
            } else {
                countDown.cancel()
                countDown = CountDown(mCurrentTime, COUNT_DOWN_INTERVAL)
                mStart.setText(R.string.main_timer_start)
            }

            mIsStarting = !mIsStarting
        }

        mReset.setOnClickListener {
            countDown.cancel()
            countDown = CountDown(mTimer.time, COUNT_DOWN_INTERVAL)
            mTimerTime.text = mDataFormat.format(mTimer.time)
            mStart.isEnabled = true
            mStart.setText(R.string.main_timer_start)
            mIsStarting = false
        }
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

    private fun createNotification() {
        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(cannel)
        }

        val notification = NotificationCompat.Builder(mContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentTitle(mContext.getString(R.string.app_name))
            setContentText(mContext.getString(R.string.main_notification_text, mTimer.name))
        }.build()

        notificationManager.notify(mTimer.id, notification)
    }

    inner class CountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            mCurrentTime = millisUntilFinished
            mTimerTime.setText(mDataFormat.format(millisUntilFinished))
        }

        override fun onFinish() {
            mTimerTime.setText(mDataFormat.format(0))
            mStart.isEnabled = false
            createNotification()
        }
    }
}
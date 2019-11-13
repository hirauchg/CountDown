package com.hirauchi.countdown.manager

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.hirauchi.countdown.database.TimerContract
import com.hirauchi.countdown.database.TimerDBHelper
import com.hirauchi.countdown.model.Timer

class TimerManager(context: Context) {

    val mDBHelper = TimerDBHelper(context)

    fun close() {
        mDBHelper.close()
    }

    fun getTimerList(): List<Timer> {
        val db = mDBHelper.readableDatabase

        val cursor = db.query(TimerContract.TimerEntry.TABLE_NAME, null, null, null, null, null, null)
        val timerList = mutableListOf<Timer>()
        with(cursor) {
            while (moveToNext()) {
                val timer = Timer(
                    getInt(getColumnIndexOrThrow(BaseColumns._ID)),
                    getString(getColumnIndexOrThrow(TimerContract.TimerEntry.COLUMN_NAME)),
                    getLong(getColumnIndexOrThrow(TimerContract.TimerEntry.COLUMN_TIME)))
                timerList.add(timer)
            }
        }

        return timerList
    }

    fun addTimer(name: String) {
        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(TimerContract.TimerEntry.COLUMN_NAME, name)
            put(TimerContract.TimerEntry.COLUMN_TIME, 60000) // TODO
        }

        db?.insert(TimerContract.TimerEntry.TABLE_NAME, null, values)
    }

    fun updateTimer(id: Int, name: String) {
        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(TimerContract.TimerEntry.COLUMN_NAME, name)
        }

        db.update(TimerContract.TimerEntry.TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(id.toString()))
    }

    fun deleteTimer(id: Int) {
        val db = mDBHelper.writableDatabase
        db.delete(TimerContract.TimerEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString()))
    }
}
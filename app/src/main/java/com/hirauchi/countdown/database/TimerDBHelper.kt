package com.hirauchi.countdown.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object TimerContract {
    object TimerEntry : BaseColumns {
        const val TABLE_NAME = "timer"
        const val COLUMN_NAME = "name"
        const val COLUMN_TIME = "time"
    }
}

class TimerDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Timer.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TimerContract.TimerEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${TimerContract.TimerEntry.COLUMN_NAME} TEXT," +
                    "${TimerContract.TimerEntry.COLUMN_TIME} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TimerContract.TimerEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}
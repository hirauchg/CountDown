package com.hirauchi.countdown.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.hirauchi.countdown.R
import com.hirauchi.countdown.manager.TimerManager
import com.hirauchi.countdown.model.Timer
import android.widget.LinearLayout
import com.hirauchi.countdown.fragment.TimerFragment

class MainActivity : AppCompatActivity(), TimerFragment.OnTimerListener {

    lateinit var mTimerList: List<Timer>
    lateinit var mTimerManager: TimerManager

    lateinit var mContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContainer = findViewById(R.id.container)

        mTimerManager = TimerManager(this)
        mTimerList = mTimerManager.getTimerList()

        mContainer.removeAllViews()
        for (timer in mTimerList) {
            showTimer(timer)
        }
    }

    override fun onDestroy() {
        mTimerManager.close()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.main_menu_add -> {
                showAddTimerDialog()
            }

            R.id.main_menu_info -> startActivity(Intent(this, AppInfoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddTimerDialog() {
        val container = LinearLayout(this)
        val editText = EditText(this)
        val editTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        editTextParams.setMargins(56, 56, 56, 0)
        container.addView(editText, editTextParams)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.main_add_timer_dialog_title))
            .setView(container)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                addTimer(editText.text.toString())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDeleteClicked(timer: Timer) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.main_delete_timer_dialog_title))
            .setMessage(getString(R.string.main_delete_timer_dialog_message, timer.name))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                deleteTimer(timer.id)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showTimer(timer: Timer) {
        val frame = FrameLayout(this)
        frame.id = timer.id
        mContainer.addView(frame)

        supportFragmentManager.beginTransaction().replace(frame.id, TimerFragment.newInstance(timer)).commit()
    }

    private fun addTimer(name: String) {
        mTimerManager.addTimer(name)?.let {
            mTimerManager.getTimer(it)?.let {
                showTimer(it)
            }
        }
    }

    private fun deleteTimer(id: Int) {
        mTimerManager.deleteTimer(id)
        mContainer.removeView(mContainer.findViewById<FrameLayout>(id))
    }

    override fun onUpdateTimer(timer: Timer) {
        mTimerManager.updateTimer(timer)
        supportFragmentManager.beginTransaction().replace(timer.id, TimerFragment.newInstance(timer)).commit()
    }
}

package com.hirauchi.countdown.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.countdown.R
import com.hirauchi.countdown.adapter.TimerListAdapter
import com.hirauchi.countdown.manager.TimerManager
import com.hirauchi.countdown.model.Timer
import android.widget.LinearLayout

class MainActivity : AppCompatActivity(), TimerListAdapter.OnTimerListener {

    lateinit var mTimerList: List<Timer>
    lateinit var mTimerManager: TimerManager
    lateinit var mAdapter: TimerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        mAdapter = TimerListAdapter(this, this)
        recyclerView.setAdapter(mAdapter)

        mTimerManager = TimerManager(this)
        loadTimerList()
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
                mTimerManager.addTimer(editText.text.toString())
                loadTimerList()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun loadTimerList() {
        mTimerList = mTimerManager.getTimerList()
        mAdapter.setTimerList(mTimerList)
    }

    override fun onDeleteClicked(timer: Timer) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.main_delete_timer_dialog_title))
            .setMessage(getString(R.string.main_delete_timer_dialog_message, timer.name))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                deleteTimer(timer.id)
                loadTimerList()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun deleteTimer(id: Int) {
        mTimerManager.deleteTimer(id)
    }

    override fun onUpdateTimer(timer: Timer) {
        mTimerManager.updateTimer(timer)
        loadTimerList()
    }
}

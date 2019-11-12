package com.hirauchi.countdown.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.countdown.R
import com.hirauchi.countdown.adapter.TimerListAdapter
import com.hirauchi.countdown.manager.TimerManager
import com.hirauchi.countdown.model.Timer

class MainActivity : AppCompatActivity() {

    lateinit var mTimerList: List<Timer>
    lateinit var mTimerManager: TimerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        val adapter = TimerListAdapter()
        recyclerView.setAdapter(adapter)

        mTimerManager = TimerManager(this)
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
                
            }

            R.id.main_menu_info -> startActivity(Intent(this, AppInfoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


}
